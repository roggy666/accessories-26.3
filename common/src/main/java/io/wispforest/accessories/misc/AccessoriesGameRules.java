package io.wispforest.accessories.misc;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import io.wispforest.accessories.Accessories;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class AccessoriesGameRules {

    public static final GameRule<Boolean> RULE_KEEP_ACCESSORY_INVENTORY = registerBoolean("keep_accessory_inventory", GameRuleCategory.PLAYER, false);

    // Game rules are a proper registry now, so this mirrors the private GameRules#registerBoolean under our namespace
    public static GameRule<Boolean> registerBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        var rule = new GameRule<>(
                category,
                GameRuleType.BOOL,
                BoolArgumentType.bool(),
                GameRuleTypeVisitor::visitBoolean,
                Codec.BOOL,
                value -> value ? 1 : 0,
                defaultValue,
                FeatureFlags.VANILLA_SET
        );

        return Registry.register(BuiltInRegistries.GAME_RULE, Accessories.of(name), rule);
    }
}
