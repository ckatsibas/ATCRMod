package com.eabkystudios.atcrmod.block.entity.block;

import com.eabkystudios.atcrmod.block.custom.AbstractPoweredBlock;
import com.eabkystudios.atcrmod.block.entity.ATCRBlockEntities;
import com.eabkystudios.atcrmod.menu.custom.PoweredFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class PoweredFurnaceEntity extends AbstractPoweredBlockEntity
{
    public static final int CAPACITY = 4000;
    private static final int MIN_ENERGY_TO_START = 400;
    private static final int ENERGY_PER_CRAFT = 400;
    public final EnergyHandler input = energy;
    private boolean previewReady;

    public PoweredFurnaceEntity(BlockPos worldPosition, BlockState blockState) {
        super(ATCRBlockEntities.POWERED_FURNACE_BE.get(), worldPosition, blockState, CAPACITY, DenseRedBlockEntity.GENERATION_RATE, 0);
    }

    public @NonNull Component getDisplayName() {
        return Component.translatable("block.atcrmod.powered_furnace");
    }

    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new PoweredFurnaceMenu(id, inventory, this, this.inventory, this.dataAccess);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {

        if (!(level instanceof ServerLevel server) || energy.getEnergyStored() < MIN_ENERGY_TO_START){
            return;
        }

        if(hasRecipe() && isOutputSlotEmptyOrReceivable()) {
            increaseCraftingProgress();
            setChanged(level, pos, state);
            level.setBlockAndUpdate(pos, state.setValue(AbstractPoweredBlock.LIT, true));

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
                energy.set(energy.getEnergyStored() - ENERGY_PER_CRAFT);
            }
        } else {
            resetProgress();
            level.setBlockAndUpdate(pos, state.setValue(AbstractPoweredBlock.LIT, false));
        }
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        previewReady = input.getBooleanOr("preview_ready", false);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
    }


    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("powered_furnace.progress", progress);
        output.putInt("powered_furnace.max_progress", maxProgress);
        output.putInt("energy", energy.getEnergyStored());

        output.putChild("inventory", inventory);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        progress = input.getIntOr("powered_furnace.progress", 0);
        maxProgress = input.getIntOr("powered_furnace.max_progress", 0);
        setEnergy(input.getIntOr("energy", 0));

        input.child("inventory").ifPresent(inventory::deserialize);
    }
}
