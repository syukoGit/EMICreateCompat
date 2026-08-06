package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.screen.BoMScreen;
import fr.syuko.emicreatecompat.emi.bom.ExpectedCostIndex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BoMScreen.class, remap = false)
public abstract class BoMScreenMixin {

    @Inject(method = "recalculateTree", at = @At("HEAD"))
    private void emicreatecompat$measureExpectedCosts(CallbackInfo ci) {
        ExpectedCostIndex.rebuild();
    }

    @Inject(method = "onClose", at = @At("TAIL"), remap = true)
    private void emicreatecompat$dropExpectedCosts(CallbackInfo ci) {
        ExpectedCostIndex.clear();
    }
}
