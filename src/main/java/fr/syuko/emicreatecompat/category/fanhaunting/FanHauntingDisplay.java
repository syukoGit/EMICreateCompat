package fr.syuko.emicreatecompat.category.fanhaunting;

import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record FanHauntingDisplay(ResourceLocation id, Ingredient input, List<ChancedStack> outputs) {
}
