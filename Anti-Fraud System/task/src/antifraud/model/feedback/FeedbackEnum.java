package antifraud.model.feedback;

import java.util.EnumSet;
import java.util.Set;

public enum FeedbackEnum {
    NONE(""),
    ALLOWED("ALLOWED"),
    MANUAL_PROCESSING("MANUAL_PROCESSING"),
    PROHIBITED("PROHIBITED");

    private String text;

    FeedbackEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Set<FeedbackEnum> validFeedback() {
        return EnumSet.of(ALLOWED, MANUAL_PROCESSING, PROHIBITED);
    }
}
