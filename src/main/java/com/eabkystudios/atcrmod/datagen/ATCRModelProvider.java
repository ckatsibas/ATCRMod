package com.eabkystudios.atcrmod.datagen;

import com.eabkystudios.atcrmod.AgainstTheCreepingRot;
import com.eabkystudios.atcrmod.block.ATCRBlocks;
import com.eabkystudios.atcrmod.item.ATCRItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class ATCRModelProvider extends ModelProvider {
    public ATCRModelProvider(PackOutput output, String modId) {
        super(output, modId);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        /*ITEMS*/
        itemModels.generateFlatItem(ATCRItems.STEEL_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.SILVER_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.RAW_SILVER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.LOTUS_FLOWER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.LEATHER_WRAPPED_HANDLE.get(), ModelTemplates.FLAT_ITEM);
        generateGuiOverrideItem(itemModels, ATCRItems.SILVER_SWORD.get(),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/silver_sword_2d"),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/silver_sword_3d"));
        generateGuiOverrideItem(itemModels, ATCRItems.STEEL_SWORD.get(),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/steel_sword_2d"),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/steel_sword_3d"));
        generateGuiOverrideItem(itemModels, ATCRItems.STEEL_PICKAXE.get(),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/steel_pickaxe_2d"),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/steel_pickaxe_3d"));
        generateGuiOverrideItem(itemModels, ATCRItems.STEEL_AXE.get(),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/steel_axe_2d"),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/steel_axe_3d"));
        generateGuiOverrideItem(itemModels, ATCRItems.STEEL_SHOVEL.get(),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/steel_shovel_2d"),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/steel_shovel_3d"));
        itemModels.declareCustomModelItem(ATCRItems.TRAVELER_SWORD.get());
        itemModels.declareCustomModelItem(ATCRItems.TRAVELER_DAGGER.get());
        // itemModels.declareCustomModelItem(ATCRItems.TRAVELER_SHIELD.get());
        itemModels.generateFlatItem(ATCRItems.RED_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.BATTERY_BASIC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.LOST_TRAVELER_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.DEER_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.VITALITY_RING.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.GOLD_RING.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.AMULET_OF_THE_DEATHLESS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.RED_METAL_INGOT.get(), ModelTemplates.FLAT_ITEM);
        generateGuiOverrideItem(itemModels, ATCRItems.RUINED_IRON_RITUAL_DAGGER.get(),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/ruined_iron_ritual_dagger_2d"),
                Identifier.fromNamespaceAndPath(AgainstTheCreepingRot.MODID, "item/ruined_iron_ritual_dagger_3d"));
        itemModels.generateFlatItem(ATCRItems.HEART_OF_RUINOUS_ROT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.STEEL_ARMOR_HELMET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.STEEL_ARMOR_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.STEEL_ARMOR_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.STEEL_ARMOR_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.SILVER_ARMOR_HELMET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.SILVER_ARMOR_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.SILVER_ARMOR_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ATCRItems.SILVER_ARMOR_BOOTS.get(), ModelTemplates.FLAT_ITEM);

        /*BLOCKS*/
        blockModels.createTrivialCube(ATCRBlocks.STEEL_BLOCK.get());
        blockModels.createTrivialCube(ATCRBlocks.SILVER_ORE.get());
        blockModels.createTrivialCube(ATCRBlocks.SILVER_DEEPSLATE_ORE.get());
        blockModels.family(ATCRBlocks.GRASSY_COBBLESTONE.get()).stairs(ATCRBlocks.GRASSY_COBBLESTONE_STAIRS.get()).slab(ATCRBlocks.GRASSY_COBBLESTONE_SLAB.get()).wall(ATCRBlocks.GRASSY_COBBLESTONE_WALL.get());
        createPoweredBlock(blockModels, ATCRBlocks.POWERED_FURNACE.get(), TexturedModel.ORIENTABLE, blockModels.modelOutput);
        blockModels.createTrivialCube(ATCRBlocks.CLUSTER_OF_RED.get());
        blockModels.createTrivialCube(ATCRBlocks.DENSE_BLOCK_OF_RED.get());
        blockModels.family(ATCRBlocks.RUINED_STONE.get()).stairs(ATCRBlocks.RUINED_STONE_STAIRS.get()).slab(ATCRBlocks.RUINED_STONE_SLAB.get()).wall(ATCRBlocks.RUINED_STONE_WALL.get());
        blockModels.family(ATCRBlocks.RUINED_COBBLESTONE.get()).stairs(ATCRBlocks.RUINED_COBBLESTONE_STAIRS.get()).slab(ATCRBlocks.RUINED_COBBLESTONE_SLAB.get()).wall(ATCRBlocks.RUINED_COBBLESTONE_WALL.get());
        blockModels.family(ATCRBlocks.RUINED_MOSSY_COBBLESTONE.get()).stairs(ATCRBlocks.RUINED_MOSSY_COBBLESTONE_STAIRS.get()).slab(ATCRBlocks.RUINED_MOSSY_COBBLESTONE_SLAB.get()).wall(ATCRBlocks.RUINED_MOSSY_COBBLESTONE_WALL.get());
        blockModels.family(ATCRBlocks.RUINED_STONE_BRICKS.get()).stairs(ATCRBlocks.RUINED_STONE_BRICKS_STAIRS.get()).slab(ATCRBlocks.RUINED_STONE_BRICKS_SLAB.get()).wall(ATCRBlocks.RUINED_STONE_BRICKS_WALL.get());
        blockModels.family(ATCRBlocks.RUINED_MOSSY_STONE_BRICKS.get()).stairs(ATCRBlocks.RUINED_MOSSY_STONE_BRICKS_STAIRS.get()).slab(ATCRBlocks.RUINED_MOSSY_STONE_BRICKS_SLAB.get()).wall(ATCRBlocks.RUINED_MOSSY_STONE_BRICKS_WALL.get());
        blockModels.family(ATCRBlocks.RUINED_CRACKED_STONE_BRICKS.get()).stairs(ATCRBlocks.RUINED_CRACKED_STONE_BRICKS_STAIRS.get()).slab(ATCRBlocks.RUINED_CRACKED_STONE_BRICKS_SLAB.get()).wall(ATCRBlocks.RUINED_CRACKED_STONE_BRICKS_WALL.get());
        blockModels.createTrivialCube(ATCRBlocks.RUINED_CHISELED_STONE_BRICKS.get());
        blockModels.createTrivialCube(ATCRBlocks.RED_METAL_CABLE.get());
    }

    /*METHODS*/
    public void createPoweredBlock(BlockModelGenerators blockModels, Block poweredBlock, TexturedModel.Provider provider, BiConsumer<Identifier, ModelInstance> modelOutput) {
        MultiVariant normalModel = plainVariant(provider.create(poweredBlock, modelOutput));
        Material frontTexture = TextureMapping.getBlockTexture(poweredBlock, "_front_on");
        Material topTexture = TextureMapping.getBlockTexture(poweredBlock, "_top_on");
        MultiVariant litModel = plainVariant(provider.get(poweredBlock).updateTextures(textures ->
                textures.put(TextureSlot.FRONT, frontTexture)).updateTextures(textures -> textures.put(TextureSlot.TOP, topTexture)).createWithSuffix(poweredBlock, "_on", modelOutput));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(poweredBlock).with(createBooleanModelDispatch(BlockStateProperties.LIT, litModel, normalModel)).with(ROTATION_HORIZONTAL_FACING));
    }

    /**
     * Creates a model definition that uses one model in the GUI and another in all other display contexts.
     *
     * @param itemModels the item model generator
     * @param item the item to configure
     * @param guiModelId the model to use in the GUI
     * @param fallbackModelId the model to use in other contexts
     */
    private void generateGuiOverrideItem(ItemModelGenerators itemModels, Item item, Identifier guiModelId, Identifier fallbackModelId) {
        // Wrap each model ID in the format expected by the item model system.
        var guiModel = new CuboidItemModelWrapper.Unbaked(guiModelId, Optional.empty(), List.of());
        var fallbackModel = new CuboidItemModelWrapper.Unbaked(fallbackModelId, Optional.empty(), List.of());

        // Use guiModel whenever the item is shown in the GUI.
        var guiCase = new SelectItemModel.SwitchCase<ItemDisplayContext>(List.of(ItemDisplayContext.GUI), guiModel);

        // Check the display context and use fallbackModel if no case matches.
        var displayContextSwitch = new SelectItemModel.UnbakedSwitch<DisplayContext, ItemDisplayContext>(new DisplayContext(), List.of(guiCase));

        itemModels.itemModelOutput.accept(item, new SelectItemModel.Unbaked(Optional.empty(), displayContextSwitch, Optional.of(fallbackModel)));
    }
}
