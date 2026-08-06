package fr.syuko.emicreatecompat.emi.bom;

import dev.emi.emi.bom.ChanceMaterialCost;
import dev.emi.emi.bom.ChanceState;
import dev.emi.emi.bom.MaterialNode;
import fr.syuko.emicreatecompat.config.Config;

public final class ChancedCosts {

    private ChancedCosts() {
    }

    public static long effectiveAmount(ChanceMaterialCost cost) {
        return ExpectedAmounts.snappedToWholeCrafts(cost.amount,
                                                    cost.minBatch,
                                                    cost.chance,
                                                    Config.extraCraftThreshold);
    }

    public static long nodeAmount(MaterialNode node, long amount, ChanceState chance) {
        return ExpectedAmounts.snappedToWholeCrafts(amount, node.amount, chance.chance(), Config.extraCraftThreshold);
    }

    public static long favoriteAmount(MaterialNode node) {
        return ExpectedAmounts.snappedToWholeCrafts(node.totalNeeded,
                                                    node.amount,
                                                    ((ChancedNode) node).emicreatecompat$chanceMultiplier(),
                                                    Config.extraCraftThreshold);
    }
}
