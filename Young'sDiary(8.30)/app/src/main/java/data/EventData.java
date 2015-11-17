package data;

import java.util.Date;

/**
 * Created by Justin on 8/18/2015.
 */
public class EventData {
    private int eventID;
    private String title;
    private String description;
    private String tag;
    private Date startDate;
    private Date endDate;
    private int icon;
    private RepeatData repeatData;

    public EventData(int eventID, String title, String description, String tag, Date startDate, Date endDate, int icon) {
        this.title = title;
        this.eventID = eventID;
        this.description = description;
        this.tag = tag;
        this.startDate = startDate;
        this.endDate = endDate;
        this.icon = icon;
    }

    public Date getEndDate() {
        return endDate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public String getTag() {
        return tag;
    }
    public int getEventID() {
        return eventID;
    }
    public int getIcon() {
        return icon;
    }
    public RepeatData getRepeatData() {
        return repeatData;
    }
    public void setRepeatData(RepeatData repeatData) {
        this.repeatData = repeatData;
    }
}

