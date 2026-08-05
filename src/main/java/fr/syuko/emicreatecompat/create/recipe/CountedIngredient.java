package fr.syuko.emicreatecompat.create.recipe;

import net.minecraft.world.item.crafting.Ingredient;

public record CountedIngredient(Ingredient ingredient, int count) {
}
