package io.wispforest.accessories.mixin.client.owo;

import io.wispforest.accessories.client.gui.AccessoriesScreen;
import io.wispforest.owo.compat.rei.OwoReiPlugin;
import io.wispforest.owo.ui.base.BaseOwoContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(value = OwoReiPlugin.class, remap = false)
public abstract class OwoReiPluginMixin {

    // owo-lib 0.13.1+26.3 numbers the BaseOwoContainerScreen exclusion lambda as $1
    @Inject(method = "lambda$registerExclusionZones$1", at = @At("HEAD"), remap = false, cancellable = true)
    private static void accessories$preventZonesForAccessoriesScreen(BaseOwoContainerScreen screen, CallbackInfoReturnable<Collection> cir) {
        if(screen instanceof AccessoriesScreen) cir.setReturnValue(List.of());
    }
}
