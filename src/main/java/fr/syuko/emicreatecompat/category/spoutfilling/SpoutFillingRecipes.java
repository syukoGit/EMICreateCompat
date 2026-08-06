package fr.syuko.emicreatecompat.category.spoutfilling;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.item.ItemHelper;
import fr.syuko.emicreatecompat.Emicreatecompat;
import fr.syuko.emicreatecompat.create.recipe.FluidItems;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.List;

public final class SpoutFillingRecipes {

    private static final int BUCKET = 1000;

    private SpoutFillingRecipes() {
    }

    public static List<SpoutFillingDisplay> all(RecipeManager manager) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }

        List<SpoutFillingDisplay> displays = new ArrayList<>();
        addDataRecipes(displays, manager);
        addPotions(displays);
        addContainers(displays);
        return displays;
    }

    private static void addDataRecipes(List<SpoutFillingDisplay> displays, RecipeManager manager) {
        RecipeType<FillingRecipe> filling = AllRecipeTypes.FILLING.getType();

        for (RecipeHolder<FillingRecipe> holder : manager.getAllRecipesFor(filling)) {
            FillingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty() || recipe.getRollableResults().isEmpty()) {
                continue;
            }

            SizedFluidIngredient required = recipe.getRequiredFluid();
            List<FluidStack> fluids = new ArrayList<>();
            for (FluidStack option : required.getFluids()) {
                FluidStack sized = option.copy();
                sized.setAmount(required.amount());
                fluids.add(sized);
            }
            if (fluids.isEmpty()) {
                continue;
            }

            ItemStack output = recipe.getRollableResults().getFirst().getStack();
            if (output.isEmpty()) {
                continue;
            }

            displays.add(new SpoutFillingDisplay(holder.id(), recipe.getIngredients().getFirst(), fluids, output));
        }
    }

    private static void addPotions(List<SpoutFillingDisplay> displays) {
        for (ItemStack potion : FluidItems.potions()) {
            if (!PotionFluidHandler.isPotionItem(potion)) {
                continue;
            }

            FluidStack fluid = PotionFluidHandler.getFluidFromPotionItem(potion);
            if (fluid.isEmpty()) {
                continue;
            }

            displays.add(new SpoutFillingDisplay(syntheticId("potion", potion, fluid),
                                                 Ingredient.of(Items.GLASS_BOTTLE),
                                                 List.of(fluid),
                                                 potion));
        }
    }

    private static void addContainers(List<SpoutFillingDisplay> displays) {
        List<FluidStack> fluids = FluidItems.sources();

        for (ItemStack container : FluidItems.withFluidHandler()) {
            for (FluidStack fluid : fluids) {
                ItemStack filled = fill(container, fluid);
                if (filled == null) {
                    continue;
                }

                FluidStack used = fluid.copy();
                used.setAmount(BUCKET);
                displays.add(new SpoutFillingDisplay(syntheticId("fill", container, fluid),
                                                     Ingredient.of(container),
                                                     List.of(used),
                                                     filled));
            }
        }
    }

    private static ItemStack fill(ItemStack container, FluidStack fluid) {
        ItemStack copy = container.copy();
        IFluidHandlerItem handler = copy.getCapability(Capabilities.FluidHandler.ITEM);
        if (handler == null || !GenericItemFilling.isFluidHandlerValid(copy, handler)) {
            return null;
        }

        FluidStack toFill = fluid.copy();
        toFill.setAmount(BUCKET);
        handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);

        ItemStack result = handler.getContainer();
        if (result.isEmpty() || ItemHelper.sameItem(result, copy)) {
            return null;
        }

        return result;
    }

    private static ResourceLocation syntheticId(String prefix, ItemStack item, FluidStack fluid) {
        ResourceLocation itemName = RegisteredObjectsHelper.getKeyOrThrow(item.getItem());
        ResourceLocation fluidName = RegisteredObjectsHelper.getKeyOrThrow(fluid.getFluid());
        return ResourceLocation.fromNamespaceAndPath(Emicreatecompat.MODID,
                                                     "/spout_filling/" + prefix + "/" + itemName.getNamespace() + "_" + itemName.getPath() + "_" + fluidName.getNamespace() + "_" + fluidName.getPath());
    }
}
