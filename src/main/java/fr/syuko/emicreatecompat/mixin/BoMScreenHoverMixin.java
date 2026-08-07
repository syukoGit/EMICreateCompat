package fr.syuko.emicreatecompat.mixin;

import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.bom.MaterialNode;
import dev.emi.emi.runtime.EmiDrawContext;
import fr.syuko.emicreatecompat.emi.bom.ExpectedCostTooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(targets = "dev.emi.emi.screen.BoMScreen$Hover", remap = false)
public abstract class BoMScreenHoverMixin {

    @Shadow
    public EmiIngredient stack;

    @Shadow
    public MaterialNode node;

    @Redirect(method = "drawTooltip", at = @At(value = "INVOKE", target = "Ldev/emi/emi/EmiRenderHelper;drawTooltip(Lnet/minecraft/client/gui/screens/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;II)V"))
    private void emicreatecompat$appendExpectedCost(Screen screen,
                                                    EmiDrawContext context,
                                                    List<ClientTooltipComponent> components,
                                                    int x,
                                                    int y) {
        if (node == null) {
            ExpectedCostTooltip.append(components, stack);
        }
        EmiRenderHelper.drawTooltip(screen, context, components, x, y);
    }
}
