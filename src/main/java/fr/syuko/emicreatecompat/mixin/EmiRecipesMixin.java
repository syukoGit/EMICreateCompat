package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.registry.EmiRecipes;
import fr.syuko.emicreatecompat.chance.ChanceInjector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EmiRecipes.class, remap = false)
public abstract class EmiRecipesMixin {

    @Shadow
    private static List<EmiRecipe> recipes;

    @Inject(method = "bake", at = @At("TAIL"))
    private static void emicreatecompat$injectCreateChances(CallbackInfo ci) {
        ChanceInjector.applyTo(recipes);
    }
}
