package fr.syuko.emicreatecompat.emi.bom;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class ExpectedCostTooltip {

    private ExpectedCostTooltip() {
    }

    public static void append(List<ClientTooltipComponent> components, EmiIngredient ingredient) {
        if (!BomAmountMode.rawAmounts() || ingredient == null) {
            return;
        }

        Long expected = ExpectedCostIndex.expectedFor(ingredient);
        if (expected == null) {
            return;
        }

        components.add(ClientTooltipComponent.create(EmiPort.ordered(Component.translatable(
                "tooltip.emicreatecompat.expected_mode_cost",
                expected).withStyle(ChatFormatting.GOLD))));
    }
}
