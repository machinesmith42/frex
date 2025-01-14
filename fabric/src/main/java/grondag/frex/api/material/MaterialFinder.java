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

package grondag.frex.api.material;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import io.vram.frex.api.material.MaterialCondition;

import grondag.frex.api.Renderer;

/**
 * Finds standard {@link RenderMaterial} instances used to communicate
 * quad rendering characteristics to a {@link RenderContext}.
 *
 * <p>Must be obtained via {@link Renderer#materialFinder()}.
 */
@Deprecated
public interface MaterialFinder extends net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder {
	@Override
	MaterialFinder clear();

	/**
	 * @deprecated Emit multiple quads with translucency for decal effects.
	 */
	@Override
	@Deprecated
	default MaterialFinder spriteDepth(int depth) {
		// NOOP
		return this;
	}

	/**
	 * @deprecated Emit multiple quads with translucency for decal effects.
	 */
	@Override
	@Deprecated
	default MaterialFinder blendMode(int spriteIndex, BlendMode blendMode) {
		return blendMode(blendMode);
	}

	/**
	 * Defines how sprite pixels will be blended with the scene.
	 *
	 * <p>The application of blend mode is context-dependent, especially
	 * for {@link BlendMode#TRANSLUCENT}.  Various material properties
	 * will be set differently when the model is rendered as a block, vs.
	 * a moving entity block, vs. item in GUI and item floating in the world,
	 * as examples.
	 *
	 * <p>To avoid this ambiguity, pass {@code null} as the parameter and the material
	 * will always be applied without adjustment.
	 *
	 * @param blendMode
	 * @return
	 */
	MaterialFinder blendMode(@Nullable BlendMode blendMode);

	@Override
	@Deprecated
	default MaterialFinder disableColorIndex(int spriteIndex, boolean disable) {
		return disableColorIndex(disable);
	}

	MaterialFinder disableColorIndex(boolean disable);

	@Override
	@Deprecated
	default MaterialFinder disableDiffuse(int spriteIndex, boolean disable) {
		return disableDiffuse(disable);
	}

	MaterialFinder disableDiffuse(boolean disable);

	@Override
	@Deprecated
	default MaterialFinder disableAo(int spriteIndex, boolean disable) {
		return disableAo(disable);
	}

	MaterialFinder disableAo(boolean disable);

	@Override
	@Deprecated
	default MaterialFinder emissive(int spriteIndex, boolean isEmissive) {
		return emissive(isEmissive);
	}

	MaterialFinder emissive(boolean isEmissive);

	@Override
	RenderMaterial find();

	//////////////////////////////////////////////////
	//  FREX EXTENSIONS FOLLOW
	//////////////////////////////////////////////////

	MaterialFinder copyFrom(RenderMaterial material);

	/**
	 * Enables blur (bilinear texture sampling) on magnification,
	 * and makes minification blur LOD-linear. Used for enchantment glint.
	 *
	 * <p>Note this is isn't generally useful for atlas sprites and different from mipmap.
	 * Mipmap is done with linear sampling using nearest LOD because otherwise artifacts appear when
	 * sprites are sampled across LODS levels, especially for randomized rotated
	 * sprites like grass.
	 *
	 * @param blur [@code true} to enable bilinear texture sampling on magnification
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder blur(boolean blur);

	MaterialFinder condition(MaterialCondition condition);

	/**
	 * Enables or disables backface culling on GPU. Generally
	 * desired for most usage because it results in fewer polygons drawn.
	 *
	 * @param cull [@code true} to enable GPU face culling
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder cull(boolean cull);

	//Parameters to {@link #cutout(int)}.

	/** Disable cutout testing. */
	int CUTOUT_NONE = 0;
	/** Use 0.5 as cutout threshold. */
	int CUTOUT_HALF = 1;
	/** Use 0.1 as cutout threshold. */
	int CUTOUT_TENTH = 2;
	/** Use zero as cutout threshold. */
	int CUTOUT_ZERO = 3;
	/** Use vertex color alpha as cutout threshold. */
	int CUTOUT_ALPHA = 4;

