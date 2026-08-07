package fr.syuko.emicreatecompat.create.stock;

public enum StockAvailability {
    NOT_BOUND,
    WRONG_DIMENSION,
    OUT_OF_RANGE,
    STALE,
    LIVE;

    public boolean reachable() {
        return this == STALE || this == LIVE;
    }
}
