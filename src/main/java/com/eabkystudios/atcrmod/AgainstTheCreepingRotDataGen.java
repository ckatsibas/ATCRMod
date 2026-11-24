package com.eabkystudios.atcrmod;

import com.eabkystudios.atcrmod.datagen.*;
import com.eabkystudios.atcrmod.datagen.block.ATCRBlockLootTableProvider;
import com.eabkystudios.atcrmod.datagen.block.ATCRBlockTagsProvider;
import com.eabkystudios.atcrmod.datagen.entity.ATCREntityLootTableProvider;
import com.eabkystudios.atcrmod.datagen.entity.ATCREntityTagsProvider;
import com.eabkystudios.atcrmod.datagen.item.ATCRCuriosProvider;
import com.eabkystudios.atcrmod.datagen.item.ATCREquipmentAssetProvider;
import com.eabkystudios.atcrmod.datagen.item.ATCRItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;

@EventBusSubscriber(modid = AgainstTheCreepingRot.MODID)
public class AgainstTheCreepingRotDataGen {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event){
        var lookupProvider = event.getLookupProvider();
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new ATCRModelProvider(packOutput, AgainstTheCreepingRot.MODID));
        generator.addProvider(true, new ATCRBlockTagsProvider(packOutput, lookupProvider, AgainstTheCreepingRot.MODID));
        generator.addProvider(true, new ATCRItemTagsProvider(packOutput, lookupProvider, AgainstTheCreepingRot.MODID));
        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(
                        new LootTableProvider.SubProviderEntry(ATCRBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(ATCREntityLootTableProvider::new, LootContextParamSets.ENTITY)
                ), lookupProvider));
        generator.addProvider(true, new ATCRRecipeProvider.Runner(packOutput, lookupProvider));
        generator.addProvider(true, new ATCRDatapackProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ATCRCuriosProvider(AgainstTheCreepingRot.MODID, packOutput, lookupProvider));
        generator.addProvider(true, new ATCREquipmentAssetProvider(packOutput));
        generator.addProvider(true, new ATCREntityTagsProvider(packOutput, lookupProvider, AgainstTheCreepingRot.MODID));
    }
}