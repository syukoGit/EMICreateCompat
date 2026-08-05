package fr.syuko.emicreatecompat.category.sequencedassembly;

import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record AssemblyStep(StepKind kind, Ingredient heldItem, List<FluidStack> fluids, boolean keepHeldItem) {

    public static final int WIDTH = 25;

    public static final int MARGIN = 3;

    public enum StepKind {
        PRESSING,
        CUTTING,
        DEPLOYING,
        SPOUTING
    }
}
