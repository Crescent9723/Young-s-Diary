package data;

import java.util.Date;

/**
 * Created by Justin on 8/27/2015.
 */
public class NoteData {
    private int noteID;
    private String text;
    private Date date;
    public NoteData(int noteID, String text, Date date) {
        this.noteID = noteID;
        this.text = text;
        this.date = date;
    }

    public int getNoteID() {
        return noteID;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }
}
