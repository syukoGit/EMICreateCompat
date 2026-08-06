package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.bom.ChanceState;
import dev.emi.emi.bom.MaterialNode;
import dev.emi.emi.bom.TreeCost;
import fr.syuko.emicreatecompat.emi.bom.ChancedNode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TreeCost.class, remap = false)
public abstract class TreeCostMixin {

    @Inject(method = "calculateCost", at = @At(value = "FIELD", target = "Ldev/emi/emi/bom/MaterialNode;neededBatches:J", opcode = Opcodes.PUTFIELD, ordinal = 1, shift = At.Shift.AFTER))
    private void emicreatecompat$recordChanceMultiplier(MaterialNode node,
                                                        long amount,
                                                        ChanceState chance,
                                                        boolean trackProgress,
                                                        CallbackInfo ci) {
        ((ChancedNode) node).emicreatecompat$setChanceMultiplier(chance.chanced()
                                                                 ? chance.chance()
                                                                 : 1f);
    }
}
