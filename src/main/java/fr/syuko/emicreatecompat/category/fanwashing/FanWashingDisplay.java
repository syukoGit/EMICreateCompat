package fr.syuko.emicreatecompat.category.fanwashing;

import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record FanWashingDisplay(ResourceLocation id, Ingredient input, List<ChancedStack> outputs) {
}
