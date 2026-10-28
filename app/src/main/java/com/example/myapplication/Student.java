
package com.example.myapplication;
public class Student {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String grade;
    private final String major;
    private final boolean notificationsEnabled;
    private final boolean termsAccepted;

    public Student(
            String firstName,
            String lastName,
            String email,
            String grade,
            String major,
            boolean notificationsEnabled,
            boolean termsAccepted) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.grade = grade;
        this.major = major;
        this.notificationsEnabled = notificationsEnabled;
        this.termsAccepted = termsAccepted;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGrade() {
        return grade;
    }

    public String getMajor() {
        return major;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public boolean isTermsAccepted() {
        return termsAccepted;
    }
}