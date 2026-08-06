package fr.syuko.emicreatecompat.mixin;

import fr.syuko.emicreatecompat.emi.bom.BomAmountMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "dev.emi.emi.screen.BoMScreen$Node", remap = false)
public abstract class BoMScreenNodeMixin {

    @ModifyVariable(method = "setColor", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private boolean emicreatecompat$hideChanceTint(boolean chanced) {
        return chanced && !BomAmountMode.rawAmounts();
    }
}
