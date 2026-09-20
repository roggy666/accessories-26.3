package io.wispforest.accessories.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.wispforest.accessories.utils.JsonUtils;
import io.wispforest.accessories.pond.ReplaceableJsonResourceReloadListener;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(SimpleJsonResourceReloadListener.class)
public abstract class SimpleJsonResourceReloadListenerMixin implements ReplaceableJsonResourceReloadListener {
    @Unique
    private boolean allowReplacementLoading = false;

    @Override
    public void accessories$allowReplacementLoading(boolean value) {
        this.allowReplacementLoading = value;
    }

    @Override
    public boolean accessories$allowReplacementLoading() {
        return this.allowReplacementLoading;
    }

    // Since 26.3 the directory scan is inlined into prepare(), so the listener instance
    // (and therefore the replacement flag) is available right where the resources are listed
    @WrapOperation(
        method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/FileToIdConverter;listMatchingResources(Lnet/minecraft/server/packs/resources/ResourceManager;)Ljava/util/Map;")
    )
    private Map<Identifier, Resource> listReplacedResources(FileToIdConverter instance, ResourceManager resourceManager, Operation<Map<Identifier, Resource>> original) {
        if (this.allowReplacementLoading) {
            return JsonUtils.scanDirectoryWithReplace(resourceManager, instance);
        }

        return original.call(instance, resourceManager);
    }
}
