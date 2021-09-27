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

package io.vram.frex.impl.renderloop;

import io.vram.frex.api.renderloop.FrustumSetupListener;
import io.vram.frex.api.renderloop.WorldRenderContext;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class FrustumSetupListenerImpl {
	private static final ObjectArrayList<FrustumSetupListener> LISTENERS = new ObjectArrayList<>();
	private static FrustumSetupListener active = ctx -> { };

	public static void register(FrustumSetupListener listener) {
		LISTENERS.add(listener);

		if (LISTENERS.size() == 1) {
			active = LISTENERS.get(0);
		} else if (LISTENERS.size() == 2) {
			active = ctx -> {
				final int limit = LISTENERS.size();

				for (int i = 0; i < limit; ++i) {
					LISTENERS.get(i).afterFrustumSetup(ctx);
				}
			};
		}
	}

	public static void invoke(WorldRenderContext ctx) {
		active.afterFrustumSetup(ctx);
	}
}
