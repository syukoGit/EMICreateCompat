package fr.syuko.emicreatecompat.emi.bom;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.bom.ChanceState;
import dev.emi.emi.bom.MaterialNode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public final class ChancedNodeText {

    private ChancedNodeText() {
    }

    public static Component amountText(MaterialNode node, long amount, ChanceState chance) {
        long snapped = ChancedCosts.nodeAmount(node, amount, chance);

        return EmiPort.append(EmiPort.literal("≈"), EmiRenderHelper.getAmountText(node.ingredient, snapped))
                      .withStyle(ChatFormatting.GOLD);
    }
}
