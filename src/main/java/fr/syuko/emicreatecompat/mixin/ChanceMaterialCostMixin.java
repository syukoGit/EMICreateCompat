package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.bom.ChanceMaterialCost;
import fr.syuko.emicreatecompat.emi.bom.ChancedCosts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChanceMaterialCost.class, remap = false)
public abstract class ChanceMaterialCostMixin {

    @Inject(method = "getEffectiveAmount", at = @At("HEAD"), cancellable = true)
    private void emicreatecompat$snapToWholeCrafts(CallbackInfoReturnable<Long> cir) {
        cir.setReturnValue(ChancedCosts.effectiveAmount((ChanceMaterialCost) (Object) this));
    }
}
