package com.eabkystudios.atcrmod.item.client;

import com.eabkystudios.atcrmod.AgainstTheCreepingRot;
import com.eabkystudios.atcrmod.item.ATCRItems;
import com.eabkystudios.atcrmod.item.custom.SilverArmorItem;
import com.geckolib.model.DefaultedItemGeoModel;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;

import java.util.Set;

public class SilverArmorRenderer <R extends HumanoidRenderState & GeoRenderState> extends GeoArmorRenderer<SilverArmorItem, R> {
    public SilverArmorRenderer() {
        super(new DefaultedItemGeoModel<>(Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "armor/silver_armor")));
    }

    // Capture the worn stack data for later
    @Override
    public void addRenderData(SilverArmorItem animatable, RenderData relatedObject, R renderState, float partialTick) {
        Set<Item> wornArmor = new ObjectOpenHashSet<>(4);

        boolean fullSetEffect = false;

        if (!(relatedObject.entity() instanceof ArmorStand)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)
                    wornArmor.add(relatedObject.entity().getItemBySlot(slot).getItem());
            }

            fullSetEffect = wornArmor.containsAll(ObjectArrayList.of(
                    ATCRItems.SILVER_ARMOR_HELMET.get(),
                    ATCRItems.SILVER_ARMOR_CHESTPLATE.get(),
                    ATCRItems.SILVER_ARMOR_LEGGINGS.get(),
                    ATCRItems.SILVER_ARMOR_BOOTS.get()));
        }

        renderState.addGeckolibData(SilverArmorItem.HAS_FULL_SET_EFFECT, fullSetEffect);
    }
}
