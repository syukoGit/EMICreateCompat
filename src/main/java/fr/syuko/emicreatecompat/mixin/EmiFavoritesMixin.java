package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.bom.MaterialNode;
import dev.emi.emi.runtime.EmiFavorites;
import fr.syuko.emicreatecompat.emi.bom.ChancedFavorites;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EmiFavorites.class, remap = false)
public abstract class EmiFavoritesMixin {

    @Redirect(method = "countRecipes",
              at = @At(value = "FIELD",
                       target = "Ldev/emi/emi/bom/MaterialNode;totalNeeded:J",
                       opcode = Opcodes.GETFIELD))
    private static long emicreatecompat$alignNeededWithTree(MaterialNode node) {
        return ChancedFavorites.neededFor(node);
    }
}
