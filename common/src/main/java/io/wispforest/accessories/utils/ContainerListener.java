package io.wispforest.accessories.utils;

import net.minecraft.world.Container;

/**
 * Replacement for the removed vanilla {@code net.minecraft.world.ContainerListener}
 */
@FunctionalInterface
public interface ContainerListener {
    void containerChanged(Container container);
}
