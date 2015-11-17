package com.nerdsnulls.youngsdiary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CommonMethod {
    private CommonMethod() {
    }

    public static String convertWeekDays(String date) {
        String formattedDate = null;
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd");
            Date date12 = originalFormat.parse(date);
            formattedDate = targetFormat.format(date12);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formattedDate;

    }

    public static String convertWeekDaysMonth(String date) {
        String formattedDate = null;
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy, MMM dd");
            Date date12 = originalFormat.parse(date);
            formattedDate = targetFormat.format(date12);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formattedDate;

    }

    public boolean checkDateWithInRange(Date startDate, Date endDate, Date selectedDate) {
        Calendar start = setDefaultDate(startDate, 0, 0);
        Date newStartDate = start.getTime();
        Calendar end = setDefaultDate(endDate, 23, 59);
        Date newEndDate = end.getTime();
        return (selectedDate.after(newStartDate) && selectedDate.before(newEndDate));
    }

    public static CommonMethod getInstance() {
        return new CommonMethod();
    }

    public Calendar setDefaultDate(Date date, int value, int value2) {
        Calendar end = Calendar.getInstance();
        end.setTime(date);
        end.set(Calendar.HOUR_OF_DAY, value);
        end.set(Calendar.MINUTE, value2);
        end.set(Calendar.SECOND, value2);
        return end;
    }

    public String getDateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return dateFormat.format(date);
    }

    public Date getStringToDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.parse(date);
    }

    public String convertIntArrayToString(int[] list) {
        if (list == null){
            return null;
        }
        String text = "";
        for (int each : list){
            if (each == list[list.length-1]){
                text += each;
            } else {
                text += each + ", ";
            }
        }
        return text;
    }

    public int[] convertStringToIntArray(String str){
        if (str == null){
            return null;
        }
        String[] split = str.split(", ");
        int[] result = new int[split.length];
        for (int i = 0 ; i < split.length ; i++){
            result[i] = Integer.parseInt(split[i]);
        }
        return result;
    }

}