package com.eabkystudios.atcrmod.item.custom;

import com.eabkystudios.atcrmod.entity.ATCRAttributes;
import com.eabkystudios.atcrmod.item.ATCRArmorMaterials;
import com.eabkystudios.atcrmod.item.ATCRItems;
import com.eabkystudios.atcrmod.item.client.SilverArmorRenderer;
import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.util.GeckoLibUtil;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

//TODO: absorb is probably temp effect or will change later
//TODO: makes immune to necromancer's aoe effect
public class SilverArmorItem extends Item implements GeoItem {
    private static final Logger log = LoggerFactory.getLogger(SilverArmorItem.class);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final DataTicket<Boolean> HAS_FULL_SET_EFFECT = DataTicket.create("atcrmod_silver_has_full_set_effect", Boolean.class);
    private static final Map<ArmorMaterial, List<MobEffectInstance>> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, List<MobEffectInstance>>()).put(ATCRArmorMaterials.SILVER_ARMOR_MATERIAL,
                    List.of(new MobEffectInstance(MobEffects.ABSORPTION, 200, 1, false, false))).build();
    private Identifier id = Identifier.fromNamespaceAndPath("atcrmod", "silver_armor_modifier");
    private final float holyAuraAmount = 10f;
    private AttributeModifier holyAuraModifier = new AttributeModifier(id, holyAuraAmount, AttributeModifier.Operation.ADD_VALUE);
    private AttributeInstance holyAuraInstance;

    public SilverArmorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @org.jspecify.annotations.Nullable EquipmentSlot slot) {
        if(owner instanceof Player player && !level.isClientSide() && hasFullSuitOfArmorOn(player)) {
            // evaluateArmorEffects(player);
            modifyHolyAura(player, hasFullSuitOfArmorOn(player));
        }
        else if(owner instanceof Player player && !level.isClientSide() && !hasFullSuitOfArmorOn(player)){
            modifyHolyAura(player, hasFullSuitOfArmorOn(player));
        }
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            // Defer creation of our renderer then cache it so that it doesn't get instantiated too early
            private final Supplier<SilverArmorRenderer<?>> renderer = Suppliers.memoize(SilverArmorRenderer::new);

            @Nullable
            @Override
            public GeoArmorRenderer<?, ?> getGeoArmorRenderer(ItemStack itemStack, EquipmentSlot equipmentSlot) {
                return this.renderer.get();
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private void evaluateArmorEffects(Player player) {
        for(Map.Entry<ArmorMaterial, List<MobEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            List<MobEffectInstance> mapEffect = entry.getValue();
            addEffectToPlayer(player, mapEffect);
        }
    }

    private void addEffectToPlayer(Player player, List<MobEffectInstance> mapEffect) {
        boolean hasPlayerEffect = mapEffect.stream().allMatch(effect -> player.hasEffect(effect.getEffect()));

        if(!hasPlayerEffect) {
            for (MobEffectInstance effect : mapEffect) {
                player.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
            }
        }
    }

    private void modifyHolyAura(Player player, Boolean hasfullSet){
        holyAuraInstance = player.getAttribute(ATCRAttributes.HOLY_AURA);
        if (holyAuraInstance!=null){
            if (hasfullSet){
                holyAuraInstance.addOrUpdateTransientModifier(holyAuraModifier);
            }
            else if(!hasfullSet){
                holyAuraInstance.removeModifier(holyAuraModifier);
            }
        }
    }

    private boolean hasFullSuitOfArmorOn(Player player) {
        Set<Item> wornArmor = new ObjectOpenHashSet<>(4);
        boolean hasfullSet = false;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR){
                wornArmor.add(player.getItemBySlot(slot).getItem());
            }

            hasfullSet = wornArmor.containsAll(ObjectArrayList.of(
                    ATCRItems.SILVER_ARMOR_HELMET.get(),
                    ATCRItems.SILVER_ARMOR_CHESTPLATE.get(),
                    ATCRItems.SILVER_ARMOR_LEGGINGS.get(),
                    ATCRItems.SILVER_ARMOR_BOOTS.get()));
        }

        return hasfullSet;
    }
}
