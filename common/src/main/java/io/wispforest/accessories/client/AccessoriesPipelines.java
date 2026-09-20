package io.wispforest.accessories.client;

import net.minecraft.client.renderer.BindGroupLayouts;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import io.wispforest.accessories.Accessories;
import io.wispforest.owo.ui.event.WindowResizeCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;

import java.util.function.Consumer;

public class AccessoriesPipelines {

    // Textured GUI quad whose vertex color is interpreted as HSV instead of RGB, masked by the texture alpha
    // Same layout as the vanilla textured GUI snippet, which is not accessible from the common module
    private static final RenderPipeline.Snippet GUI_TEXTURED_SNIPPET = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .buildSnippet();

    public static final RenderPipeline.Snippet SPECTRUM_SNIPPET = RenderPipeline.builder(GUI_TEXTURED_SNIPPET)
            .withFragmentShader(Accessories.of("core/spectrum_position_tex"))
            .withVertexShader(Accessories.of("core/spectrum_position_tex"))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .buildSnippet();

    public static final RenderPipeline SPECTRUM = RenderPipeline.builder(SPECTRUM_SNIPPET)
            .withLocation(Accessories.of("pipeline/spectrum"))
            .build();

    public static void registerPipelines(Consumer<RenderPipeline> pipelineRegister) {
        pipelineRegister.accept(SPECTRUM);
    }

    private static TextureTarget BUFFER = null;

    public static TextureTarget getOrCreateBuffer() {
        try {
            if (BUFFER == null) {
                var window = Minecraft.getInstance().getWindow();

                BUFFER = new TextureTarget("accessories_buffer_thingy", window.getWidth(), window.getHeight(), GpuFormat.RGBA8_UNORM, GpuFormat.D32_FLOAT);

                WindowResizeCallback.EVENT.register((innerClient, innerWindow) -> {
                    if (BUFFER == null) return;
                    BUFFER.resize(innerWindow.getWidth(), innerWindow.getHeight());
                });
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create the buffer for Accessories Hover Rendering due to an error!", e);
        }

        return BUFFER;
    }
}
