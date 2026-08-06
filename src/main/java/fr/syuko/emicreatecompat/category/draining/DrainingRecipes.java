package fr.syuko.emicreatecompat.category.draining;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
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

import java.util.ArrayList;
import java.util.List;

public final class DrainingRecipes {

    private static final int BUCKET = 1000;

    private DrainingRecipes() {
    }

    public static List<DrainingDisplay> all(RecipeManager manager) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }

        List<DrainingDisplay> displays = new ArrayList<>();
        addDataRecipes(displays, manager);
        addPotions(displays);
        addContainers(displays);
        return displays;
    }

    private static void addDataRecipes(List<DrainingDisplay> displays, RecipeManager manager) {
        RecipeType<EmptyingRecipe> emptying = AllRecipeTypes.EMPTYING.getType();

        for (RecipeHolder<EmptyingRecipe> holder : manager.getAllRecipesFor(emptying)) {
            EmptyingRecipe recipe = holder.value();
            if (recipe.getIngredients().isEmpty() || recipe.getRollableResults().isEmpty()) {
                continue;
            }

            FluidStack fluid = recipe.getResultingFluid();
            ItemStack output = recipe.getRollableResults().getFirst().getStack();
            if (fluid.isEmpty() || output.isEmpty()) {
                continue;
            }

            displays.add(new DrainingDisplay(holder.id(), recipe.getIngredients().getFirst(), fluid, output));
        }
    }

    private static void addPotions(List<DrainingDisplay> displays) {
        for (ItemStack potion : FluidItems.potions()) {
            if (!PotionFluidHandler.isPotionItem(potion)) {
                continue;
            }

            FluidStack fluid = PotionFluidHandler.getFluidFromPotionItem(potion);
            if (fluid.isEmpty()) {
                continue;
            }

            displays.add(new DrainingDisplay(syntheticId("potion", potion, fluid),
                                             Ingredient.of(potion),
                                             fluid,
                                             new ItemStack(Items.GLASS_BOTTLE)));
        }
    }

    private static void addContainers(List<DrainingDisplay> displays) {
        for (ItemStack container : FluidItems.withFluidHandler()) {
            ItemStack copy = container.copy();
            IFluidHandlerItem handler = copy.getCapability(Capabilities.FluidHandler.ITEM);
            if (handler == null) {
                continue;
            }

            FluidStack drained = handler.drain(BUCKET, IFluidHandler.FluidAction.EXECUTE);
            ItemStack result = handler.getContainer();
            if (drained.isEmpty() || result.isEmpty()) {
                continue;
            }

            ItemStack
                    emptied =
                    ItemHelper.sameItem(container, result)
                    ? container
                    : result;
            displays.add(new DrainingDisplay(syntheticId("empty", container, drained),
                                             Ingredient.of(container),
                                             drained,
                                             emptied));
        }
    }

    private static ResourceLocation syntheticId(String prefix, ItemStack item, FluidStack fluid) {
        ResourceLocation itemName = RegisteredObjectsHelper.getKeyOrThrow(item.getItem());
        ResourceLocation fluidName = RegisteredObjectsHelper.getKeyOrThrow(fluid.getFluid());
        return ResourceLocation.fromNamespaceAndPath(Emicreatecompat.MODID,
                                                     "/draining/" + prefix + "/" + itemName.getNamespace() + "_" + itemName.getPath() + "_" + fluidName.getNamespace() + "_" + fluidName.getPath());
    }
}
