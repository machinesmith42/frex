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

package io.vram.frex.api.rendertype;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import io.vram.frex.impl.material.BlockPresetsImpl;

/**
 * Use to associate blocks or fluids with render types other than default.
 * They can be retrieved via the vanilla utility {@code ItemBlockRenderTypes}.
 */
public interface BlockPresets {
	static void mapBlocks(int preset, Block... blocks) {
		BlockPresetsImpl.mapBlocks(preset, blocks);
	}

	static void mapBlocks(RenderType chunkRenderType, Block... blocks) {
		BlockPresetsImpl.mapBlocks(chunkRenderType, blocks);
	}

	static void mapFluids(int preset, Fluid... fluids) {
		BlockPresetsImpl.mapFluids(preset, fluids);
	}

	static void mapFluids(RenderType chunkRenderType, Fluid... fluids) {
		BlockPresetsImpl.mapFluids(chunkRenderType, fluids);
	}
}
