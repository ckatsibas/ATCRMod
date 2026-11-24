package com.eabkystudios.atcrmod.event;

import com.eabkystudios.atcrmod.AgainstTheCreepingRot;
import com.eabkystudios.atcrmod.block.entity.ATCRBlockEntities;
import com.eabkystudios.atcrmod.data.ATCRDataComponents;
import com.eabkystudios.atcrmod.data.custom.DeathlessAmuletProtection;
import com.eabkystudios.atcrmod.entity.ATCRAttributes;
import com.eabkystudios.atcrmod.entity.ATCREntities;
import com.eabkystudios.atcrmod.entity.custom.DeerEntity;
import com.eabkystudios.atcrmod.entity.custom.LostTravelerEntity;
import com.eabkystudios.atcrmod.item.ATCRItems;
import com.eabkystudios.atcrmod.networking.ClientPayloadHandler;
import com.eabkystudios.atcrmod.networking.packet.SummonEntityPacketC2S;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.concurrent.atomic.AtomicReference;

@EventBusSubscriber(modid = AgainstTheCreepingRot.MODID)
public class ATCRRegisterEvents {

    @SubscribeEvent
    public static void registerPayload(RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

        registrar.playToServer(SummonEntityPacketC2S.TYPE, SummonEntityPacketC2S.STREAM_CODEC, ClientPayloadHandler::handleSummonEntityPacket);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ATCREntities.LOST_TRAVELER.get(), LostTravelerEntity.createAttributes().build());
        event.put(ATCREntities.DEER.get(), DeerEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER, ATCRAttributes.HOLY_AURA)) {
            event.add(EntityType.PLAYER, ATCRAttributes.HOLY_AURA, 0.0);
        }
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ATCREntities.LOST_TRAVELER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(ATCREntities.DEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ATCRBlockEntities.POWERED_FURNACE_BE.get(), (furnace, side) -> furnace.getEnergyHandler());
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ATCRBlockEntities.RED_METAL_CABLE_BE.get(), (cable, side) -> cable.getEnergyHandler());
    }
}
