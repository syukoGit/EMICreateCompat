package fr.syuko.emicreatecompat.emi.bom;

import fr.syuko.emicreatecompat.config.ChanceAmounts;
import fr.syuko.emicreatecompat.config.Config;

public final class BomAmountMode {

    private static boolean measuringExpectedCosts = false;

    private BomAmountMode() {
    }

    public static boolean rawAmounts() {
        return !measuringExpectedCosts && Config.chanceAmounts == ChanceAmounts.RAW;
    }

    public static void whileMeasuringExpectedCosts(Runnable measurement) {
        measuringExpectedCosts = true;
        try {
            measurement.run();
        } finally {
            measuringExpectedCosts = false;
        }
    }
}
