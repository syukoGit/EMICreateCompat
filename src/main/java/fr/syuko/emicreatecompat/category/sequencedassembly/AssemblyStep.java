package fr.syuko.emicreatecompat.category.sequencedassembly;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record AssemblyStep(StepKind kind, Ingredient heldItem, List<FluidStack> fluids, boolean keepHeldItem,
                           ItemStack machine) {

    public static final int WIDTH = 25;

    public static final int MARGIN = 3;

    public static AssemblyStep pressing() {
        return new AssemblyStep(StepKind.PRESSING, null, List.of(), false, ItemStack.EMPTY);
    }

    public static AssemblyStep cutting() {
        return new AssemblyStep(StepKind.CUTTING, null, List.of(), false, ItemStack.EMPTY);
    }

    public static AssemblyStep deploying(Ingredient heldItem, boolean keepHeldItem) {
        return new AssemblyStep(StepKind.DEPLOYING, heldItem, List.of(), keepHeldItem, ItemStack.EMPTY);
    }

    public static AssemblyStep spouting(List<FluidStack> fluids) {
        return new AssemblyStep(StepKind.SPOUTING, null, fluids, false, ItemStack.EMPTY);
    }

    public static AssemblyStep generic(Ingredient heldItem, List<FluidStack> fluids, ItemStack machine) {
        return new AssemblyStep(StepKind.GENERIC, heldItem, fluids, false, machine);
    }

    public enum StepKind {
        PRESSING,
        CUTTING,
        DEPLOYING,
        SPOUTING,
        GENERIC
    }
}
