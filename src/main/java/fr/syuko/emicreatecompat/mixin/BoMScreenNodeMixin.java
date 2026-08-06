package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.bom.ChanceState;
import dev.emi.emi.bom.MaterialNode;
import fr.syuko.emicreatecompat.emi.bom.BomAmountMode;
import fr.syuko.emicreatecompat.emi.bom.ChancedNodeText;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "dev.emi.emi.screen.BoMScreen$Node", remap = false)
public abstract class BoMScreenNodeMixin {

    @Shadow
    public MaterialNode node;

    @Shadow
    public long amount;

    @Shadow
    public ChanceState chance;

    @ModifyVariable(method = "setColor", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private boolean emicreatecompat$hideChanceTint(boolean chanced) {
        return chanced && !BomAmountMode.rawAmounts();
    }

    @Inject(method = "getAmountText", at = @At("HEAD"), cancellable = true)
    private void emicreatecompat$snapToWholeCrafts(CallbackInfoReturnable<Component> cir) {
        if (chance.chanced()) {
            cir.setReturnValue(ChancedNodeText.amountText(node, amount, chance));
        }
    }
}
