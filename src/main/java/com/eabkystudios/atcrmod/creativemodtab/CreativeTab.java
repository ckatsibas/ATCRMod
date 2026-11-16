package com.eabkystudios.atcrmod.creativemodtab;

import com.eabkystudios.atcrmod.AgainstTheCreepingRot;
import com.eabkystudios.atcrmod.block.ATCRBlocks;
import com.eabkystudios.atcrmod.item.ATCRItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Supplier;

public class CreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AgainstTheCreepingRot.MODID);

    //TODO: make method that adds all current and new items and blocks
    public static final Supplier<CreativeModeTab> ATCR_TAB = CREATIVE_MODE_TABS.register("actr_all_tab",
            ()-> CreativeModeTab.builder().icon(()->new ItemStack(ATCRItems.RED_SHARD.get()))
                    .title(Component.translatable("creativetab.atcrmod.actr_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.acceptAll(allModItems());
                        //output.acceptAll(ATCRBlocks.allModBlocks());
                    })
                    .build());

    private static Collection<ItemStack> allModItems() {
        Collection<ItemStack> items = new ArrayList<>();
        ATCRItems.ITEMS.getEntries().forEach(holder -> {
            Item item = holder.get();
            items.add(item.getDefaultInstance());
        });

        return items;
    }

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
