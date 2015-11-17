package data;

/**
 * Created by Justin on 9/8/2015.
 */
public enum RepeatType {
    NONE("NONE"), DAILY("DAILY"), WEEKLY("WEEKLY"), MONTHLY("MONTHLY"), YEARLY("YEARLY");
    private String text;
    RepeatType(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
