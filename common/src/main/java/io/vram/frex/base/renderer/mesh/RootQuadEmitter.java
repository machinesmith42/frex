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

package io.vram.frex.base.renderer.mesh;

import io.vram.frex.api.buffer.PooledQuadEmitter;
import io.vram.frex.api.buffer.PooledVertexEmitter;
import io.vram.frex.api.buffer.QuadTransform;
import io.vram.frex.api.model.InputContext;

public abstract class RootQuadEmitter extends BaseQuadEmitter {
	protected final TransformStack transformStack;

	public RootQuadEmitter() {
		transformStack = createTransformStack();
	}

	/** Override to use custom stack. */
	protected TransformStack createTransformStack() {
		return new TransformStack();
	}

	@Override
	public PooledQuadEmitter withTransformQuad(InputContext context, QuadTransform transform) {
		return transformStack.createTransform(context, transform, this);
	}

	@Override
	public PooledVertexEmitter withTransformVertex(InputContext context, QuadTransform transform) {
		return transformStack.createTransform(context, transform, this);
	}
}
