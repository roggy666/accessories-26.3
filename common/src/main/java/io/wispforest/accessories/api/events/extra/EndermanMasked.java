package io.wispforest.accessories.api.events.extra;

import io.wispforest.accessories.api.slot.SlotReference;
import io.wispforest.accessories.impl.event.WrappedEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.item.ItemStack;

/**
 * @deprecated Use {@link IsGazeDisguised#EVENT} and check for Enderman specifically
 */
@Deprecated(forRemoval = true)
public interface EndermanMasked {

    Event<EndermanMasked> EVENT = new WrappedEvent<>(IsGazeDisguised.EVENT, endermanMasked -> {
        return (lookingEntity, stack, reference) -> {
            if (!(lookingEntity instanceof Enderman enderMan)) return TriState.DEFAULT;

            return endermanMasked.isEndermanMasked(enderMan, stack, reference);
        };
    }, gazeEvent -> (enderMan, stack, reference) -> gazeEvent.invoker().isWearDisguise(enderMan, stack, reference));

    /**
     * @param enderMan  The specific {@link Enderman} for the given check
     * @param stack     The specific stack being evaluated
     * @param reference The reference to the specific location within the Accessories Inventory
     * @return If the given enderman sees a mask on the given passed referenced entity
     */
    TriState isEndermanMasked(Enderman enderMan, ItemStack stack, SlotReference reference);
}
