package io.wispforest.accessories.mixin;

import net.minecraft.util.Prediction;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.pond.DroppedStacksExtension;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    @Accessor("player")
    public abstract Player accessories$player();

    // 26.3 passes an explicit countingOnly flag and only evaluates the carried stack when clearing,
    // so the accessory containers are folded into the result instead of a local counter
    @ModifyReturnValue(method = "clearOrCountMatchingItems", at = @At("RETURN"))
    private int clearAccessories(int count, Predicate<ItemStack> stackPredicate, boolean countingOnly, int maxCount, Container inventory) {
        var capability = AccessoriesCapability.get(accessories$player());

        if(capability == null) return count;

        var total = new int[]{count};

        capability.getContainers().forEach((s, container) -> {
            var accessories = container.getAccessories();
            total[0] += ContainerHelper.clearOrCountMatchingItems(accessories, stackPredicate, maxCount - total[0], countingOnly);

            var cosmetics = container.getCosmeticAccessories();
            total[0] += ContainerHelper.clearOrCountMatchingItems(cosmetics, stackPredicate, maxCount - total[0], countingOnly);
        });

        return total[0];
    }

    @ModifyReturnValue(method = "contains(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("TAIL"))
    private boolean extendContainsCheck(boolean original, @Local(argsOnly = true) ItemStack stack) {
        return original || checkAccessoriesContainers(stack1 -> stack1.isEmpty() && ItemStack.isSameItemSameComponents(stack1, stack));
    }

    @ModifyReturnValue(method = "contains(Lnet/minecraft/tags/TagKey;)Z", at = @At("TAIL"))
    private boolean extendContainsCheck(boolean original, @Local(argsOnly = true) TagKey<Item> tag){
        return original || checkAccessoriesContainers(stack -> !stack.isEmpty() && stack.is(tag));
    }

    @ModifyReturnValue(method = "contains(Ljava/util/function/Predicate;)Z", at = @At("TAIL"))
    private boolean extendContainsCheck(boolean original, Predicate<ItemStack> predicate){
        return original || checkAccessoriesContainers(predicate);
    }

    @Unique
    private boolean checkAccessoriesContainers(Predicate<ItemStack> predicate){
        var capability = AccessoriesCapability.get(accessories$player());

        if(capability == null) return false;

        return capability.isEquipped(predicate);
    }

    @Inject(method = "dropAll", at = @At(value = "TAIL"))
    private void addAccessoriesToDropCall(CallbackInfo ci) {
        var player = accessories$player();
        var ext = ((DroppedStacksExtension) player);

        for (var itemstack : ext.toBeDroppedStacks()) {
            player.drop(itemstack, true, Prediction.SERVER_ONLY);
        }

        ext.addToBeDroppedStacks(List.of());
    }
}