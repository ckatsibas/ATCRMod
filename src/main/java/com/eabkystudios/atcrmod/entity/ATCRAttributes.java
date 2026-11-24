package com.eabkystudios.atcrmod.entity;

import com.eabkystudios.atcrmod.AgainstTheCreepingRot;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ATCRAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, AgainstTheCreepingRot.MODID);

    public static final Holder<Attribute> HOLY_AURA = ATTRIBUTES.register("holy_aura", () -> new RangedAttribute("attributes.atcrmod.holy_aura", 0, -10000, 10000).setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

}
