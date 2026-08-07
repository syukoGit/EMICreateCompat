package fr.syuko.emicreatecompat.emi.bom;

import dev.emi.emi.bom.ChanceState;
import dev.emi.emi.bom.MaterialNode;
import fr.syuko.emicreatecompat.config.Config;

public final class ChancedProduction {

    private ChancedProduction() {
    }

    public static ChanceState snappedToWholeAttempts(ChanceState chance,
                                                     float produceChance,
                                                     MaterialNode node,
                                                     long amount) {
        ChanceState produced = chance.produce(produceChance);
        if (!produced.chanced()) {
            return produced;
        }

        long divisor = node.divisor > 0
                       ? node.divisor
                       : 1;
        long batches = (long) Math.ceil(amount / (double) divisor);
        if (batches <= 0) {
            return produced;
        }

        long attempts = ExpectedAmounts.snappedAttempts(batches, produced.chance(), Config.extraCraftThreshold);

        return new ChanceState((float) (attempts / (double) batches), true);
    }
}
