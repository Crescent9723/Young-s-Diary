package sqlDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nerdsnulls.youngsdiary.CommonMethod;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import data.EventData;
import data.RepeatData;
import data.RepeatType;

/**
 * Created by Justin on 8/17/2015.
 */
public class EventSQLiteHandler {
    EventSQLiteOpenHelper helper;
    SQLiteDatabase db;
    private Calendar startDateCal = Calendar.getInstance();
    private Calendar endDateCal = Calendar.getInstance();
    private Calendar selectedDate = Calendar.getInstance();
    private Calendar selectedEndDate = Calendar.getInstance();


    public EventSQLiteHandler(Context ctx){
        helper = new EventSQLiteOpenHelper(ctx, "event.sqlite", null, 1);
    }

    public static EventSQLiteHandler open(Context ctx){
        return new EventSQLiteHandler(ctx);
    }

    public void close(){
       helper.close();
    }

    public void insert(String userID, String title, Date start, Date end, String description, String tag, int icon,
                       RepeatType repeatType, int duration, int[] weekdayList, int[] dayList, int month, int day){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userID", userID);
        values.put("title", title);
        values.put("startDate", CommonMethod.getInstance().getDateToString(start));
        values.put("endDate", CommonMethod.getInstance().getDateToString(end));
        values.put("description", description);
        values.put("tag", tag);
        values.put("icon", icon);
        values.put("repeatType", repeatType.getText());
        values.put("duration", duration);
        values.put("weekdayList", CommonMethod.getInstance().convertIntArrayToString(weekdayList));
        values.put("dayList", CommonMethod.getInstance().convertIntArrayToString(dayList));
        values.put("month", month);
        values.put("day", day);

        db.insert("event", null, values);
    }

    public void update(int eventID, String userID, String title, Date start, Date end, String description, String tag, int icon,
                       RepeatType repeatType, int duration, int[] weekdayList, int[] dayList, int month, int day){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userID", userID);
        values.put("title", title);
        values.put("startDate", CommonMethod.getInstance().getDateToString(start));
        values.put("endDate", CommonMethod.getInstance().getDateToString(end));
        values.put("description", description);
        values.put("tag", tag);
        values.put("icon", icon);
        values.put("repeatType", repeatType.getText());
        values.put("duration", duration);
        values.put("weekdayList", CommonMethod.getInstance().convertIntArrayToString(weekdayList));
        values.put("dayList", CommonMethod.getInstance().convertIntArrayToString(dayList));
        values.put("month", month);
        values.put("day", day);

        db.update("event", values, "eventID = ?", new String[]{Integer.toString(eventID)});
    }

    public void delete(int eventID){
        db = helper.getWritableDatabase();
        db.delete("event", "eventID = ?", new String[]{Integer.toString(eventID)});
    }

    public void clearTable(){
        db = helper.getWritableDatabase();
        db.delete("event", null, null);
    }

