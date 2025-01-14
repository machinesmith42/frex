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

package io.vram.frex.pastel.mixinterface;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;

import io.vram.frex.api.world.RenderRegionBakeListener;
import io.vram.frex.pastel.PastelTerrainRenderContext;

public interface RenderChunkRegionExt {
	int frx_cachedAoLevel(int cacheIndex);

	int frx_cachedBrightness(int cacheIndex);

	int frx_cachedBrightness(BlockPos pos);

	boolean frx_isClosed(int cacheIndex);

	@Nullable
	Object frx_getBlockEntityRenderData(BlockPos pos);

	PastelTerrainRenderContext frx_getContext();

	void frx_setContext(PastelTerrainRenderContext context, BlockPos origin);

	@Nullable
	RenderRegionBakeListener[] frx_getRenderRegionListeners();

	Biome frx_getBiome(BlockPos pos);
}
