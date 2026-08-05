package fr.syuko.emicreatecompat.plugin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.category.conversion.ConversionDisplay;
import fr.syuko.emicreatecompat.category.conversion.ConversionEmiRecipe;
import fr.syuko.emicreatecompat.category.conversion.ConversionRecipes;
import fr.syuko.emicreatecompat.category.polishing.PolishingDisplay;
import fr.syuko.emicreatecompat.category.polishing.PolishingEmiRecipe;
import fr.syuko.emicreatecompat.category.polishing.PolishingRecipes;
import fr.syuko.emicreatecompat.category.pressing.PressingDisplay;
import fr.syuko.emicreatecompat.category.pressing.PressingEmiRecipe;
import fr.syuko.emicreatecompat.category.pressing.PressingRecipes;
import fr.syuko.emicreatecompat.config.Config;
import net.neoforged.fml.ModList;

@EmiEntrypoint
public class CreateEmiPlugin implements EmiPlugin {
    private static final String JEI_MODID = "jei";

    private static boolean shouldRegister() {
        return switch (Config.recipeRegistration) {
            case ALWAYS -> true;
            case NEVER -> false;
            case AUTO -> !ModList.get().isLoaded(JEI_MODID);
        };
    }

    @Override
    public void register(EmiRegistry registry) {
        if (!shouldRegister()) {
            return;
        }

        registry.addCategory(CreateEmiCategories.PRESSING);
        registry.addWorkstation(CreateEmiCategories.PRESSING, EmiStack.of(AllBlocks.MECHANICAL_PRESS.get()));

        for (PressingDisplay display : PressingRecipes.all(registry.getRecipeManager())) {
            registry.addRecipe(new PressingEmiRecipe(display));
        }

        registry.addCategory(CreateEmiCategories.SANDPAPER_POLISHING);
        registry.addWorkstation(CreateEmiCategories.SANDPAPER_POLISHING, EmiStack.of(AllItems.SAND_PAPER.get()));
        registry.addWorkstation(CreateEmiCategories.SANDPAPER_POLISHING, EmiStack.of(AllItems.RED_SAND_PAPER.get()));

        for (PolishingDisplay display : PolishingRecipes.all(registry.getRecipeManager())) {
            registry.addRecipe(new PolishingEmiRecipe(display));
        }

        registry.addCategory(CreateEmiCategories.MYSTERY_CONVERSION);

        for (ConversionDisplay display : ConversionRecipes.all()) {
            registry.addRecipe(new ConversionEmiRecipe(display));
        }
    }
}
