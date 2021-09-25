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

package io.vram.frex.impl.material.predicate;

import io.vram.frex.api.material.RenderMaterial;

import net.minecraft.world.entity.Entity;

public class EntityMaterialOnly extends EntityBiPredicate {
	private final MaterialPredicate materialPredicate;

	public EntityMaterialOnly(MaterialPredicate materialPredicate) {
		this.materialPredicate = materialPredicate;
	}

	@Override
	public boolean test(Entity ignored, RenderMaterial renderMaterial) {
		return materialPredicate.test(renderMaterial);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EntityMaterialOnly) {
			return materialPredicate.equals(((EntityMaterialOnly) obj).materialPredicate);
		} else {
			return false;
		}
	}
}