	/**
	 * Directly enables cutout rendering without using {@link #blendMode(BlendMode)}.
	 * May be overridden if BlendMode is non-null.
	 *
	 * @param cutout mode to enable, one of the CUTOUT_ constants.
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder cutout(int cutout);

	/**
	 * Parameters to {@link MaterialFinder#decal(int)}.
	 */
	int DECAL_NONE = 0;
	int DECAL_POLYGON_OFFSET = 1;
	int DECAL_VIEW_OFFSET = 2;

	/**
	 * @param decal one of {@link MaterialFinder#DECAL_NONE}, {@link MaterialFinder#DECAL_POLYGON_OFFSET}
	 * or {@link MaterialFinder#DECAL_VIEW_OFFSET}
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder decal(int decal);

	/**
	 * Parameters to {@link MaterialFinder#depthTest(int)}.
	 */
	int DEPTH_TEST_DISABLE = 0;
	int DEPTH_TEST_ALWAYS = 1;
	int DEPTH_TEST_EQUAL = 2;
	int DEPTH_TEST_LEQUAL = 3;

	/**
	 * @param depthTest one of {@link MaterialFinder#DEPTH_TEST_DISABLE}, {@link MaterialFinder#DEPTH_TEST_ALWAYS},
	 * {@link MaterialFinder#DEPTH_TEST_EQUAL} or {@link MaterialFinder#DEPTH_TEST_LEQUAL}
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder depthTest(int depthTest);

	/**
	 * Performance hint for the renderer -
	 * set true when the material has a shader and the shader
	 * uses procedural texturing and does not use the bound texture.
	 *
	 * <p>Primary use case is to disable upload of animated textures
	 * that aren't going to be used.
	 *
	 * @param discardsTexture {@code true} to enable performance hint
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder discardsTexture(boolean discardsTexture);

	/**
	 * @deprecated No longer used or valid.
	 * The lightmap texture is always available to pipelines that request it.
	 * Will be removed in a subsequent release.
	 */
	@Deprecated
	@ScheduledForRemoval
	default MaterialFinder enableLightmap(boolean enableLightmap) {
		return this;
	}

	/**
	 * Enable or disable the translucent flash overlay used to indicate primed TNT.
	 *
	 * @param flashOverlay {@code true} to enable
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder flashOverlay(boolean flashOverlay);

	/**
	 * Enable or disables atmospheric fog for this material.
	 */
	MaterialFinder fog(boolean enable);

	/**
	 * @deprecated No longer has any effect and will be removed in a subsequent release.
	 */
	@Deprecated
	@ScheduledForRemoval
	default MaterialFinder gui(boolean gui) {
		// NOOP
		return this;
	}

	/**
	 * Enable or disable the translucent red overlay used to indicate damage.
	 *
	 * @param hurtOverlay {@code true} to enable
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder hurtOverlay(boolean hurtOverlay);

	/**
	 * Set the line width to the full-width default used by Minecraft.
	 * Enabled for most vanilla render layers except some debug renders.
	 *
	 * @param lines {@code true} to enable full-width line rendering
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder lines(boolean lines);

	MaterialFinder shader(@Nullable ResourceLocation vertexSourceId, @Nullable ResourceLocation fragmentSourceId);

	MaterialFinder shader(@Nullable ResourceLocation vertexSourceId, @Nullable ResourceLocation fragmentSourceId, @Nullable ResourceLocation depthVertexSourceId, @Nullable ResourceLocation depthFragmentSourceId);

	/**
	 * For transparent materials, enables sorting of quads by
	 * camera distance before buffering and drawing. Vanilla sorts
	 * all transparent render layers even when it isn't strictly needed.
	 *
	 * <p>Content authors should set this to false for translucent decal materials
	 * when it is known the quad will be backed by a solid quad to avoid the
	 * performance overhead of sorting.
	 *
	 * @param sorted {@code true} to enable sorting of quads by camera distance
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder sorted(boolean sorted);

	/**
	 * Parameters to {@link #target(int)}.
	 */
	int TARGET_MAIN = 0;
	int TARGET_OUTLINE = 1;
	int TARGET_TRANSLUCENT = 2;
	int TARGET_PARTICLES = 3;
	int TARGET_WEATHER = 4;
	int TARGET_CLOUDS = 5;
	int TARGET_ENTITIES = 6;

