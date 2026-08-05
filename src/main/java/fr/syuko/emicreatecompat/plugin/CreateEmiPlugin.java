package fr.syuko.emicreatecompat.plugin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import fr.syuko.emicreatecompat.category.automaticbrewing.AutomaticBrewingEmiRecipe;
import fr.syuko.emicreatecompat.category.automaticbrewing.AutomaticBrewingRecipes;
import fr.syuko.emicreatecompat.category.automaticpacking.AutomaticPackingEmiRecipe;
import fr.syuko.emicreatecompat.category.automaticpacking.AutomaticPackingRecipes;
import fr.syuko.emicreatecompat.category.automaticshaped.AutomaticShapedEmiRecipe;
import fr.syuko.emicreatecompat.category.automaticshaped.AutomaticShapedRecipes;
import fr.syuko.emicreatecompat.category.automaticshapeless.AutomaticShapelessEmiRecipe;
import fr.syuko.emicreatecompat.category.automaticshapeless.AutomaticShapelessRecipes;
import fr.syuko.emicreatecompat.category.blockcutting.BlockCuttingEmiRecipe;
import fr.syuko.emicreatecompat.category.blockcutting.BlockCuttingRecipes;
import fr.syuko.emicreatecompat.category.conversion.ConversionEmiRecipe;
import fr.syuko.emicreatecompat.category.conversion.ConversionRecipes;
import fr.syuko.emicreatecompat.category.crushing.CrushingEmiRecipe;
import fr.syuko.emicreatecompat.category.crushing.CrushingRecipes;
import fr.syuko.emicreatecompat.category.deploying.DeployingEmiRecipe;
import fr.syuko.emicreatecompat.category.deploying.DeployingRecipes;
import fr.syuko.emicreatecompat.category.fanblasting.FanBlastingEmiRecipe;
import fr.syuko.emicreatecompat.category.fanblasting.FanBlastingRecipes;
import fr.syuko.emicreatecompat.category.fanhaunting.FanHauntingEmiRecipe;
import fr.syuko.emicreatecompat.category.fanhaunting.FanHauntingRecipes;
import fr.syuko.emicreatecompat.category.fansmoking.FanSmokingEmiRecipe;
import fr.syuko.emicreatecompat.category.fansmoking.FanSmokingRecipes;
import fr.syuko.emicreatecompat.category.fanwashing.FanWashingEmiRecipe;
import fr.syuko.emicreatecompat.category.fanwashing.FanWashingRecipes;
import fr.syuko.emicreatecompat.category.itemapplication.ItemApplicationEmiRecipe;
import fr.syuko.emicreatecompat.category.itemapplication.ItemApplicationRecipes;
import fr.syuko.emicreatecompat.category.milling.MillingEmiRecipe;
import fr.syuko.emicreatecompat.category.milling.MillingRecipes;
import fr.syuko.emicreatecompat.category.mixing.MixingEmiRecipe;
import fr.syuko.emicreatecompat.category.mixing.MixingRecipes;
import fr.syuko.emicreatecompat.category.packing.PackingEmiRecipe;
import fr.syuko.emicreatecompat.category.packing.PackingRecipes;
import fr.syuko.emicreatecompat.category.polishing.PolishingEmiRecipe;
import fr.syuko.emicreatecompat.category.polishing.PolishingRecipes;
import fr.syuko.emicreatecompat.category.pressing.PressingEmiRecipe;
import fr.syuko.emicreatecompat.category.pressing.PressingRecipes;
import fr.syuko.emicreatecompat.category.sawing.SawingEmiRecipe;
import fr.syuko.emicreatecompat.category.sawing.SawingRecipes;
import fr.syuko.emicreatecompat.category.sequencedassembly.SequencedAssemblyEmiRecipe;
import fr.syuko.emicreatecompat.category.sequencedassembly.SequencedAssemblyRecipes;
import fr.syuko.emicreatecompat.config.Config;
import fr.syuko.emicreatecompat.create.recipe.EncasedFan;
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
                                                                          .toList()),

                       new RegisteredCategory(CreateEmiCategories.MILLING,
                                              List.of(EmiStack.of(AllBlocks.MILLSTONE.get())),
                                              manager -> MillingRecipes.all(manager)
                                                                       .stream()
                                                                       .map(MillingEmiRecipe::new)
                                                                       .toList()),

                       new RegisteredCategory(CreateEmiCategories.SAWING,
                                              List.of(EmiStack.of(AllBlocks.MECHANICAL_SAW.get())),
                                              manager -> SawingRecipes.all(manager)
                                                                      .stream()
                                                                      .map(SawingEmiRecipe::new)
                                                                      .toList()),

                       new RegisteredCategory(CreateEmiCategories.ITEM_APPLICATION,
                                              List.of(),
                                              manager -> ItemApplicationRecipes.all(manager)
                                                                               .stream()
                                                                               .map(ItemApplicationEmiRecipe::new)
                                                                               .toList()),

                       new RegisteredCategory(CreateEmiCategories.FAN_WASHING,
                                              List.of(EmiStack.of(EncasedFan.labelled("fan_washing"))),
                                              manager -> FanWashingRecipes.all(manager)
                                                                          .stream()
                                                                          .map(FanWashingEmiRecipe::new)
                                                                          .toList()),

                       new RegisteredCategory(CreateEmiCategories.FAN_SMOKING,
                                              List.of(EmiStack.of(EncasedFan.labelled("fan_smoking"))),
                                              manager -> FanSmokingRecipes.all(manager)
                                                                          .stream()
                                                                          .map(FanSmokingEmiRecipe::new)
                                                                          .toList()),

                       new RegisteredCategory(CreateEmiCategories.FAN_HAUNTING,
                                              List.of(EmiStack.of(EncasedFan.labelled("fan_haunting"))),
                                              manager -> FanHauntingRecipes.all(manager)
                                                                           .stream()
                                                                           .map(FanHauntingEmiRecipe::new)
                                                                           .toList()),

                       new RegisteredCategory(CreateEmiCategories.CRUSHING,
                                              List.of(EmiStack.of(AllBlocks.CRUSHING_WHEEL.get())),
                                              manager -> CrushingRecipes.all(manager)
                                                                        .stream()
                                                                        .map(CrushingEmiRecipe::new)
                                                                        .toList()),

                       new RegisteredCategory(CreateEmiCategories.DEPLOYING,
                                              List.of(EmiStack.of(AllBlocks.DEPLOYER.get()),
                                                      EmiStack.of(AllBlocks.DEPOT.get()),
                                                      EmiStack.of(AllItems.BELT_CONNECTOR.get())),
                                              manager -> DeployingRecipes.all(manager)
                                                                         .stream()
                                                                         .map(DeployingEmiRecipe::new)
                                                                         .toList()),

                       new RegisteredCategory(CreateEmiCategories.FAN_BLASTING,
                                              List.of(EmiStack.of(EncasedFan.labelled("fan_blasting"))),
                                              manager -> FanBlastingRecipes.all(manager)
                                                                           .stream()
                                                                           .map(FanBlastingEmiRecipe::new)
                                                                           .toList()),

                       new RegisteredCategory(CreateEmiCategories.BLOCK_CUTTING,
                                              List.of(EmiStack.of(AllBlocks.MECHANICAL_SAW.get())),
                                              manager -> BlockCuttingRecipes.all(manager)
                                                                            .stream()
                                                                            .map(BlockCuttingEmiRecipe::new)
                                                                            .toList()),

                       new RegisteredCategory(CreateEmiCategories.AUTOMATIC_SHAPED,
                                              List.of(EmiStack.of(AllBlocks.MECHANICAL_CRAFTER.get())),
                                              manager -> AutomaticShapedRecipes.all(manager)
                                                                               .stream()
                                                                               .map(AutomaticShapedEmiRecipe::new)
                                                                               .toList()),

                       new RegisteredCategory(CreateEmiCategories.MIXING,
                                              List.of(EmiStack.of(AllBlocks.MECHANICAL_MIXER.get()),
                                                      EmiStack.of(AllBlocks.BASIN.get())),
                                              manager -> MixingRecipes.all(manager)
                                                                      .stream()
                                                                      .map(MixingEmiRecipe::new)
                                                                      .toList()),

                       new RegisteredCategory(CreateEmiCategories.AUTOMATIC_SHAPELESS,
                                              List.of(EmiStack.of(AllBlocks.MECHANICAL_MIXER.get()),
                                                      EmiStack.of(AllBlocks.BASIN.get())),
                                              manager -> AutomaticShapelessRecipes.all(manager)
                                                                                  .stream()
                                                                                  .map(AutomaticShapelessEmiRecipe::new)
                                                                                  .toList()),

                       new RegisteredCategory(CreateEmiCategories.AUTOMATIC_BREWING,
                                              List.of(EmiStack.of(AllBlocks.MECHANICAL_MIXER.get()),
                                                      EmiStack.of(AllBlocks.BASIN.get())),
                                              manager -> AutomaticBrewingRecipes.all()
                                                                                .stream()
                                                                                .map(AutomaticBrewingEmiRecipe::new)
                                                                                .toList()),

                       new RegisteredCategory(CreateEmiCategories.PACKING,
                                              List.of(EmiStack.of(AllBlocks.MECHANICAL_PRESS.get()),
                                                      EmiStack.of(AllBlocks.BASIN.get())),
                                              manager -> PackingRecipes.all(manager)
                                                                       .stream()
                                                                       .map(PackingEmiRecipe::new)
                                                                       .toList()),

                       new RegisteredCategory(CreateEmiCategories.AUTOMATIC_PACKING,
                                              List.of(EmiStack.of(AllBlocks.MECHANICAL_PRESS.get()),
                                                      EmiStack.of(AllBlocks.BASIN.get())),
                                              manager -> AutomaticPackingRecipes.all(manager)
                                                                                .stream()
                                                                                .map(AutomaticPackingEmiRecipe::new)
                                                                                .toList()),

                       new RegisteredCategory(CreateEmiCategories.SEQUENCED_ASSEMBLY,
                                              List.of(),
                                              manager -> SequencedAssemblyRecipes.all(manager)
                                                                                 .stream()
                                                                                 .map(SequencedAssemblyEmiRecipe::new)
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
