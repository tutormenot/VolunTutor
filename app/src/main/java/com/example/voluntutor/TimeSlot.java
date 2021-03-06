package com.example.voluntutor;

/**
 * This class models time slots that the tutor chooses to say they are available during.
 */

import android.os.Parcel;
import android.os.Parcelable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeSlot implements Comparable<TimeSlot>, Parcelable {
    private String dayOfWeek;
    /**
     * On a 24-hour system
     */
    private int sHour;
    private int sMinute;
    private int eHour;
    private int eMinute;
    //constructor(s)

    /**
     * Constructs a TimeSlot object with provided parameters
     * @param d Day (String object)
     * @param sh Start Hour (int)
     * @param sm Start Minute (int)
     * @param eh End Hour (int)
     * @param em End Minute (int)
     */
    public TimeSlot(String d, int sh, int sm, int eh, int em) {
        dayOfWeek = d;
        sHour = sh;
        sMinute = sm;
        eHour = eh;
        eMinute = em;
    }

    /**
     * Default constructor for the TimeSlot class without any entered parameters
     */
    public TimeSlot() {
        dayOfWeek = "";
        sHour = 0;
        sMinute = 0;
        eHour = 0;
        eMinute = 0;
    }
    //methods
    /**
     * This takes in a session to see if the session fits within the time slot and checks if it's after right now
     * @param s the session
     * @return whether the session is in the time slot
     */
    public boolean isValid(Sessions s) {
        Date d = new Date(Long.parseLong(s.getDate()));

        SimpleDateFormat df = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        String dow = df.format(d);
        boolean onDay = (dow.equals(dayOfWeek));

        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, sHour);
        c.set(Calendar.MINUTE, sMinute);
        Date startSlot = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, eHour);
        c.set(Calendar.MINUTE, eMinute);
        Date endSlot = c.getTime();

        c.setTime(d);
        c.add(Calendar.MINUTE, s.getLength());
        Date endSession = c.getTime();

        boolean startWithin = (d.after(startSlot) || d.equals(startSlot));
        boolean endWithin = (endSession.before(endSlot) || endSession.equals(endSlot));
        boolean afterNow = !d.before(now);

        return onDay && startWithin && endWithin && afterNow;
    }

    /**
     * Returns the day of the week of the slot (as a string)
     * @return day of the week of the slot
     */
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    /**
     * Returns the hour that the slot begins
     * @return the start hour
     */
    public int getsHour() {
        return sHour;
    }
    /**
     * Returns the minute that the slot begins
     * @return the start minute
     */
    public int getsMinute() {
        return sMinute;
    }
    /**
     * Returns the hour that the slot ends
     * @return the end hour
     */
    public int geteHour() {
        return eHour;
    }
    /**
     * Returns the minute that the slot ends
     * @return the end minute
     */
    public int geteMinute() {
        return eMinute;
    }
    /**
     * Creates a string from the slot (ie Monday from 2:30 to 3:30)
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("00");
        return dayOfWeek + " from " + sHour + ":" + df.format(sMinute) + " to " + eHour + ":" + df.format(eMinute);
    }
    /**
     * Allows ArrayLists of time slots to be sorted based on time
     */
    @Override
    public int compareTo(TimeSlot o) {
        int hourCompare = Integer.compare(o.getsHour(), sHour);
        if(hourCompare != 0) {
            return -1*hourCompare;
        }
        return -1*Integer.compare(o.getsMinute(), sMinute);
    }

    /**
     * Equals method for two time slots
     * @param ts the other time slot
     * @return if the two slots are the same
     */
    public boolean equals(TimeSlot ts) {
        return dayOfWeek.equals(ts.getDayOfWeek()) && sHour == ts.getsHour() && sMinute == ts.getsMinute() && eHour == ts.geteHour()
                && eMinute == ts.geteMinute();
    }

    /**
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the information about the time slot to a parcel
     * @param dest the parcel
     * @param flags flags that may be passed in
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {dayOfWeek, Integer.toString(sHour), Integer.toString(sMinute),
        Integer.toString(eHour), Integer.toString(eMinute)});
    }

    /**
     * Tells the parcel how to create an object of class TimeSlot
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        /**
         * Takes the parcel and unwraps it to make the TimeSlot
         * @param in the parcel
         * @return a TimeSlot
         */
        public TimeSlot createFromParcel(Parcel in) {
            String[] s = in.createStringArray();
            TimeSlot toReturn = new TimeSlot(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4]));
            return toReturn;
        }

        /**
         * Makes an arraylist of TimeSlots
         * @param size the size of the array
         * @return
         */
        public TimeSlot[] newArray(int size) {
            return new TimeSlot[size];
        }
    };
}
