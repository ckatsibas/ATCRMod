package com.eabkystudios.atcrmod.data.enums;
import net.minecraft.util.StringRepresentable;
import javax.annotation.Nonnull;

public enum ConnectorType implements StringRepresentable {
    NONE,
    CABLE,
    MACHINE,
    GENERATOR;

    public static final ConnectorType[] VALUES = values();

    @Override
    @Nonnull
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
