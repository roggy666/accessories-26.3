package io.wispforest.accessories.client;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import io.wispforest.accessories.Accessories;
import io.wispforest.owo.ui.event.WindowResizeCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;

import java.util.function.Consumer;

public class AccessoriesPipelines {

    // Textured GUI quad whose vertex color is interpreted as HSV instead of RGB, masked by the texture alpha
    public static final RenderPipeline.Snippet SPECTRUM_SNIPPET = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
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

                BUFFER = new TextureTarget("accessories_buffer_thingy", window.getWidth(), window.getHeight(), true, GpuFormat.RGBA8_UNORM);

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
