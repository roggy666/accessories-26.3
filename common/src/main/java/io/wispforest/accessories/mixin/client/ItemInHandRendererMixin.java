package io.wispforest.accessories.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.wispforest.accessories.client.AccessoriesClient;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//@Environment(EnvType.CLIENT)
// the two-handed map arm rendering moved to the first-person renderer in 26.3 and reads the avatar render state
@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class ItemInHandRendererMixin {
    @ModifyExpressionValue(method = "renderTwoHandedMap", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;isInvisible:Z", opcode = Opcodes.GETFIELD))
    private boolean accessories$overrideFirstPersonInvisibility(boolean original) {
        if (original) AccessoriesClient.IS_PLAYER_INVISIBLE = true;
        return false;
    }
}