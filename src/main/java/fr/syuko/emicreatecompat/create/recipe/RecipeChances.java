package fr.syuko.emicreatecompat.create.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public final class RecipeChances {

    private RecipeChances() {
    }

    public static List<ChancedStack> of(Recipe<?> recipe) {
        if (recipe instanceof ProcessingRecipe<?, ?> processing) {
            return whenAnyIsChanced(ProcessingOutputs.of(processing.getRollableResults()));
        }

        if (recipe instanceof SequencedAssemblyRecipe assembly) {
            return whenAnyIsChanced(assembledResult(assembly));
        }

        return List.of();
    }

    private static List<ChancedStack> assembledResult(SequencedAssemblyRecipe assembly) {
        if (assembly.resultPool.isEmpty()) {
            return List.of();
        }

        ItemStack result = assembly.resultPool.getFirst().getStack();
        if (result.isEmpty()) {
            return List.of();
        }

        return List.of(new ChancedStack(result, assembly.getOutputChance()));
    }

    private static List<ChancedStack> whenAnyIsChanced(List<ChancedStack> stacks) {
        for (ChancedStack stack : stacks) {
            if (stack.chance() != 1f) {
                return stacks;
            }
        }

        return List.of();
    }
}
