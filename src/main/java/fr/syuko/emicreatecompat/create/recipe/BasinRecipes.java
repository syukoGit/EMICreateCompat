package fr.syuko.emicreatecompat.create.recipe;

import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.item.ItemHelper;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BasinRecipes {

    private BasinRecipes() {
    }

    public static List<CountedIngredient> itemInputs(BasinRecipe recipe) {
        List<CountedIngredient> inputs = new ArrayList<>();

        for (Pair<Ingredient, MutableInt> condensed : ItemHelper.condenseIngredients(present(recipe.getIngredients()))) {
            inputs.add(new CountedIngredient(condensed.getFirst(), condensed.getSecond().getValue()));
        }

        return List.copyOf(inputs);
    }

    public static NonNullList<Ingredient> present(List<Ingredient> ingredients) {
        NonNullList<Ingredient> filtered = NonNullList.create();

        for (Ingredient ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                filtered.add(ingredient);
            }
        }

        return filtered;
    }

    public static List<List<FluidStack>> fluidInputs(BasinRecipe recipe) {
        List<List<FluidStack>> inputs = new ArrayList<>();

        for (SizedFluidIngredient ingredient : recipe.getFluidIngredients()) {
            List<FluidStack> options = new ArrayList<>();
            for (FluidStack stack : ingredient.getFluids()) {
                FluidStack sized = stack.copy();
                sized.setAmount(ingredient.amount());
                options.add(sized);
            }
            if (!options.isEmpty()) {
                inputs.add(List.copyOf(options));
            }
        }

        return List.copyOf(inputs);
    }

    public static List<FluidStack> fluidOutputs(BasinRecipe recipe) {
        return List.copyOf(Arrays.asList(recipe.getFluidResults().toArray(new FluidStack[0])));
    }

    public static HeatRequirement heat(BasinRecipe recipe) {
        HeatCondition condition = recipe.getRequiredHeat();
        if (condition == HeatCondition.SUPERHEATED) {
            return HeatRequirement.SUPERHEATED;
        }
        if (condition == HeatCondition.HEATED) {
            return HeatRequirement.HEATED;
        }
        return HeatRequirement.NONE;
    }
}
