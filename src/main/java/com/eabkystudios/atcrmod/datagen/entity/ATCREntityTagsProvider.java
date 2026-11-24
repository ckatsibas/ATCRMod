package com.eabkystudios.atcrmod.datagen.entity;

import com.eabkystudios.atcrmod.entity.ATCREntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class ATCREntityTagsProvider extends EntityTypeTagsProvider
{
    public ATCREntityTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, lookupProvider, modId);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(EntityTypeTags.UNDEAD)
                .add(ATCREntities.LOST_TRAVELER.get());
    }
}
