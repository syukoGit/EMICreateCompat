package fr.syuko.emicreatecompat.category.basin;

import com.simibubi.create.content.processing.basin.BasinRecipe;
import fr.syuko.emicreatecompat.create.recipe.BasinRecipes;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.ProcessingOutputs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public final class BasinDisplays {

    private BasinDisplays() {
    }

    public static void add(List<BasinDisplay> displays, ResourceLocation id, BasinRecipe recipe) {
        List<ChancedStack> itemOutputs = ProcessingOutputs.of(recipe.getRollableResults());
        List<FluidStack> fluidOutputs = BasinRecipes.fluidOutputs(recipe);
        if (itemOutputs.isEmpty() && fluidOutputs.isEmpty()) {
            return;
        }

        displays.add(new BasinDisplay(id,
                                      BasinRecipes.itemInputs(recipe),
                                      BasinRecipes.fluidInputs(recipe),
                                      itemOutputs,
                                      fluidOutputs,
                                      BasinRecipes.heat(recipe)));
    }
}
