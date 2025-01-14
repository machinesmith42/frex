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

import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import io.vram.frex.impl.world.BlockEntityRenderDataImpl;
import io.vram.frex.impl.world.BlockEntityRenderDataProviderAccess;

@Mixin(BlockEntityType.class)
public class MixinBlockEntityType implements BlockEntityRenderDataProviderAccess {
	private Function<BlockEntity, Object> frxDataProvider = BlockEntityRenderDataImpl.defaultProvider();

	@Override
	public Function<BlockEntity, Object> frxGetDataProvider() {
		return frxDataProvider;
	}

	@Override
	public void frxSetDataProvider(Function<BlockEntity, Object> provider) {
		frxDataProvider = provider;
	}
}
