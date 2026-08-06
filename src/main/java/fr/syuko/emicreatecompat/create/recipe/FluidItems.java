package fr.syuko.emicreatecompat.create.recipe;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class FluidItems {

    private static final int BUCKET = 1000;

    private static final List<Item> POTION_ITEMS = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION);

    private FluidItems() {
    }

    public static List<ItemStack> withFluidHandler() {
        List<ItemStack> stacks = new ArrayList<>();

        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack stack = new ItemStack(item);
            if (stack.getCapability(Capabilities.FluidHandler.ITEM) != null) {
                stacks.add(stack);
            }
        }

        return stacks;
    }

    public static List<ItemStack> potions() {
        List<ItemStack> stacks = new ArrayList<>();

        for (Holder<Potion> potion : BuiltInRegistries.POTION.asHolderIdMap()) {
            for (Item item : POTION_ITEMS) {
                ItemStack stack = new ItemStack(item);
                stack.set(DataComponents.POTION_CONTENTS,
                          new PotionContents(Optional.of(potion), Optional.empty(), List.of()));
                stacks.add(stack);
            }
        }

        return stacks;
    }

    public static List<FluidStack> sources() {
        List<FluidStack> fluids = new ArrayList<>();

        for (Fluid fluid : BuiltInRegistries.FLUID) {
            if (fluid.isSource(fluid.defaultFluidState())) {
                fluids.add(new FluidStack(fluid, BUCKET));
            }
        }

        return fluids;
    }
}
