package fr.syuko.emicreatecompat.emi.bom;

import dev.emi.emi.bom.MaterialNode;
import fr.syuko.emicreatecompat.config.Config;

public final class ChancedFavorites {

    private ChancedFavorites() {
    }

    public static long neededFor(MaterialNode node) {
        if (!Config.alignChancedFavorites) {
            return node.totalNeeded;
        }

        return ChancedCosts.favoriteAmount(node);
    }
}
