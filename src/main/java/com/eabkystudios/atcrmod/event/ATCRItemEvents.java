package com.eabkystudios.atcrmod.event;

import com.eabkystudios.atcrmod.AgainstTheCreepingRot;
import com.eabkystudios.atcrmod.data.ATCRDataComponents;
import com.eabkystudios.atcrmod.data.custom.DeathlessAmuletProtection;
import com.eabkystudios.atcrmod.entity.ATCRAttributes;
import com.eabkystudios.atcrmod.entity.ATCREntities;
import com.eabkystudios.atcrmod.item.ATCRItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.concurrent.atomic.AtomicReference;

@EventBusSubscriber(modid = AgainstTheCreepingRot.MODID)
public class ATCRItemEvents {
    private static final Logger log = LoggerFactory.getLogger(ATCRItemEvents.class);

    @SubscribeEvent
    public static void amuletOfTheDeathlessEvent(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        AtomicReference<DeathlessAmuletProtection> protection = new AtomicReference<>();

        CuriosApi.getCuriosInventory(entity).ifPresent(inventory -> {
            for (ICurioStacksHandler slot : inventory.getCurios().values()) {
                var stacks = slot.getStacks();

                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack amulet = stacks.getStackInSlot(i);
                    protection.set(amulet.get(ATCRDataComponents.DEATHLESS_AMULET_PROTECTION));

                    if (!amulet.is(ATCRItems.AMULET_OF_THE_DEATHLESS.get())) {
                        continue;
                    }

                    int cooldown = amulet.getOrDefault(ATCRDataComponents.DEATHLESS_AMULET_COOLDOWN.get(), 0);
                    if (cooldown > 0) {
                        continue;
                    }

                    event.setCanceled(true);
                    entity.setHealth(1.0F);
                    protection.get().applyEffects(amulet, entity);
                    amulet.set(ATCRDataComponents.DEATHLESS_AMULET_COOLDOWN.get(), 6000);

                    return;
                }
            }
        });
    }

    @SubscribeEvent
    public static void silverArmorPlayerHitPre(LivingDamageEvent.Pre event){
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();

        if (!(entity instanceof Player player)) {
            return;
        }

        var aura = player.getAttribute(ATCRAttributes.HOLY_AURA);
        if (aura == null) {
            return;
        }

        double auraValue = aura.getValue();
        if (auraValue >= 10f && damageSource.getEntity().is(EntityTypeTags.UNDEAD)){
            event.setNewDamage((float) (event.getOriginalDamage() * (1 - auraValue/100)));
        }
    }

    @SubscribeEvent
    public static void silverArmorPlayerHitPost(LivingDamageEvent.Post event){
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = entity.getLastDamageSource();


        if (!(entity instanceof Player player)) {
            return;
        }

        var aura = player.getAttribute(ATCRAttributes.HOLY_AURA);
        if (aura == null) {
            return;
        }

        double auraValue = aura.getValue();
        if (auraValue >= 10f && damageSource.getEntity().is(EntityTypeTags.UNDEAD)){
                damageSource.getEntity().asLivingEntity().knockback(auraValue/10, 2, 2);
            damageSource.getEntity().asLivingEntity().hurtServer((ServerLevel) entity.level(), entity.damageSources().magic(), (float) (1 + auraValue/10));
        }
    }
}
