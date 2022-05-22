package antifraud.model.transaction;

public final class Limits {

    private static Long MANUAL_PROCESSING_LIMIT = 200L;

    private static Long PROHIBITED_LIMIT = 1500L;

    public synchronized static void upgradeManualProcessingLimit(Long amount) {
        MANUAL_PROCESSING_LIMIT = (long) Math.ceil(0.8 * MANUAL_PROCESSING_LIMIT + 0.2 * amount);
    }

    public synchronized static void downgradManualProcessingLimit(Long amount) {
        MANUAL_PROCESSING_LIMIT = (long) Math.ceil(0.8 * MANUAL_PROCESSING_LIMIT - 0.2 * amount);
    }

    public synchronized static void upgradeProhibitedLimit(Long amount) {
        PROHIBITED_LIMIT = (long) Math.ceil(0.8 * PROHIBITED_LIMIT + 0.2 * amount);
    }

    public synchronized static void downgradProhibitedLimit(Long amount) {
        PROHIBITED_LIMIT = (long) Math.ceil(0.8 * PROHIBITED_LIMIT - 0.2 * amount);
    }

    public synchronized static Long getLimitManualProcessing() {
        return MANUAL_PROCESSING_LIMIT;
    }

    public synchronized static Long getLimitProhibited() {
        return PROHIBITED_LIMIT;
    }

}
