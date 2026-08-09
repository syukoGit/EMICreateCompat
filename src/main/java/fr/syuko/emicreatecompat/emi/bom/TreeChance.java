package fr.syuko.emicreatecompat.emi.bom;

public final class TreeChance {

    private static boolean present = false;

    private TreeChance() {
    }

    public static void reset() {
        present = false;
    }

    public static void observe(float chance) {
        if (chance != 1.0f) {
            present = true;
        }
    }

    public static boolean present() {
        return present;
    }
}
