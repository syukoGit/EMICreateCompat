package fr.syuko.emicreatecompat.category.automaticbrewing;

import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.infrastructure.config.AllConfigs;
import fr.syuko.emicreatecompat.category.basin.BasinDisplay;
import fr.syuko.emicreatecompat.category.basin.BasinDisplays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public final class AutomaticBrewingRecipes {

    private AutomaticBrewingRecipes() {
    }

    public static List<BasinDisplay> all() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !AllConfigs.server().recipes.allowBrewingInMixer.get()) {
            return List.of();
        }

        List<BasinDisplay> displays = new ArrayList<>();

        for (RecipeHolder<? extends BasinRecipe> holder : PotionMixingRecipes.createRecipes(level)) {
            BasinDisplays.add(displays, holder.id(), holder.value());
        }

        return displays;
    }
}
