package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.bom.BoM;
import dev.emi.emi.bom.ChanceState;
import dev.emi.emi.bom.MaterialNode;
import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.BoMScreen;
import fr.syuko.emicreatecompat.emi.bom.ChanceModeButton;
import fr.syuko.emicreatecompat.emi.bom.ChancedProduction;
import fr.syuko.emicreatecompat.emi.bom.ExpectedCostIndex;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BoMScreen.class, remap = false)
public abstract class BoMScreenMixin {

    @Shadow
    public abstract void recalculateTree();

    @Redirect(method = "addNewNodes", at = @At(value = "INVOKE", target = "Ldev/emi/emi/bom/ChanceState;produce(F)Ldev/emi/emi/bom/ChanceState;"))
    private ChanceState emicreatecompat$snapAttempts(ChanceState chance,
                                                     float produceChance,
                                                     MaterialNode node,
                                                     long multiplier,
                                                     long divisor,
                                                     int depth,
                                                     ChanceState incoming) {
        return ChancedProduction.snappedToWholeAttempts(chance, produceChance, node, multiplier);
    }

    @Inject(method = "recalculateTree", at = @At("HEAD"))
    private void emicreatecompat$measureExpectedCosts(CallbackInfo ci) {
        ExpectedCostIndex.rebuild();
    }

    @Inject(method = "onClose", at = @At("TAIL"), remap = true)
    private void emicreatecompat$dropExpectedCosts(CallbackInfo ci) {
        ExpectedCostIndex.clear();
    }

    @Inject(method = "render", at = @At("TAIL"), remap = true)
    private void emicreatecompat$renderChanceModeButton(GuiGraphics graphics,
                                                        int mouseX,
                                                        int mouseY,
                                                        float delta,
                                                        CallbackInfo ci) {
        if (BoM.tree == null) {
            return;
        }

        ChanceModeButton.render(graphics, mouseX, mouseY);
        if (ChanceModeButton.contains(mouseX, mouseY)) {
            EmiRenderHelper.drawTooltip((BoMScreen) (Object) this,
                                        EmiDrawContext.wrap(graphics),
                                        ChanceModeButton.tooltip(),
                                        mouseX,
                                        mouseY);
        }
    }

    @Inject(method = "getHoveredStack", at = @At("HEAD"), cancellable = true)
    private void emicreatecompat$ignoreStacksUnderButton(int mouseX, int mouseY, CallbackInfoReturnable<Object> cir) {
        if (BoM.tree != null && ChanceModeButton.contains(mouseX, mouseY)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true, remap = true)
    private void emicreatecompat$toggleChanceMode(double mouseX,
                                                  double mouseY,
                                                  int button,
                                                  CallbackInfoReturnable<Boolean> cir) {
        if (BoM.tree == null || button != 0 || !ChanceModeButton.contains(mouseX, mouseY)) {
            return;
        }

        ChanceModeButton.toggle();
        recalculateTree();
        cir.setReturnValue(true);
    }
}
