package fr.syuko.emicreatecompat.emi.bom;

public final class ExpectedAmounts {

    private ExpectedAmounts() {
    }

    public static long snappedToWholeCrafts(long amount,
                                            long amountPerCraft,
                                            float multiplier,
                                            double extraCraftThreshold) {
        if (amount <= 0 || multiplier <= 1f) {
            return amount;
        }

        long perCraft = amountPerCraft > 0
                        ? amountPerCraft
                        : 1;
        long crafts = (long) Math.ceil(amount / (double) perCraft);
        long scaledCrafts = (long) Math.ceil(crafts * (double) multiplier - extraCraftThreshold);

        return Math.max(crafts, scaledCrafts) * perCraft;
    }
}
