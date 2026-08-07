package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.bom.MaterialNode;
import fr.syuko.emicreatecompat.emi.bom.ChancedNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = MaterialNode.class, remap = false)
public abstract class MaterialNodeMixin implements ChancedNode {

    @Unique
    private float emicreatecompat$multiplier = 1f;

    @Override
    public float emicreatecompat$chanceMultiplier() {
        return emicreatecompat$multiplier;
    }

    @Override
    public void emicreatecompat$setChanceMultiplier(float multiplier) {
        emicreatecompat$multiplier = multiplier;
    }
}
