package fr.syuko.emicreatecompat.category.itemapplication;

import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record ItemApplicationDisplay(ResourceLocation id, Ingredient processed, Ingredient heldItem,
                                     boolean keepHeldItem, List<ChancedStack> outputs) {
}
