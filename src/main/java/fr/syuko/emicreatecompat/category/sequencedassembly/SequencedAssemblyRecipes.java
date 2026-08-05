package fr.syuko.emicreatecompat.category.sequencedassembly;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.List;

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
            AssemblyStep step = step(sequenced);
            if (step == null) {
                return List.of();
            }
            steps.add(step);
        }

        return List.copyOf(steps);
    }

    private static AssemblyStep step(SequencedRecipe<?> sequenced) {
        ProcessingRecipe<?, ?> wrapped = sequenced.getRecipe();

        if (wrapped instanceof PressingRecipe) {
            return new AssemblyStep(AssemblyStep.StepKind.PRESSING, null, List.of(), false);
        }

        if (wrapped instanceof CuttingRecipe) {
            return new AssemblyStep(AssemblyStep.StepKind.CUTTING, null, List.of(), false);
        }

        if (wrapped instanceof DeployerApplicationRecipe deploying) {
            if (deploying.getIngredients().size() < 2) {
                return null;
            }
            return new AssemblyStep(AssemblyStep.StepKind.DEPLOYING,
                                    deploying.getIngredients().get(1),
                                    List.of(),
                                    deploying.shouldKeepHeldItem());
        }

        if (wrapped instanceof FillingRecipe filling) {
            if (filling.getFluidIngredients().isEmpty()) {
                return null;
            }
            SizedFluidIngredient ingredient = filling.getFluidIngredients().getFirst();
            List<FluidStack> options = new ArrayList<>();
            for (FluidStack option : ingredient.getFluids()) {
                FluidStack sized = option.copy();
                sized.setAmount(ingredient.amount());
                options.add(sized);
            }
            if (options.isEmpty()) {
                return null;
            }
            return new AssemblyStep(AssemblyStep.StepKind.SPOUTING, null, List.copyOf(options), false);
        }

        return null;
    }
}
