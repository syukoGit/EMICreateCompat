package fr.syuko.emicreatecompat.category.deploying;

import fr.syuko.emicreatecompat.create.recipe.ChancedStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record DeployingDisplay(ResourceLocation id, Ingredient processed, Ingredient heldItem, boolean keepHeldItem,
                               List<ChancedStack> outputs) {
}
