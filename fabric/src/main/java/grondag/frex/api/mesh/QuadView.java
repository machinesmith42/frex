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

package grondag.frex.api.mesh;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import grondag.frex.api.material.RenderMaterial;

@Deprecated
public interface QuadView extends net.fabricmc.fabric.api.renderer.v1.mesh.QuadView {
	/**
	 * Retrieves the material serialized with the quad.
	 */
	@Override
	RenderMaterial material();

	/**
	 * Reads baked vertex data and outputs standard baked quad
	 * vertex data in the given array and location.
	 *
	 * @param spriteIndex The sprite to be used for the quad.
	 * Behavior for values &gt; 0 is currently undefined.
	 *
	 * @param target Target array for the baked quad data.
	 *
	 * @param targetIndex Starting position in target array - array must have
	 * at least 28 elements available at this index.
	 *
	 * @param isItem If true, will output vertex normals. Otherwise will output
	 * lightmaps, per Minecraft vertex formats for baked models.
	 */
	@Override
	@Deprecated
	@ScheduledForRemoval
	default void toVanilla(int spriteIndex, int[] target, int targetIndex, boolean isItem) {
		toVanilla(target, targetIndex);
	}

	/**
	 * Reads baked vertex data and outputs standard baked quad
	 * vertex data in the given array and location.
	 *
	 * @param spriteIndex The sprite to be used for the quad.
	 * Behavior for values &gt; 0 is currently undefined.
	 *
	 * @param target Target array for the baked quad data.
	 *
	 * @param targetIndex Starting position in target array - array must have
	 * at least 28 elements available at this index.
	 *
	 * @param isItem If true, will output vertex normals. Otherwise will output
	 * lightmaps, per Minecraft vertex formats for baked models.
	 */
	void toVanilla(int[] target, int targetIndex);

	@Override
	@Deprecated
	@ScheduledForRemoval
	default BakedQuad toBakedQuad(int spriteIndex, TextureAtlasSprite sprite, boolean isItem) {
		return toBakedQuad(sprite);
	}

	/**
	 * Generates a new BakedQuad instance with texture
	 * coordinates and colors from the given sprite.
	 *
	 * @param spriteIndex The sprite to be used for the quad.
	 * Behavior for {@code spriteIndex > 0} is currently undefined.
	 *
	 * @param sprite  {@link MutableQuadView} does not serialize sprites
	 * so the sprite must be provided by the caller.
	 *
	 * @param isItem If true, will output vertex normals. Otherwise will output
	 * lightmaps, per Minecraft vertex formats for baked models.
	 *
	 * @return A new baked quad instance with the closest-available appearance
	 * supported by vanilla features. Will retain emissive light maps, for example,
	 * but the standard Minecraft renderer will not use them.
	 */
	default BakedQuad toBakedQuad(TextureAtlasSprite sprite) {
		final int[] vertexData = new int[VANILLA_QUAD_STRIDE];
		toVanilla(vertexData, 0);
		return new BakedQuad(vertexData, colorIndex(), lightFace(), sprite, true);
	}

	@Override
	@Deprecated
	@ScheduledForRemoval
	default int spriteColor(int vertexIndex, int spriteIndex) {
		return vertexColor(vertexIndex);
	}

	/**
	 * Retrieve vertex color.
	 */
	int vertexColor(int vertexIndex);

	@Override
	@Deprecated
	@ScheduledForRemoval
	default float spriteU(int vertexIndex, int spriteIndex) {
		return spriteU(vertexIndex);
	}

	/**
	 * Retrieve horizontal sprite atlas coordinates.
	 */
	float spriteU(int vertexIndex);

	@Override
	@Deprecated
	@ScheduledForRemoval
	default float spriteV(int vertexIndex, int spriteIndex) {
		return spriteV(vertexIndex);
	}

	/**
	 * Retrieve vertical sprite atlas coordinates.
	 */
	float spriteV(int vertexIndex);
}
