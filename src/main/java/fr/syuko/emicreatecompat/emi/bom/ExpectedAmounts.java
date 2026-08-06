package fr.syuko.emicreatecompat.emi.bom;

public final class ExpectedAmounts {

    private ExpectedAmounts() {
    }

    public static long expected(long amount, long amountPerCraft, float multiplier) {
        if (amount <= 0) {
            return amount;
        }

        return Math.max(Math.max(amountPerCraft, 0L), Math.round(amount * (double) multiplier));
    }

    public static long snappedAttempts(long batches, float multiplier, double extraCraftThreshold) {
        if (batches <= 0 || multiplier <= 1f) {
            return batches;
        }

        return Math.max(batches, (long) Math.ceil(batches * (double) multiplier - extraCraftThreshold));
    }
}
