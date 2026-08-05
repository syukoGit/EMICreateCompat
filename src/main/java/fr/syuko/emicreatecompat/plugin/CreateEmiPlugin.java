package fr.syuko.emicreatecompat.plugin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.category.conversion.ConversionEmiRecipe;
import fr.syuko.emicreatecompat.category.conversion.ConversionRecipes;
import fr.syuko.emicreatecompat.category.polishing.PolishingEmiRecipe;
import fr.syuko.emicreatecompat.category.polishing.PolishingRecipes;
import fr.syuko.emicreatecompat.category.pressing.PressingEmiRecipe;
import fr.syuko.emicreatecompat.category.pressing.PressingRecipes;
import fr.syuko.emicreatecompat.config.Config;
import net.neoforged.fml.ModList;

import java.util.List;

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

    private static List<RegisteredCategory> categories() {
        return List.of(new RegisteredCategory(CreateEmiCategories.PRESSING,
                                              List.of(EmiStack.of(AllBlocks.MECHANICAL_PRESS.get())),
                                              manager -> PressingRecipes.all(manager)
                                                                        .stream()
                                                                        .map(PressingEmiRecipe::new)
                                                                        .toList()),

                       new RegisteredCategory(CreateEmiCategories.SANDPAPER_POLISHING,
                                              List.of(EmiStack.of(AllItems.SAND_PAPER.get()),
                                                      EmiStack.of(AllItems.RED_SAND_PAPER.get())),
                                              manager -> PolishingRecipes.all(manager)
                                                                         .stream()
                                                                         .map(PolishingEmiRecipe::new)
                                                                         .toList()),

                       new RegisteredCategory(CreateEmiCategories.MYSTERY_CONVERSION,
                                              List.of(),
                                              manager -> ConversionRecipes.all()
                                                                          .stream()
                                                                          .map(ConversionEmiRecipe::new)
                                                                          .toList()));
    }

    @Override
    public void register(EmiRegistry registry) {
        if (!shouldRegister()) {
            return;
        }

        for (RegisteredCategory registered : categories()) {
            registry.addCategory(registered.category());

            for (EmiStack workstation : registered.workstations()) {
                registry.addWorkstation(registered.category(), workstation);
            }

            for (EmiRecipe recipe : registered.recipes().apply(registry.getRecipeManager())) {
                registry.addRecipe(recipe);
            }
        }
    }
}
