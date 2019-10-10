package com.dicoding.picodiploma.mybottomnavigation.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;

import com.dicoding.picodiploma.mybottomnavigation.NotificationReceiver;
import com.dicoding.picodiploma.mybottomnavigation.R;

import java.util.Calendar;
import java.util.Objects;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    public static String ACTION1 = "Notification1";
    public static String ACTION2 = "Notification2";
    public static int REQUEST_CODE = 100;
    private final String RELEASE_REMINDER = "release_reminder";
    private final String DAILY_REMINDER = "daily_reminder";
    private Switch switchReleaseReminder, switchDailyReminder;
    private SharedPreferences sharedPref;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        switchDailyReminder = findViewById(R.id.switch_daily_reminder);
        switchReleaseReminder = findViewById(R.id.switch_release_reminder);
        mIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switchReleaseReminder.setOnClickListener(this);
        switchDailyReminder.setOnClickListener(this);

        sharedPref = getApplicationContext().getSharedPreferences("Release", Context.MODE_PRIVATE);
        boolean releaseReminder = sharedPref.getBoolean(RELEASE_REMINDER, false);
        boolean dailyReminder = sharedPref.getBoolean(DAILY_REMINDER, false);
        if (releaseReminder) {
            switchReleaseReminder.setChecked(true);
        } else {
            switchReleaseReminder.setChecked(false);
        }

        if (dailyReminder) {
            switchDailyReminder.setChecked(true);
        } else {
            switchDailyReminder.setChecked(false);
        }

    }

    @Override
    public void onClick(View v) {
        PendingIntent pendingIntent;
        AlarmManager alarmManager;
        if (v.getId() == R.id.switch_release_reminder) {
            Calendar calendar = Calendar.getInstance();
            mIntent.setAction(ACTION2);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (switchReleaseReminder.isChecked()) {

                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(RELEASE_REMINDER, true);
                editor.apply();
            } else {
                calendar.clear();
                alarmManager.cancel(pendingIntent);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(RELEASE_REMINDER, false);
                editor.apply();
            }
        } else if (v.getId() == R.id.switch_daily_reminder) {

            Calendar calendar = Calendar.getInstance();
            mIntent.setAction(ACTION1);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (switchDailyReminder.isChecked()) {

                calendar.set(Calendar.HOUR_OF_DAY, 7);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(DAILY_REMINDER, true);
                editor.apply();
            } else {
                calendar.clear();
                alarmManager.cancel(pendingIntent);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(DAILY_REMINDER, false);
                editor.apply();
            }
        }
    }
}