    public ArrayList<EventData> selectAllEventByUser(String userID) throws ParseException {
        db = helper.getReadableDatabase();
        String query = "SELECT eventID, title, startDate, endDate, description, tag, icon, repeatType, duration, " +
                "weekdayList, dayList, month, day from event where userID = '" + userID + "';";

        Cursor cursor = db.rawQuery(query, null);
        ArrayList<EventData> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Date startDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("startDate")));
            Date endDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("endDate")));
            int eventID = cursor.getInt(cursor.getColumnIndex("eventID"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));
            int icon = cursor.getInt(cursor.getColumnIndex("icon"));
            String repeatType = cursor.getString(cursor.getColumnIndex("repeatType"));
            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
            String weekdayList = cursor.getString(cursor.getColumnIndex("weekdayList"));
            String dayList = cursor.getString(cursor.getColumnIndex("dayList"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            setEventList(list, startDate, endDate, eventID, title, description, tag, icon,
                    repeatType, duration, weekdayList, dayList, month, day);
        }
        return list;
    }


    public ArrayList<EventData> selectAllEventByDate(String userID, Date date) throws ParseException {
        db = helper.getReadableDatabase();
        String query = "SELECT eventID, title, startDate, endDate, description, tag, icon, repeatType, duration, " +
                "weekdayList, dayList, month, day from event where userID = '" + userID + "';";

        Cursor cursor = db.rawQuery(query, null);
        ArrayList<EventData> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Date startDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("startDate")));
            Date endDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("endDate")));
            if (CommonMethod.getInstance().checkDateWithInRange(startDate, endDate, date)){
                int eventID = cursor.getInt(cursor.getColumnIndex("eventID"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                int icon = cursor.getInt(cursor.getColumnIndex("icon"));
                String repeatType = cursor.getString(cursor.getColumnIndex("repeatType"));
                int duration = cursor.getInt(cursor.getColumnIndex("duration"));
                String weekdayList = cursor.getString(cursor.getColumnIndex("weekdayList"));
                String dayList = cursor.getString(cursor.getColumnIndex("dayList"));
                int month = cursor.getInt(cursor.getColumnIndex("month"));
                int day = cursor.getInt(cursor.getColumnIndex("day"));
                setEventListByDate(list, date, startDate, endDate, eventID, title, description, tag, icon,
                        repeatType, duration, weekdayList, dayList, month, day);
            }
        }
        return list;
    }

    public ArrayList<EventData> selectAllEventByTag(String userID, String tagSel) throws ParseException {
        db = helper.getReadableDatabase();
        String query = "SELECT eventID, title, startDate, endDate, description, tag, icon, repeatType, duration, " +
                "weekdayList, dayList, month, day from event where userID = '" + userID + "' ORDER BY startDate ASC;";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<EventData> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int eventID = cursor.getInt(cursor.getColumnIndex("eventID"));
            Date startDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("startDate")));
            Date endDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("endDate")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));
            int icon = cursor.getInt(cursor.getColumnIndex("icon"));
            String repeatType = cursor.getString(cursor.getColumnIndex("repeatType"));
            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
            String weekdayList = cursor.getString(cursor.getColumnIndex("weekdayList"));
            String dayList = cursor.getString(cursor.getColumnIndex("dayList"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            if (tag.equals(tagSel)){
                RepeatData repeatData = new RepeatData.Builder(startDate, endDate, RepeatType.valueOf(repeatType)).dayList(CommonMethod.getInstance().convertStringToIntArray(dayList)).
                        day(day).month(month).weekdayList(CommonMethod.getInstance().convertStringToIntArray(weekdayList)).duration(duration).build();
                EventData eventData = new EventData(eventID, title, description, tag, startDate, endDate, icon);
                eventData.setRepeatData(repeatData);
                list.add(eventData);
            }
        }
        return list;
    }

    public ArrayList<EventData> selectAllEventByUserOrderByTag(String userID) throws ParseException {
        db = helper.getReadableDatabase();
        String query = "SELECT eventID, title, startDate, endDate, description, tag, icon, repeatType, duration, " +
                "weekdayList, dayList, month, day from event where userID = '" + userID + "' ORDER BY tag, startDate ASC;";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<EventData> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int eventID = cursor.getInt(cursor.getColumnIndex("eventID"));
            Date startDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("startDate")));
            Date endDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("endDate")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));
            int icon = cursor.getInt(cursor.getColumnIndex("icon"));
            String repeatType = cursor.getString(cursor.getColumnIndex("repeatType"));
            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
            String weekdayList = cursor.getString(cursor.getColumnIndex("weekdayList"));
            String dayList = cursor.getString(cursor.getColumnIndex("dayList"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            RepeatData repeatData = new RepeatData.Builder(startDate, endDate, RepeatType.valueOf(repeatType)).dayList(CommonMethod.getInstance().convertStringToIntArray(dayList)).
                    day(day).month(month).weekdayList(CommonMethod.getInstance().convertStringToIntArray(weekdayList)).duration(duration).build();
            EventData eventData = new EventData(eventID, title, description, tag, startDate, endDate, icon);
            eventData.setRepeatData(repeatData);
            list.add(eventData);
        }
        return list;
    }

    public RepeatData getRepeatDataByEventID(int eventID) throws ParseException {
        db = helper.getReadableDatabase();
        String query = "SELECT eventID, title, startDate, endDate, description, tag, icon, repeatType, duration, " +
                "weekdayList, dayList, month, day from event where eventID = " + eventID + ";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();
        Date startDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("startDate")));
        Date endDate = CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("endDate")));
        String repeatType = cursor.getString(cursor.getColumnIndex("repeatType"));
        int duration = cursor.getInt(cursor.getColumnIndex("duration"));
        String weekdayList = cursor.getString(cursor.getColumnIndex("weekdayList"));
        String dayList = cursor.getString(cursor.getColumnIndex("dayList"));
        int month = cursor.getInt(cursor.getColumnIndex("month"));
        int day = cursor.getInt(cursor.getColumnIndex("day"));
        return new RepeatData.Builder(startDate, endDate, RepeatType.valueOf(repeatType)).weekdayList(CommonMethod.getInstance().convertStringToIntArray(weekdayList)).
                day(day).dayList(CommonMethod.getInstance().convertStringToIntArray(dayList)).duration(duration).month(month).build();
    }
    private void setEventList(ArrayList<EventData> list, Date startDate, Date endDate, int eventID, String title, String description, String tag, int icon, String repeatType, int duration, String weekdayList, String dayList, int month, int day) {
        switch (RepeatType.valueOf(repeatType)){
            case NONE:
                list.add(new EventData(eventID, title, description, tag, startDate, endDate, icon));
                break;
            case YEARLY:
                selectedEndDate.setTime(endDate);
                selectedDate.setTime(startDate);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, day);
                startDateCal.setTime(startDate);
                if (selectedDate.before(startDateCal)){
                    selectedDate.add(Calendar.YEAR, 1);
                }
                endDateCal.setTime(endDate);
                while (endDateCal.after(selectedDate)) {
                    selectedEndDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                    list.add(new EventData(eventID, title, description, tag, selectedDate.getTime(), selectedEndDate.getTime(), icon));
                    selectedDate.add(Calendar.YEAR, 1);
                }
                break;
            case MONTHLY:
                int[] dayListArray = CommonMethod.getInstance().convertStringToIntArray(dayList);
                for (int eachDay : dayListArray){
                    selectedEndDate.setTime(endDate);
                    selectedDate.setTime(startDate);
                    selectedDate.set(Calendar.DAY_OF_MONTH, eachDay);
                    startDateCal.setTime(startDate);
                    if (selectedDate.before(startDateCal)){
                        selectedDate.add(Calendar.MONTH, 1);
                    }
                    endDateCal.setTime(endDate);
                    while (endDateCal.after(selectedDate)) {
                        selectedEndDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                        list.add(new EventData(eventID, title, description, tag, selectedDate.getTime(), selectedEndDate.getTime(), icon));
                        selectedDate.add(Calendar.MONTH, 1);
                    }
                }

                break;
            case WEEKLY:
                int[] weekDayListArray = CommonMethod.getInstance().convertStringToIntArray(weekdayList);
                for (int eachDay : weekDayListArray) {
                    selectedEndDate.setTime(endDate);
                    selectedDate.setTime(startDate);
                    while ((eachDay + 1) != selectedDate.get(Calendar.DAY_OF_WEEK)) {
                        selectedDate.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    startDateCal.setTime(startDate);
                    endDateCal.setTime(endDate);
                    while (endDateCal.after(selectedDate)) {
                        selectedEndDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                        list.add(new EventData(eventID, title, description, tag, selectedDate.getTime(), selectedEndDate.getTime(), icon));
                        selectedDate.add(Calendar.DAY_OF_MONTH, 7);
                    }
                }
                break;
            case DAILY:
                selectedEndDate.setTime(endDate);
                selectedDate.setTime(startDate);
                endDateCal.setTime(endDate);
                while (endDateCal.after(selectedDate)) {
                    selectedEndDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                    list.add(new EventData(eventID, title, description, tag, selectedDate.getTime(), selectedEndDate.getTime(), icon));
                    selectedDate.add(Calendar.DAY_OF_MONTH, duration);
                }
                break;
        }
    }
    private void setEventListByDate(ArrayList<EventData> list, Date date, Date startDate, Date endDate, int eventID, String title, String description, String tag, int icon, String repeatType, int duration, String weekdayList, String dayList, int month, int day) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        switch (RepeatType.valueOf(repeatType)){
            case NONE:
                list.add(new EventData(eventID, title, description, tag, startDate, endDate, icon));
                break;
            case YEARLY:
                selectedEndDate.setTime(endDate);
                selectedDate.setTime(startDate);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, day);
                startDateCal.setTime(startDate);
                if (selectedDate.before(startDate)){
                    selectedDate.add(Calendar.YEAR, 1);
                }
                endDateCal.setTime(endDate);
                while (endDateCal.after(selectedDate)) {
                    selectedEndDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                    if (currentDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) && currentDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                            && currentDate.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                        list.add(new EventData(eventID, title, description, tag, selectedDate.getTime(), selectedEndDate.getTime(), icon));
                    }
                    selectedDate.add(Calendar.YEAR, 1);
                }
                break;
            case MONTHLY:
                int[] dayListArray = CommonMethod.getInstance().convertStringToIntArray(dayList);
                for (int eachDay : dayListArray){
                    selectedEndDate.setTime(endDate);
                    selectedDate.setTime(startDate);
                    selectedDate.set(Calendar.DAY_OF_MONTH, eachDay);
                    startDateCal.setTime(startDate);
                    if (selectedDate.before(startDate)){
                        selectedDate.add(Calendar.MONTH, 1);
                    }
                    endDateCal.setTime(endDate);
                    while (endDateCal.after(selectedDate)) {
                        selectedEndDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                        if (currentDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) && currentDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                                && currentDate.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                            list.add(new EventData(eventID, title, description, tag, selectedDate.getTime(), selectedEndDate.getTime(), icon));
                        }
                        selectedDate.add(Calendar.MONTH, 1);
                    }
                }

                break;
            case WEEKLY:
                int[] weekDayListArray = CommonMethod.getInstance().convertStringToIntArray(weekdayList);
                for (int eachDay : weekDayListArray) {
                    selectedEndDate.setTime(endDate);
                    selectedDate.setTime(startDate);
                    while ((eachDay + 1) != selectedDate.get(Calendar.DAY_OF_WEEK)) {
                        selectedDate.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    startDateCal.setTime(startDate);
                    endDateCal.setTime(endDate);
                    while (endDateCal.after(selectedDate)) {
                        selectedEndDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                        if (currentDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) && currentDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                                && currentDate.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                            list.add(new EventData(eventID, title, description, tag, selectedDate.getTime(), selectedEndDate.getTime(), icon));
                        }
                        selectedDate.add(Calendar.DAY_OF_MONTH, 7);
                    }
                }
                break;
            case DAILY:
                selectedEndDate.setTime(endDate);
                selectedDate.setTime(startDate);
                endDateCal.setTime(endDate);
                while (endDateCal.after(selectedDate)) {
                    selectedEndDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                    if (currentDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) && currentDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                            && currentDate.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                        list.add(new EventData(eventID, title, description, tag, selectedDate.getTime(), selectedEndDate.getTime(), icon));
                    }
                    selectedDate.add(Calendar.DAY_OF_MONTH, duration);
                }
                break;
        }
    }

}
