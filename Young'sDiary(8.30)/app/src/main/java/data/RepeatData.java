package data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Justin on 9/8/2015.
 */
public class RepeatData implements Serializable {
    private Date startDate;
    private Date endDate;
    private RepeatType repeatType;

    // For Daily
    private int duration;

    // For Weekly
    private int[] weekdayList;

    // For Monthly
    private int[] dayList;

    // For Yearly
    private int month;
    private int day;

    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }
    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
    public Date getEndDate() {
        return endDate;
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public int getDuration() {
        return duration;
    }

    public int[] getWeekdayList() {
        return weekdayList;
    }

    public int[] getDayList() {
        return dayList;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public static class Builder {
        // Required
        private Date startDate;
        private Date endDate;
        private RepeatType repeatType;

        // Optional
        private int duration = Integer.MAX_VALUE;

        private int[] weekdayList = null;

        private int[] dayList = null;

        private int month = Integer.MAX_VALUE;
        private int day = Integer.MAX_VALUE;

        public Builder(Date startDate, Date endDate, RepeatType repeatType){
            this.startDate = startDate;
            this.endDate = endDate;
            this.repeatType = repeatType;
        }

        public Builder duration(int val){
            duration = val;
            return this;
        }
        public Builder weekdayList(int[] list){
            weekdayList = list;
            return this;
        }
        public Builder dayList(int[] list){
            dayList = list;
            return this;
        }
        public Builder month(int val){
            month = val;
            return this;
        }
        public Builder day(int val){
            day = val;
            return this;
        }

        public RepeatData build(){
            return new RepeatData(this);
        }
    }
    RepeatData(Builder builder){
        startDate = builder.startDate;
        endDate = builder.endDate;
        repeatType = builder.repeatType;
        duration = builder.duration;
        weekdayList = builder.weekdayList;
        dayList = builder.dayList;
        month = builder.month;
        day = builder.day;
    }
}
