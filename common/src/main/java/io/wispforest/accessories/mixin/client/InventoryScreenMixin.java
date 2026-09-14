package io.wispforest.accessories.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.wispforest.accessories.pond.CosmeticArmorLookupTogglable;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {
    @WrapMethod(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;")
    private static EntityRenderState accessories$wrapWithArmorLookup(LivingEntity livingEntity, Operation<EntityRenderState> original) {
        var state = new EntityRenderState[1];

        CosmeticArmorLookupTogglable.runWithLookupToggle(livingEntity, () -> state[0] = original.call(livingEntity));

        return state[0];
    }
}
