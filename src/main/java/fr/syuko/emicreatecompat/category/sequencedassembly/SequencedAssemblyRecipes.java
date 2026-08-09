package fr.syuko.emicreatecompat.category.sequencedassembly;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class SequencedAssemblyRecipes {

    private SequencedAssemblyRecipes() {
    }

    public static List<SequencedAssemblyDisplay> all(RecipeManager manager) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }

        RecipeType<SequencedAssemblyRecipe> assembly = AllRecipeTypes.SEQUENCED_ASSEMBLY.getType();
        List<SequencedAssemblyDisplay> displays = new ArrayList<>();

        for (RecipeHolder<SequencedAssemblyRecipe> holder : manager.getAllRecipesFor(assembly)) {
            SequencedAssemblyRecipe recipe = holder.value();

            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (output.isEmpty()) {
                continue;
            }

            List<AssemblyStep> steps = steps(recipe);
            if (steps.isEmpty()) {
                continue;
            }

            displays.add(new SequencedAssemblyDisplay(holder.id(),
                                                      recipe.getIngredient(),
                                                      output.copy(),
                                                      recipe.getOutputChance(),
                                                      recipe.getLoops(),
                                                      steps));
        }

        return displays;
    }

    private static List<AssemblyStep> steps(SequencedAssemblyRecipe recipe) {
        List<AssemblyStep> steps = new ArrayList<>();

        for (SequencedRecipe<?> sequenced : recipe.getSequence()) {
            steps.add(step(sequenced));
        }

        return List.copyOf(steps);
    }

    private static AssemblyStep step(SequencedRecipe<?> sequenced) {
        AssemblyStep known = knownStep(sequenced.getRecipe());

        return known != null
               ? known
               : genericStep(sequenced);
    }

    private static AssemblyStep knownStep(ProcessingRecipe<?, ?> wrapped) {
        if (wrapped instanceof PressingRecipe) {
            return AssemblyStep.pressing();
        }

        if (wrapped instanceof CuttingRecipe) {
            return AssemblyStep.cutting();
        }

        if (wrapped instanceof DeployerApplicationRecipe deploying) {
            if (deploying.getIngredients().size() < 2) {
                return null;
            }
            return AssemblyStep.deploying(deploying.getIngredients().get(1), deploying.shouldKeepHeldItem());
        }

        if (wrapped instanceof FillingRecipe filling) {
            if (filling.getFluidIngredients().isEmpty()) {
                return null;
            }
            List<FluidStack> options = sizedFluids(filling.getFluidIngredients().getFirst());
            if (options.isEmpty()) {
                return null;
            }
            return AssemblyStep.spouting(options);
        }

        return null;
    }

    private static AssemblyStep genericStep(SequencedRecipe<?> sequenced) {
        IAssemblyRecipe assembly = sequenced.getAsAssemblyRecipe();

        List<Ingredient> ingredients = new ArrayList<>();
        assembly.addAssemblyIngredients(ingredients);

        List<SizedFluidIngredient> fluidIngredients = new ArrayList<>();
        assembly.addAssemblyFluidIngredients(fluidIngredients);

        Set<ItemLike> machines = new LinkedHashSet<>();
        assembly.addRequiredMachines(machines);

        return AssemblyStep.generic(ingredients.isEmpty()
                                    ? null
                                    : ingredients.getFirst(),
                                    fluidIngredients.isEmpty()
                                    ? List.of()
                                    : sizedFluids(fluidIngredients.getFirst()),
                                    machines.isEmpty()
                                    ? ItemStack.EMPTY
                                    : new ItemStack(machines.iterator().next()));
    }

    private static List<FluidStack> sizedFluids(SizedFluidIngredient ingredient) {
        List<FluidStack> options = new ArrayList<>();

        for (FluidStack option : ingredient.getFluids()) {
            FluidStack sized = option.copy();
            sized.setAmount(ingredient.amount());
            options.add(sized);
        }

        return List.copyOf(options);
    }
}
