/*
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License.  You may obtain a copy
 *  of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package io.vram.frex.impl.material;

import static io.vram.frex.impl.material.predicate.MaterialPredicate.MATERIAL_ALWAYS_TRUE;
import static io.vram.frex.impl.material.predicate.StateBiPredicate.BLOCK_ALWAYS_TRUE;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.BiPredicate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.vram.frex.api.material.BlockEntityMaterialMap;
import io.vram.frex.impl.FrexLog;
import io.vram.frex.impl.material.predicate.MaterialPredicate;
import io.vram.frex.impl.material.predicate.MaterialPredicateDeserializer;
import io.vram.frex.impl.material.predicate.StateBiPredicate;
import io.vram.frex.impl.material.predicate.StateMaterialBoth;
import io.vram.frex.impl.material.predicate.StateMaterialOnly;
import io.vram.frex.impl.material.predicate.StateOnly;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;

@Internal
public class BlockEntityMaterialMapDeserializer {
	public static void deserialize(BlockEntityType<?> key, ResourceLocation idForLog, List<Resource> orderedResourceList, IdentityHashMap<BlockEntityType<?>, BlockEntityMaterialMap> map) {
		try {
			final JsonObject[] reversedJsonList = new JsonObject[orderedResourceList.size()];
			final String[] packIds = new String[orderedResourceList.size()];
			final String idString = idForLog.toString();

			final BlockEntityMaterialMap defaultMap = BlockEntityMaterialMap.IDENTITY;
			MaterialTransform defaultTransform = MaterialTransform.IDENTITY;
			BlockEntityMaterialMap result = defaultMap;

			int j = 0;

			for (int i = orderedResourceList.size(); i-- > 0; ) {
				final InputStreamReader reader = new InputStreamReader(orderedResourceList.get(i).getInputStream(), StandardCharsets.UTF_8);
				final JsonObject json = GsonHelper.parse(reader);

				reversedJsonList[j] = json;
				packIds[j] = orderedResourceList.get(i).getSourceName();

				// Only read top-most resource for defaultMaterial
				if (json.has("defaultMaterial") && defaultTransform == MaterialTransform.IDENTITY) {
					defaultTransform = MaterialTransformLoader.loadTransform(formatLog(packIds[j], idString), json.get("defaultMaterial").getAsString(), defaultTransform);
					result = new BlockEntitySingleMaterialMap(BLOCK_ALWAYS_TRUE, defaultTransform);
				}

				j++;
			}

			result = loadBlockEntityMaterialMap(idString, packIds, reversedJsonList, result, defaultTransform);

			if (result != defaultMap) {
				map.put(key, result);
			}
		} catch (final Exception e) {
			FrexLog.warn("Unable to load block entity material map for " + idForLog.toString() + " due to unhandled exception:", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static BlockEntityMaterialMap loadBlockEntityMaterialMap(String idForLog, String[] packIdsForLog, JsonObject[] reversedJsonList, BlockEntityMaterialMap defaultMap, MaterialTransform defaultTransform) {
		try {
			final ObjectArrayList<StateBiPredicate> predicates = new ObjectArrayList<>();
			final ObjectArrayList<MaterialTransform> transforms = new ObjectArrayList<>();

			for (int jsonId = 0; jsonId < reversedJsonList.length; jsonId++) {
				final String packId = packIdsForLog[jsonId];
				final JsonObject json = reversedJsonList[jsonId];
				final JsonArray jsonArray = json.getAsJsonArray("map");

				if (jsonArray == null || jsonArray.isJsonNull() || jsonArray.size() == 0) {
					continue;
				}

				final int limit = jsonArray.size();

				for (int i = 0; i < limit; ++i) {
					final JsonObject obj = jsonArray.get(i).getAsJsonObject();

					if (!obj.has("predicate")) {
						FrexLog.warn("Unable to load entity material map " + formatLog(packId, idForLog) + " because 'predicate' tag is missing. Using default material.");
						continue;
					}

					if (!obj.has("material")) {
						FrexLog.warn("Unable to load entity material map " + formatLog(packId, idForLog) + " because 'material' tag is missing. Using default material.");
						continue;
					}

					final StateBiPredicate predicate = loadPredicate(obj.get("predicate").getAsJsonObject());

					if (predicate == BLOCK_ALWAYS_TRUE) {
						FrexLog.warn("Empty material map predicate in " + formatLog(packId, idForLog) + " was skipped. This may indicate an invalid JSON.");
					} else {
						if (!predicates.contains(predicate)) {
							predicates.add(predicate);
							transforms.add(MaterialTransformLoader.loadTransform(formatLog(packId, idForLog), obj.get("material").getAsString(), defaultTransform));
						} else {
							// TODO: dev purpose only. remove or require dev env or config
							FrexLog.info("Found duplicate predicate in " + formatLog(packId, idForLog));
						}
					}
				}
			}

			if (predicates.isEmpty()) {
				return defaultMap;
			} else {
				predicates.add(BLOCK_ALWAYS_TRUE);
				transforms.add(defaultTransform);
				final int n = predicates.size();
				return new BlockEntityMultiMaterialMap(predicates.toArray(new BiPredicate[n]), transforms.toArray(new MaterialTransform[n]));
			}
		} catch (final Exception e) {
			FrexLog.warn("Unable to load material map " + idForLog + " because of exception. Using default material map.", e);
			return defaultMap;
		}
	}

	private static StateBiPredicate loadPredicate(JsonObject obj) {
		final StatePropertiesPredicate statePredicate = StatePropertiesPredicate.fromJson(obj.get("statePredicate"));
		final MaterialPredicate materialPredicate = MaterialPredicateDeserializer.deserialize(obj.get("materialPredicate").getAsJsonObject());

		if (statePredicate == StatePropertiesPredicate.ANY) {
			if (materialPredicate == MATERIAL_ALWAYS_TRUE) {
				return BLOCK_ALWAYS_TRUE;
			} else {
				return new StateMaterialOnly(materialPredicate);
			}
		} else {
			if (materialPredicate == MATERIAL_ALWAYS_TRUE) {
				return new StateOnly(statePredicate);
			} else {
				return new StateMaterialBoth(statePredicate, materialPredicate);
			}
		}
	}

	private static String formatLog(String packId, String id) {
		return String.format("%s;%s", packId, id);
	}
}