package com.eabkystudios.atcrmod.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.List;

public record DeathlessAmuletProtection(List<ConsumeEffect> deathlessAmuletEffects) {
    public static final Codec<DeathlessAmuletProtection> CODEC = RecordCodecBuilder.create(i -> i.group(ConsumeEffect.CODEC.listOf().optionalFieldOf("deathless_amulet_effects", List.of()).forGetter(DeathlessAmuletProtection::deathlessAmuletEffects)).apply(i, DeathlessAmuletProtection::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, DeathlessAmuletProtection> STREAM_CODEC = StreamCodec.composite(ConsumeEffect.STREAM_CODEC.apply(ByteBufCodecs.list()), DeathlessAmuletProtection::deathlessAmuletEffects, DeathlessAmuletProtection::new);

    public static final DeathlessAmuletProtection AMULET_OF_THE_DEATHLESS = new DeathlessAmuletProtection(
            List.of(
                    new ClearAllStatusEffectsConsumeEffect(),
                    new ApplyStatusEffectsConsumeEffect(
                            List.of(
                                    new MobEffectInstance(MobEffects.REGENERATION, 900, 1),
                                    new MobEffectInstance(MobEffects.ABSORPTION, 100, 1),
                                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0)
                            )
                    )
            )
    );

    public void applyEffects(ItemStack itemStack, LivingEntity entity) {
        for (ConsumeEffect effect : this.deathlessAmuletEffects) {
            effect.apply(entity.level(), itemStack, entity);
        }
    }
}
