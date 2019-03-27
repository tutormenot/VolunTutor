package com.example.voluntutor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//This class represents a tutor in the VolunTutor Application
//We should probably consider changing timeSlots to Strings (ie "Monday,2:00 p.m.)
//We should also start adding sorting methods as private methods and automatically call them from
//other methods
//Should we have a "don't verify" method? To let student or tutor say that the session actually didn't
//happen
public class Tutor {
    //data
    private String name;
    private String school;
    private ArrayList<Sessions> psessions;
    private ArrayList<Sessions> usessions;
    private ArrayList<Sessions> vsessions;
    private ArrayList<Date> timeSlots;
    private ArrayList<String> subjects;
    //constructor(s)
    public Tutor(String n, String s) {
        name = n;
        school = s;
        psessions = new ArrayList<Sessions>();
        usessions = new ArrayList<Sessions>();
        vsessions = new ArrayList<Sessions>();
        subjects = new ArrayList<String>();
    }
    //methods

    /**
     * Adds a subject to the list of subjects this person tutors in
     * @param s the subject to be added
     */
    public void addSubject(String s) {
        subjects.add(s.toLowerCase());
    }
    /**
     * Takes a subject out of the list of subjects
     * @param s the subject to be removed
     */
    public void removeSubject(String s) {
        subjects.remove(s.toLowerCase());
    }
    /**
     * Gets the name of the tutor.
     * @return String representation of the tutor's name
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the tutor.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the school of the tutor.
     * @return String representation of school.
     */
    public String getSchool() {
        return school;
    }
    /**
     * Sets the name of the tutor.
     */
    public void setSchool(String school) {
        this.school = school;
    }
    /**
     * Gets the pending sessions of the tutor.
     * @return ArrayList of Sessions that are pending
     */
    public ArrayList<Sessions> getPSessions() {
        return psessions;
    }

    /**
     * Checks through the upcoming sessions to see if they have already passed. If they have
     * the session is moved to pending
     */
    public void checkUpcoming() {
        Calendar cal = Calendar.getInstance();
        for(int i = 0; i < usessions.size(); i++) {
            if(usessions.get(i).getDate().before(cal.getTime())) {
                Sessions s = usessions.get(i);
                psessions.add(s);
                usessions.remove(i);
                i--;
            }
        }
    }
    /**
     * If any of the pending sessions have been fully verified, this method will move them to
     * the vsessions arraylist
     */
    public void checkPending() {
        for(int i = 0; i < psessions.size(); i++) {
            if(psessions.get(i).getVerified()) {
                Sessions s = psessions.get(i);
                psessions.remove(i);
                vsessions.add(s);
                i--;
            }
        }
    }
    /**
     * Gets the sessions that are upcoming
     * @return the sessions in an arraylist
     */
    public ArrayList<Sessions> getUsessions() {
        return usessions;
    }

    /**
     * Gets the sessions that are already verified
     * @return the sessions that are verified in an arraylist
     */
    public ArrayList<Sessions> getVsessions() {
        return vsessions;
    }
    /**
     * Adds a session to upcoming sessions
     * @param s the session
     */
    public void addSession(Sessions s) {
        usessions.add(s);
    }
    /**
     * Gets the Time Slots of the tutor.
     * @return ArrayList of Dates the tutor is available
     */
    public ArrayList<Date> getTimeSlots() {
        return timeSlots;
    }
    /**
     * Adds a time slot during which the tutor is available
     */
    public void addTimeSlots(Date t) {
        timeSlots.add(t);
    }
    /**
     * Gets the subjects the tutor tutors in
     * @return the subjects the tutor tutors in
     */
    public ArrayList<String> getSubjects() {
        return subjects;
    }
}
