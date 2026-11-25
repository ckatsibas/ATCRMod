package com.eabkystudios.atcrmod.block.entity.block;

import com.eabkystudios.atcrmod.block.custom.RedMetalCableBlock;
import com.eabkystudios.atcrmod.block.entity.ATCRBlockEntities;
import com.eabkystudios.atcrmod.block.resources.RedEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RedMetalCableBlockEntity extends BlockEntity {
    public static final int MAXTRANSFER = 100;
    private static final int CAPACITY = RedMetalCableBlock.CAPACITY;
    private static final int RECEIVE = DenseRedBlockEntity.GENERATION_RATE;
    private static final int EXTRACT = MAXTRANSFER;
    protected int capacity;
    protected final RedEnergy energy;
    private Map<BlockPos, Direction> outputs; // Maps each target block to the side of that block facing the cable.

    public RedMetalCableBlockEntity(BlockPos pos, BlockState state) {
        super(ATCRBlockEntities.RED_METAL_CABLE_BE.get(), pos, state);

        this.energy = new RedEnergy(CAPACITY, RECEIVE, EXTRACT);
        this.capacity = CAPACITY;
    }

    public void setEnergy(int amount) {
        energy.set(Math.clamp(amount, 0, capacity));
    }

    public void markDirty() {
        outputs = null;
        setChanged();
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide() || energy.getEnergyStored() <= 0) {
            return;
        }

        checkOutputs(level);

        for (Map.Entry<BlockPos, Direction> output : outputs.entrySet()) {
            if (energy.getEnergyStored() <= 0) {
                break;
            }

            BlockPos targetPos = output.getKey();

            // Avoid immediately pushing energy back and forth between cables.
            if (level.getBlockEntity(targetPos) instanceof RedMetalCableBlockEntity targetCable && targetCable.energy.getEnergyStored() >= energy.getEnergyStored()) {
                continue;
            }

            var handler = level.getCapability(Capabilities.Energy.BLOCK, targetPos, output.getValue());
            if (handler == null) {
                continue;
            }

            int offered = energy.extractEnergy(MAXTRANSFER, true);
            if (offered <= 0) {
                continue;
            }

            int accepted;

            try (Transaction transaction = Transaction.openRoot()) {
                accepted = handler.insert(offered, transaction);
                transaction.commit();
            }

            if (accepted > 0) {
                energy.extractEnergy(accepted, false);
            }
        }
    }

    public EnergyHandler getEnergyHandler() {
        return energy;
    }

    private void checkOutputs(Level level) {
        if (outputs != null) {
            return;
        }

        outputs = new HashMap<>();

        for (Direction direction : Direction.values()) {
            BlockPos targetPos = worldPosition.relative(direction);
            Direction targetSide = direction.getOpposite();

            var handler = level.getCapability(Capabilities.Energy.BLOCK, targetPos, targetSide);

            if (handler != null) {
                outputs.put(targetPos, targetSide);
            }
        }
    }

    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("energy", energy.getEnergyStored());
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        setEnergy(input.getIntOr("energy", 0));
    }
}
