package fr.syuko.emicreatecompat.emi.bom;

public final class ExpectedAmounts {

    private ExpectedAmounts() {
    }

    public static long scaled(long amount, float multiplier) {
        if (amount <= 0 || multiplier <= 1f) {
            return amount;
        }

        return Math.max(amount, Math.round((double) amount * multiplier));
    }
}
