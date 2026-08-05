package fr.syuko.emicreatecompat.category.deploying;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import fr.syuko.emicreatecompat.create.recipe.ProcessingOutputs;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public final class DeployingRecipes {

    private DeployingRecipes() {
    }

    public static List<DeployingDisplay> all(RecipeManager manager) {
        RecipeType<DeployerApplicationRecipe> deploying = AllRecipeTypes.DEPLOYING.getType();
        RecipeType<SandPaperPolishingRecipe> polishing = AllRecipeTypes.SANDPAPER_POLISHING.getType();
        RecipeType<ItemApplicationRecipe> itemApplication = AllRecipeTypes.ITEM_APPLICATION.getType();

        List<DeployingDisplay> displays = new ArrayList<>();

        for (RecipeHolder<DeployerApplicationRecipe> holder : manager.getAllRecipesFor(deploying)) {
            add(displays, holder);
        }

        for (RecipeHolder<SandPaperPolishingRecipe> holder : manager.getAllRecipesFor(polishing)) {
            add(displays, DeployerApplicationRecipe.convert(holder));
        }

        for (RecipeHolder<ItemApplicationRecipe> holder : manager.getAllRecipesFor(itemApplication)) {
            add(displays, ManualApplicationRecipe.asDeploying(holder));
        }

        return displays;
    }

    private static void add(List<DeployingDisplay> displays, RecipeHolder<DeployerApplicationRecipe> holder) {
        if (!AllRecipeTypes.CAN_BE_AUTOMATED.test(holder)) {
            return;
        }

        DeployerApplicationRecipe recipe = holder.value();
        if (recipe.getIngredients().size() < 2) {
            return;
        }

        List<ChancedStack> outputs = ProcessingOutputs.of(recipe.getRollableResults());
        if (outputs.isEmpty()) {
            return;
        }

        displays.add(new DeployingDisplay(holder.id(),
                                          recipe.getProcessedItem(),
                                          recipe.getRequiredHeldItem(),
                                          recipe.shouldKeepHeldItem(),
                                          outputs));
    }
}
