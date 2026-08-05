package fr.syuko.emicreatecompat.create.recipe;

import net.minecraft.world.item.ItemStack;

public record ChancedStack(ItemStack stack, float chance) {
}
