package com.example.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String PREFS_NAME = "student_registration";

    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GRADE = "grade";
    private static final String KEY_MAJOR = "major";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_TERMS = "terms";

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;

    private RadioGroup gradeRadioGroup;
    private Spinner majorSpinner;

    private CheckBox notificationsCheckBox;
    private CheckBox termsCheckBox;

    private TextView resultTextView;
    private TextView gradeErrorTextView;
    private TextView majorErrorTextView;
    private TextView termsErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // קישור בין רכיבי המסך לקוד Java
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);

        gradeRadioGroup = findViewById(R.id.gradeRadioGroup);
        majorSpinner = findViewById(R.id.majorSpinner);

        notificationsCheckBox =
                findViewById(R.id.notificationsCheckBox);

        termsCheckBox =
                findViewById(R.id.termsCheckBox);

        resultTextView = findViewById(R.id.resultTextView);
        gradeErrorTextView = findViewById(R.id.gradeErrorTextView);
        majorErrorTextView = findViewById(R.id.majorErrorTextView);
        termsErrorTextView = findViewById(R.id.termsErrorTextView);

        Button submitButton = findViewById(R.id.submitButton);

        // טעינת נתונים שנשמרו בהפעלה קודמת
        Student savedStudent = loadStudent();

        if (savedStudent != null) {
            resultTextView.setText(formatStudent(savedStudent, true));
            resultTextView.setVisibility(View.VISIBLE);
        } else {
            resultTextView.setVisibility(View.GONE);
        }

        // פעולה שמתבצעת בלחיצה על כפתור ההרשמה
        submitButton.setOnClickListener(v -> registerStudent());
    }

    private void registerStudent() {

        clearErrors();

        boolean valid = true;

        // קריאת הנתונים שהמשתמש הזין
        String firstName =
                firstNameEditText.getText().toString().trim();

        String lastName =
                lastNameEditText.getText().toString().trim();

        String email =
                emailEditText.getText().toString().trim();

        // בדיקת שם פרטי
        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError(
                    getString(R.string.error_first_name)
            );

            valid = false;
        }

        // בדיקת שם משפחה
        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError(
                    getString(R.string.error_last_name)
            );

            valid = false;
        }

        // בדיקת כתובת אימייל
        if (TextUtils.isEmpty(email)) {

            emailEditText.setError(
                    getString(R.string.error_email_required)
            );

            valid = false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            emailEditText.setError(
                    getString(R.string.error_email_invalid)
            );

            valid = false;
        }

        // בדיקת בחירת כיתה
        int selectedGradeId =
                gradeRadioGroup.getCheckedRadioButtonId();

        String grade = "";

        if (selectedGradeId == -1) {

            gradeErrorTextView.setVisibility(View.VISIBLE);

            valid = false;

        } else {

            RadioButton selectedGrade =
                    findViewById(selectedGradeId);

            grade = selectedGrade.getText().toString();
        }

        // בדיקת בחירת מגמה
        int majorPosition =
                majorSpinner.getSelectedItemPosition();

        String major = "";

        if (majorPosition <= 0) {

            majorErrorTextView.setVisibility(View.VISIBLE);

            valid = false;

        } else {

            major = majorSpinner.getSelectedItem().toString();
        }

        // בדיקת אישור תנאי השימוש
        if (!termsCheckBox.isChecked()) {

            termsErrorTextView.setVisibility(View.VISIBLE);

            valid = false;
        }

        // אם קיימת שגיאה, לא שומרים את הנתונים ולא מציגים
        // את הפרטים שהוזנו בטופס הנוכחי
        if (!valid) {
            return;
        }

        // יצירת אובייקט תלמיד לאחר שכל הבדיקות עברו
        Student student = new Student(
                firstName,
                lastName,
                email,
                grade,
                major,
                notificationsCheckBox.isChecked(),
                termsCheckBox.isChecked()
        );

        // שמירת הנתונים
        saveStudent(student);

        // הצגת פרטי התלמיד
        resultTextView.setText(formatStudent(student, false));
        resultTextView.setVisibility(View.VISIBLE);
    }

    private void clearErrors() {

        firstNameEditText.setError(null);
        lastNameEditText.setError(null);
        emailEditText.setError(null);

        gradeErrorTextView.setVisibility(View.GONE);
        majorErrorTextView.setVisibility(View.GONE);
        termsErrorTextView.setVisibility(View.GONE);
    }

    private void saveStudent(Student student) {

        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        prefs.edit()
                .putString(
                        KEY_FIRST_NAME,
                        student.getFirstName()
                )
                .putString(
                        KEY_LAST_NAME,
                        student.getLastName()
                )
                .putString(
                        KEY_EMAIL,
                        student.getEmail()
                )
                .putString(
                        KEY_GRADE,
                        student.getGrade()
                )
                .putString(
                        KEY_MAJOR,
                        student.getMajor()
                )
                .putBoolean(
                        KEY_NOTIFICATIONS,
                        student.isNotificationsEnabled()
                )
                .putBoolean(
                        KEY_TERMS,
                        student.isTermsAccepted()
                )
                .apply();
    }

    private Student loadStudent() {

        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // אם אין נתונים שמורים, לא מחזירים תלמיד
        if (!prefs.contains(KEY_FIRST_NAME)) {
            return null;
        }

        // טעינת כל הנתונים ובניית אובייקט תלמיד
        return new Student(
                prefs.getString(KEY_FIRST_NAME, ""),
                prefs.getString(KEY_LAST_NAME, ""),
                prefs.getString(KEY_EMAIL, ""),
                prefs.getString(KEY_GRADE, ""),
                prefs.getString(KEY_MAJOR, ""),
                prefs.getBoolean(KEY_NOTIFICATIONS, false),
                prefs.getBoolean(KEY_TERMS, false)
        );
    }

    private String formatStudent(Student student, boolean saved) {

        String heading;

        if (saved) {
            heading = getString(R.string.saved_title);
        } else {
            heading = getString(R.string.result_title);
        }

        String notifications;

        if (student.isNotificationsEnabled()) {
            notifications =
                    getString(R.string.result_notifications_yes);
        } else {
            notifications =
                    getString(R.string.result_notifications_no);
        }

        // בניית התוצאה באמצעות מחרוזות מקובץ strings.xml
        return heading + "\n\n"
                + getString(
                R.string.result_first_name,
                student.getFirstName()
        ) + "\n"
                + getString(
                R.string.result_last_name,
                student.getLastName()
        ) + "\n"
                + getString(
                R.string.result_email,
                student.getEmail()
        ) + "\n"
                + getString(
                R.string.result_grade,
                student.getGrade()
        ) + "\n"
                + getString(
                R.string.result_major,
                student.getMajor()
        ) + "\n"
                + notifications + "\n"
                + getString(R.string.result_terms);
    }
}