package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.bom.ChanceState;
import fr.syuko.emicreatecompat.emi.bom.BomAmountMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChanceState.class, remap = false)
public abstract class ChanceStateMixin {

    @Inject(method = "produce", at = @At("HEAD"), cancellable = true)
    private void emicreatecompat$keepProduceFlat(float chance, CallbackInfoReturnable<ChanceState> cir) {
        if (BomAmountMode.rawAmounts()) {
            cir.setReturnValue((ChanceState) (Object) this);
        }
    }

    @Inject(method = "consume", at = @At("HEAD"), cancellable = true)
    private void emicreatecompat$keepConsumeFlat(float chance, CallbackInfoReturnable<ChanceState> cir) {
        if (BomAmountMode.rawAmounts()) {
            cir.setReturnValue((ChanceState) (Object) this);
        }
    }
}
