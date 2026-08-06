package fr.syuko.emicreatecompat.emi.bom;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.bom.BoM;
import dev.emi.emi.bom.ChanceMaterialCost;
import dev.emi.emi.bom.FlatMaterialCost;

import java.util.HashMap;
import java.util.Map;

public final class ExpectedCostIndex {

    private static final Map<EmiIngredient, Long> EXPECTED_BY_INGREDIENT = new HashMap<>();

    private ExpectedCostIndex() {
    }

    public static void rebuild() {
        EXPECTED_BY_INGREDIENT.clear();
        if (!BomAmountMode.rawAmounts() || BoM.tree == null) {
            return;
        }

        BomAmountMode.whileMeasuringExpectedCosts(() -> {
            BoM.tree.calculateCost();
            index();
        });
    }

    public static void clear() {
        EXPECTED_BY_INGREDIENT.clear();
    }

    public static Long expectedFor(EmiIngredient ingredient) {
        return EXPECTED_BY_INGREDIENT.get(ingredient);
    }

    private static void index() {
        Map<EmiIngredient, Long> rawTotals = new HashMap<>();
        Map<EmiIngredient, Long> expectedTotals = new HashMap<>();

        for (FlatMaterialCost cost : BoM.tree.cost.costs.values()) {
            rawTotals.merge(cost.ingredient, cost.amount, Long::sum);
            expectedTotals.merge(cost.ingredient, cost.amount, Long::sum);
        }

        for (ChanceMaterialCost cost : BoM.tree.cost.chanceCosts.values()) {
            rawTotals.merge(cost.ingredient, cost.amount, Long::sum);
            expectedTotals.merge(cost.ingredient, cost.getEffectiveAmount(), Long::sum);
        }

        for (Map.Entry<EmiIngredient, Long> entry : expectedTotals.entrySet()) {
            if (!entry.getValue().equals(rawTotals.get(entry.getKey()))) {
                EXPECTED_BY_INGREDIENT.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
