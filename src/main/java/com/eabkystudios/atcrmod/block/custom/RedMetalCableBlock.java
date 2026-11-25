package com.eabkystudios.atcrmod.block.custom;

import com.eabkystudios.atcrmod.block.ATCRBlocks;
import com.eabkystudios.atcrmod.block.entity.block.RedMetalCableBlockEntity;
import com.eabkystudios.atcrmod.data.enums.ConnectorType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jspecify.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class RedMetalCableBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    public static final EnumProperty<ConnectorType> NORTH = EnumProperty.create("north", ConnectorType.class);
    public static final EnumProperty<ConnectorType> SOUTH = EnumProperty.create("south", ConnectorType.class);
    public static final EnumProperty<ConnectorType> EAST = EnumProperty.create("east", ConnectorType.class);
    public static final EnumProperty<ConnectorType> WEST = EnumProperty.create("west", ConnectorType.class);
    public static final EnumProperty<ConnectorType> UP = EnumProperty.create("up", ConnectorType.class);
    public static final EnumProperty<ConnectorType> DOWN = EnumProperty.create("down", ConnectorType.class);
    public static final int CAPACITY = 1000;

    private static final VoxelShape CORE = Shapes.box(0.375, 0.375, 0.375, 0.625, 0.625, 0.625);
    private static final VoxelShape ARM_NORTH = Shapes.box(0.375, 0.375, 0.0, 0.625, 0.625, 0.5);
    private static final VoxelShape ARM_SOUTH = Shapes.box(0.375, 0.375, 0.5, 0.625, 0.625, 1.0);
    private static final VoxelShape ARM_WEST = Shapes.box(0.0, 0.375, 0.375, 0.5, 0.625, 0.625);
    private static final VoxelShape ARM_EAST = Shapes.box(0.5, 0.375, 0.375, 1.0, 0.625, 0.625);
    private static final VoxelShape ARM_DOWN = Shapes.box(0.375, 0.0, 0.375, 0.625, 0.5, 0.625);
    private static final VoxelShape ARM_UP = Shapes.box(0.375, 0.5, 0.375, 0.625, 1.0, 0.625);

    public RedMetalCableBlock(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState()
                .setValue(WATERLOGGED, false)
                .setValue(NORTH, ConnectorType.NONE)
                .setValue(SOUTH, ConnectorType.NONE)
                .setValue(EAST, ConnectorType.NONE)
                .setValue(WEST, ConnectorType.NONE)
                .setValue(UP, ConnectorType.NONE)
                .setValue(DOWN, ConnectorType.NONE));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RedMetalCableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (level.isClientSide()) {
            return null;
        }

        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof RedMetalCableBlockEntity cable) {
                cable.tick(tickLevel, pos, tickLevel.getBlockState(pos));
            }
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockState state = updateConnectionProperties(
                level, pos, defaultBlockState()
        );

        return state.setValue(
                WATERLOGGED,
                level.getFluidState(pos).getType() == Fluids.WATER
        );
    }

    private static EnumProperty<ConnectorType> propertyFor(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }

    //TODO: make it so it returns enum type based on data component of the block not had coded blockstates
    private static ConnectorType getConnectorType(
            Level level,
            BlockPos neighborPos,
            Direction sideOfNeighbor
    ) {
        BlockState neighborState = level.getBlockState(neighborPos);

        if (neighborState.getBlock() instanceof RedMetalCableBlock) {
            return ConnectorType.CABLE;
        }

        if (neighborState.is(ATCRBlocks.DENSE_BLOCK_OF_RED.get())) {
            return ConnectorType.GENERATOR;
        }

        if (neighborState.is(ATCRBlocks.POWERED_FURNACE.get())) {
            return ConnectorType.MACHINE;
        }

        // Optional: recognize any block that exposes an energy capability.
        if (level.getCapability(
                Capabilities.Energy.BLOCK,
                neighborPos,
                sideOfNeighbor
        ) != null) {
            return ConnectorType.MACHINE;
        }

        return ConnectorType.NONE;
    }


    private static BlockState updateConnectionProperties(
            Level level,
            BlockPos pos,
            BlockState state
    ) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);

            ConnectorType connectorType = getConnectorType(
                    level,
                    neighborPos,
                    direction.getOpposite()
            );

            state = state.setValue(
                    propertyFor(direction),
                    connectorType
            );
        }

        return state;
    }


    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);

        if (!level.isClientSide()) {
            BlockState updated = updateConnectionProperties(level, pos, state);

            if (!updated.equals(state)) {
                level.setBlockAndUpdate(pos, updated);
            }

            if (level.getBlockEntity(pos) instanceof RedMetalCableBlockEntity cable) {
                cable.markDirty();
            }
        }
    }

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        VoxelShape shape = CORE;

        if (state.getValue(NORTH) != ConnectorType.NONE)
            shape = Shapes.join(shape, ARM_NORTH, BooleanOp.OR);
        if (state.getValue(SOUTH) != ConnectorType.NONE)
            shape = Shapes.join(shape, ARM_SOUTH, BooleanOp.OR);
        if (state.getValue(WEST) != ConnectorType.NONE)
            shape = Shapes.join(shape, ARM_WEST, BooleanOp.OR);
        if (state.getValue(EAST) != ConnectorType.NONE)
            shape = Shapes.join(shape, ARM_EAST, BooleanOp.OR);
        if (state.getValue(UP) != ConnectorType.NONE)
            shape = Shapes.join(shape, ARM_UP, BooleanOp.OR);
        if (state.getValue(DOWN) != ConnectorType.NONE)
            shape = Shapes.join(shape, ARM_DOWN, BooleanOp.OR);

        return shape;
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }
}
