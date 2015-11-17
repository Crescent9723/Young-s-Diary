package data;

import java.util.Date;

/**
 * Created by Justin on 8/27/2015.
 */
public class TodoData {
    private int todoID;
    private String text;
    private Date date;
    public TodoData(int todoID, String text, Date date) {
        this.todoID = todoID;
        this.text = text;
        this.date = date;
    }

    public int getTodoID() {
        return todoID;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }
}
