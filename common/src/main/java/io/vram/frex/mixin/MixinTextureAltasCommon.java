/*
 * Copyright © Original Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional copyright and licensing notices may apply for content that was
 * included from other projects. For more information, see ATTRIBUTION.md.
 */

package io.vram.frex.mixin;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import io.vram.frex.impl.texture.IndexedSprite;
import io.vram.frex.impl.texture.SpriteIndexImpl;
import io.vram.frex.impl.texture.SpriteInjectorImpl;

@Mixin(TextureAtlas.class)
public class MixinTextureAltasCommon {
	@Shadow @Final private ResourceLocation location;
	@Shadow @Final private Map<ResourceLocation, TextureAtlasSprite> texturesByName;

	@Inject(at = @At("RETURN"), method = "reload")
	private void afterReload(TextureAtlas.Preparations input, CallbackInfo ci) {
		final ObjectArrayList<TextureAtlasSprite> spriteIndexList = new ObjectArrayList<>();
		int index = 0;

		for (final TextureAtlasSprite sprite : texturesByName.values()) {
			spriteIndexList.add(sprite);
			final var spriteExt = (IndexedSprite) sprite;
			spriteExt.frex_index(index++);
		}

		SpriteIndexImpl.getOrCreate(location).reset(input, spriteIndexList, (TextureAtlas) (Object) this);
	}

	@Inject(at = @At("HEAD"), method = "getBasicSpriteInfos")
	private void onGetBasicSpriteInfos(ResourceManager resourceManager, Set<ResourceLocation> set, CallbackInfoReturnable<Collection<TextureAtlasSprite.Info>> ci) {
		SpriteInjectorImpl.forEachInjection(location, set::add);
	}
}
