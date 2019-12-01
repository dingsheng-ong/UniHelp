package my.edu.um.fsktm.unihelp.ui.reservation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Location;
import my.edu.um.fsktm.unihelp.models.Reservation;
import my.edu.um.fsktm.unihelp.ui.reservation.adapters.ReservationAdapter;
import my.edu.um.fsktm.unihelp.util.DateParser;
import my.edu.um.fsktm.unihelp.util.Message;
import my.edu.um.fsktm.unihelp.util.Preferences;

public class RoomDetailActivity extends AppCompatActivity {

    private Calendar calendar, fromTime, toTime;
    private String locationID = null;

    private View.OnClickListener updateDate(final int delta) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH, delta);
                long maxTime = Math.max(calendar.getTimeInMillis(), System.currentTimeMillis());
                calendar.setTimeInMillis(maxTime);
                updateCalendar();
            }
        };
    }

    private View.OnClickListener selectDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    long maxTime = Math.max(calendar.getTimeInMillis(), System.currentTimeMillis());
                    calendar.setTimeInMillis(maxTime);
                    updateCalendar();
                }
            };
            new DatePickerDialog(
                RoomDetailActivity.this,
                setDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        }
    };

    private View.OnClickListener selectTime(final boolean isFrom) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = (isFrom) ? fromTime : toTime;
                TimePickerDialog.OnTimeSetListener setTime = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        updateFromAndToTime();
                    }
                };
                TimePickerDialog dialog = new TimePickerDialog(
                    RoomDetailActivity.this,
                    setTime,
                    c.get(Calendar.HOUR_OF_DAY),
                    0, true
                );
                dialog.show();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_detail_layout);

        setupActionBar();
        setupAllTextView();
        updateListView();

        findViewById(R.id.detail_inc_date).setOnClickListener(updateDate(+1));
        findViewById(R.id.detail_dec_date).setOnClickListener(updateDate(-1));
        findViewById(R.id.detail_select_date).setOnClickListener(selectDate);
        findViewById(R.id.from_time).setOnClickListener(selectTime(true));
        findViewById(R.id.to_time).setOnClickListener(selectTime(false));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Room Details");
        } catch (NullPointerException e) { Log.e("Error", e.toString()); }
    }

    private void setupAllTextView() {
        Intent intent = getIntent();
        Location location = (Location) intent.getSerializableExtra("location");

        locationID = location.getLocationId();

        TextView nameTV = findViewById(R.id.detail_name);
        TextView facTV  = findViewById(R.id.detail_fac);

        nameTV.setText(location.getName());
        facTV.setText(location.getFaculty());

        calendar = Calendar.getInstance();
        fromTime = Calendar.getInstance();
        toTime   = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 20)
            for (Calendar c: new Calendar[]{calendar, fromTime, toTime}) {
                c.add(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.HOUR_OF_DAY, 7);
            }
        fromTime.add(Calendar.HOUR_OF_DAY, 1);
        toTime.add(Calendar.HOUR_OF_DAY, 2);

        updateCalendar();
        updateFromAndToTime();
    }

    private void updateListView() {
        Reservation[] reservations = queryReservation();

        ListView timeline = findViewById(R.id.detail_timeline);
        ListAdapter adapter = new ReservationAdapter(RoomDetailActivity.this, reservations);
        timeline.setAdapter(adapter);
        timeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message status = (Message) view.getTag();
                if (status != Message.USER_RESERVE) return;
//                Reservation a = (Reservation) parent.getAdapter().getItem(0);
                Log.d("debug", locationID);
                Toast.makeText(RoomDetailActivity.this, Preferences.getLogin(RoomDetailActivity.this), Toast.LENGTH_SHORT).show();
                updateListView();
            }
        });

    }

    private Reservation[] queryReservation() {
        // TO-DO: query reservation list
        Reservation[] reservations = new Reservation[15];
        Random random = new Random();
        Message[] status = {Message.NOT_RESERVE, Message.USER_RESERVE, Message.NON_USER_RESERVE};
        for (int i = 8; i < 23; i++) {
            reservations[i - 8] = new Reservation(
                String.format(Locale.US, "%d %s", (i > 12) ? i - 12 : i, (i > 11) ? "PM" : "AM"),
                status[random.nextInt(3)]
            );
        }
        return reservations;
    }

    private void updateCalendar() {
        String weekday = DateParser.parseWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        String month = DateParser.parseMonth(calendar.get(Calendar.MONTH));
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        day = (day.length() == 1) ? "0" + day : day;

        TextView weekdayTV = findViewById(R.id.detail_date_weekday);
        TextView monthTV = findViewById(R.id.detail_date_month);
        TextView dayTV = findViewById(R.id.detail_date_day);

        weekdayTV.setText(weekday);
        monthTV.setText(month);
        dayTV.setText(day);

        if (weekday.equals("Sun"))
            weekdayTV.setTextColor(getResources().getColor(R.color.red));
        else
            weekdayTV.setTextColor(getResources().getColor(R.color.dark_gray));

        updateListView();
    }

    private void updateFromAndToTime() {
        EditText[] timeET = {
            findViewById(R.id.from_time),
            findViewById(R.id.to_time)
        };
        Calendar[] calendars = {fromTime, toTime};

        for (int hour, i = 0; i < 2; i++) {
            hour = calendars[i].get(Calendar.HOUR_OF_DAY);
            timeET[i].setText(String.format(
                Locale.US,
                "%02d:00 %s",
                (hour < 12) ? hour : hour - 12,
                (hour < 12) ? "AM" : "PM"
            ));
        }
    }

}
