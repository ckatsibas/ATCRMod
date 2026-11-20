package com.eabkystudios.atcrmod.block.entity.block;

import com.eabkystudios.atcrmod.block.custom.RedMetalCableBlock;
import com.eabkystudios.atcrmod.block.entity.ATCRBlockEntities;
import com.eabkystudios.atcrmod.block.resources.RedEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class RedMetalCableBlockEntity extends BlockEntity {
    protected int capacity;
    protected final RedEnergy energy;
    public static final int MAXTRANSFER = 100;
    private static final int CAPACITY = RedMetalCableBlock.CAPACITY;
    private static final int RECEIVE = DenseRedBlockEntity.GENERATION_RATE;
    private static final int EXTRACT = 0;

    public RedMetalCableBlockEntity(BlockPos pos, BlockState state) {
        super(ATCRBlockEntities.RED_METAL_CABLE_BE.get(), pos, state);

        energy = new RedEnergy(CAPACITY, RECEIVE, EXTRACT);
        this.capacity = CAPACITY;
    }

    public RedMetalCableBlockEntity(BlockPos pos, BlockState state, int capacity, int receive, int extract) {
        super(ATCRBlockEntities.RED_METAL_CABLE_BE.get(), pos, state);

        this.energy = new RedEnergy(capacity, receive, extract);
        this.capacity = capacity;
    }

    public void setEnergy(int amount) {
        energy.set(Math.clamp(amount, 0, capacity));
    }

    // Cached outputs
    private Set<BlockPos> outputs = null;

    // This function will cache all outputs for this cable network. It will do this
    // by traversing all cables connected to this cable and then check for all energy
    // receivers around those cables.
    private void checkOutputs() {
        if (outputs == null) {
            outputs = new HashSet<>();
            traverse(worldPosition, cable -> {
                // Check for all energy receivers around this position (ignore cables)
                for (Direction direction : Direction.values()) {
                    BlockPos p = cable.getBlockPos().relative(direction);
                    BlockEntity te = level.getBlockEntity(p);
                    if (te != null && !(te instanceof RedMetalCableBlockEntity)) {
                        var handler = level.getCapability(Capabilities.Energy.BLOCK, p, null);
                        if (handler != null) {
                            outputs.add(p);
                        }
                    }
                }
            });
        }
    }

    public void markDirty() {
        traverse(worldPosition, cable -> cable.outputs = null);
    }

    // This is a generic function that will traverse all cables connected to this cable
    // and call the given consumer for each cable.
    private void traverse(BlockPos pos, Consumer<RedMetalCableBlockEntity> consumer) {
        Set<BlockPos> traversed = new HashSet<>();
        traversed.add(pos);
        consumer.accept(this);
        traverse(pos, traversed, consumer);
    }

    private void traverse(BlockPos pos, Set<BlockPos> traversed, Consumer<RedMetalCableBlockEntity> consumer) {
        for (Direction direction : Direction.values()) {
            BlockPos p = pos.relative(direction);
            if (!traversed.contains(p)) {
                traversed.add(p);
                if (level.getBlockEntity(p) instanceof RedMetalCableBlockEntity cable) {
                    consumer.accept(cable);
                    cable.traverse(p, traversed, consumer);
                }
            }
        }
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (energy.getEnergyStored() > 0) {
            // Only do something if we have energy
            checkOutputs();
            if (!outputs.isEmpty()) {

                int share = Math.max(1, energy.getEnergyStored() / outputs.size());

                for (BlockPos p : outputs) {
                    var handler = level.getCapability(Capabilities.Energy.BLOCK, p, null);
                    if (handler == null) continue;

                    int offered = energy.extractEnergy(Math.min(share, MAXTRANSFER), true);
                    if (offered <= 0) continue;

                    int accepted;
                    try (var transaction = net.neoforged.neoforge.transfer.transaction.Transaction.openRoot()) {
                        accepted = handler.insert(offered, transaction);
                        transaction.commit();
                    }

                    energy.extractEnergy(accepted, false);
                }
            }
        }
    }

    public EnergyHandler getEnergyHandler() {
        return energy;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("energy", energy.getEnergyStored());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        setEnergy(input.getIntOr("energy", 0));
    }
}
