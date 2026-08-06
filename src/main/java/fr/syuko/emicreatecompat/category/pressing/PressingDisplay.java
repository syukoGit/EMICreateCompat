package fr.syuko.emicreatecompat.category.pressing;

import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record PressingDisplay(ResourceLocation id, Ingredient input, List<ChancedStack> outputs) {
}