	/**
	 * Selects framebuffer target for material. Several of
	 * these are equivalent to MAIN unless Fabulous graphics are enabled.
	 * @param target identifies framebuffer target for material. One of
	 * {@link #TARGET_MAIN}, {@link #TARGET_OUTLINE}, {@link #TARGET_TRANSLUCENT},
	 * {@link #TARGET_PARTICLES}, {@link #TARGET_WEATHER}. {@link #TARGET_CLOUDS}
	 * or {@link #TARGET_ENTITIES}
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder target(int target);

	/**
	 * Select base color texture, or disable texturing.
	 * @param id namespaced id of texture or texture atlas for base color. {@code null} to disable texturing.
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder texture(@Nullable ResourceLocation id);

	/**
	 * Parameters to {@link MaterialFinder#transparency(int)}.
	 */
	int TRANSPARENCY_NONE = 0;
	/** Used for some particles and other effects. */
	int TRANSPARENCY_ADDITIVE = 1;
	/** Used for lightning. */
	int TRANSPARENCY_LIGHTNING = 2;
	/** Used for enchantment glint. */
	int TRANSPARENCY_GLINT = 3;
	/** Used for block breaking overlay. */
	int TRANSPARENCY_CRUMBLING = 4;
	/** Used for terrain blocks, decals and most other use cases. */
	int TRANSPARENCY_TRANSLUCENT = 5;
	/** Used for terrain particles. */
	int TRANSPARENCY_DEFAULT = 6;

	/**
	 * Enables or disables texture blending and sets blending mode.
	 *
	 * <p>If {@link #blendMode(BlendMode)} is used to set a non-null blend mode,
	 * then this setting will be adjusted to an appropriate context-dependent
	 * value at runtime and is essentially ignored.
	 *
	 * @param transparency one of {@link MaterialFinder#TRANSPARENCY_NONE}, {@link MaterialFinder#TRANSPARENCY_ADDITIVE},
	 * {@link MaterialFinder#TRANSPARENCY_LIGHTNING}, {@link MaterialFinder#TRANSPARENCY_GLINT},
	 * {@link MaterialFinder#TRANSPARENCY_CRUMBLING}, {@link MaterialFinder#TRANSPARENCY_TRANSLUCENT},
	 * or {@link MaterialFinder#TRANSPARENCY_DEFAULT}
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder transparency(int transparency);

	/**
	 * Directly enables mipped rendering without using {@link #blendMode(BlendMode)}.
	 * May be overridden if BlendMode is non-null.
	 *
	 * @param unmipped {@code true} to enable
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder unmipped(boolean unmipped);

	/**
	 * Parameters to {@link MaterialFinder#writeMask(int)}.
	 */
	int WRITE_MASK_COLOR = 0;
	int WRITE_MASK_DEPTH = 1;
	int WRITE_MASK_COLOR_DEPTH = 2;

	/**
	 * Determines which standard framebuffer attachments are written.
	 * Implementations may have additional framebuffer attachments not
	 * controlled by this setting.
	 *
	 * @param writeMask one of {@link MaterialFinder#WRITE_MASK_COLOR}, {@link MaterialFinder#WRITE_MASK_DEPTH},
	 * or {@link MaterialFinder#WRITE_MASK_COLOR_DEPTH}
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder writeMask(int writeMask);

	/**
	 * Set true when material should cast shadows. Pipelines that lack
	 * shadowmaps or some other mechanism for realistic shadows will
	 * ignore this.  Defaults to true.
	 *
	 * <p>Set false for materials that render as solid for practical
	 * reasons but should not cast shadows.  Beacon beams are a vanilla example.
	 *
	 * @param castShadows Set true if material should cast shadows.
	 * @return finder instance for ease of chaining calls
	 */
	MaterialFinder castShadows(boolean castShadows);
}
