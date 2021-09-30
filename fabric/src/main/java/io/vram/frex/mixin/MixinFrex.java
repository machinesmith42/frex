/*
 * Copyright © Contributing Authors
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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;

import io.vram.frex.api.renderer.Renderer;
import io.vram.frex.compat.fabric.FabricRenderer;

import grondag.frex.Frex;

@Mixin(Frex.class)
public abstract class MixinFrex {
	/**
	 * @author grondag
	 * @reason Fabric API compatibility
	 */
	@Overwrite(remap = false)
	private static void setupRenderer() {
		RendererAccess.INSTANCE.registerRenderer(FabricRenderer.of(Renderer.get()));
	}
}