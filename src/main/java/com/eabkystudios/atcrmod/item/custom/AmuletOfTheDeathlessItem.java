package com.eabkystudios.atcrmod.item.custom;

import com.eabkystudios.atcrmod.data.ATCRDataComponents;
import jdk.jfr.Event;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class AmuletOfTheDeathlessItem extends Item implements ICurioItem {
    public AmuletOfTheDeathlessItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().level().isClientSide()) {
            return;
        }

        if (stack.getOrDefault(ATCRDataComponents.DEATHLESS_AMULET_COOLDOWN.get(), 0) > 0) {
            stack.set(ATCRDataComponents.DEATHLESS_AMULET_COOLDOWN.get(), stack.getOrDefault(ATCRDataComponents.DEATHLESS_AMULET_COOLDOWN.get(), 0) - 1);
        }
    }
}
