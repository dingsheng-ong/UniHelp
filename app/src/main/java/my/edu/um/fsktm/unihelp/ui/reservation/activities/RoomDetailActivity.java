package my.edu.um.fsktm.unihelp.ui.reservation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Location;
import my.edu.um.fsktm.unihelp.models.Reservation;
import my.edu.um.fsktm.unihelp.ui.reservation.HourPickerDialog;
import my.edu.um.fsktm.unihelp.ui.reservation.QueryBuilder;
import my.edu.um.fsktm.unihelp.ui.reservation.adapters.ReservationAdapter;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.DateParser;
import my.edu.um.fsktm.unihelp.util.LoadingScreen;
import my.edu.um.fsktm.unihelp.util.Message;
import my.edu.um.fsktm.unihelp.util.Preferences;

public class RoomDetailActivity extends AppCompatActivity {

    private Calendar calendar, timeStart, timeEnd;
    private ListAdapter adapter;
    private String locationId = null;
    private Reservation[] reservations = new Reservation[14];
    private AlertDialog loading;

    private View.OnClickListener updateDate(final int delta) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH, delta);
                long currTime = System.currentTimeMillis();
                if (calendar.getTimeInMillis() < currTime)
                    calendar.setTimeInMillis(currTime);

                timeStart.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                timeEnd.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                updateCalendar();
                updateFromAndToTime();
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
                    timeStart.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    );
                    timeEnd.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    );
                    updateCalendar();
                    updateFromAndToTime();
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

    private View.OnClickListener bookRoom = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] credential = Preferences.getLogin(RoomDetailActivity.this).split("/");
            String user = credential[0];
            String query = QueryBuilder.bookRoom(user, locationId, timeStart, timeEnd);
            Log.d("query", query);
            Response.Listener listener = new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    Log.d("resp", response.toString());
                    updateCalendar();
                }
            };
            Database.sendQuery(RoomDetailActivity.this, query, listener);
        }
    };

    private View.OnClickListener selectTime(final boolean isFrom) {
        final Calendar c = (isFrom) ? timeStart : timeEnd;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface loading, int which) {
                        updateFromAndToTime();
                    }
                };
                DialogFragment hourPickerDialog = new HourPickerDialog(c, listener);
                hourPickerDialog.show(getSupportFragmentManager(), "hourPicker");
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_detail_layout);
        loading = LoadingScreen.build(RoomDetailActivity.this);

        setupActionBar();
        setupAllTextView();
        setupListView();

        updateCalendar();
        updateFromAndToTime();

        findViewById(R.id.detail_inc_date).setOnClickListener(updateDate(+1));
        findViewById(R.id.detail_dec_date).setOnClickListener(updateDate(-1));
        findViewById(R.id.detail_select_date).setOnClickListener(selectDate);
        findViewById(R.id.from_time).setOnClickListener(selectTime(true));
        findViewById(R.id.to_time).setOnClickListener(selectTime(false));
        findViewById(R.id.detail_book).setOnClickListener(bookRoom);
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
        } catch (NullPointerException e) { Log.e("RDA 141", e.toString()); }
    }

    private void setupAllTextView() {
        Intent intent = getIntent();
        Location location = (Location) intent.getSerializableExtra("location");

        locationId = location.getLocationId();

        TextView nameTV = findViewById(R.id.detail_name);
        TextView facTV  = findViewById(R.id.detail_fac);

        nameTV.setText(location.getName());
        facTV.setText(location.getFaculty());

        calendar = Calendar.getInstance();
        timeStart = Calendar.getInstance();
        timeEnd   = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 20 || hour < 8)
            for (Calendar c: new Calendar[]{calendar, timeStart, timeEnd}) {
                if (hour > 20) c.add(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.HOUR_OF_DAY, 7);
            }
        timeStart.add(Calendar.HOUR_OF_DAY, 1);
        timeEnd.add(Calendar.HOUR_OF_DAY, 2);

        for (int i = 8; i < 22; i++) {
            reservations[i - 8] = new Reservation(
                String.format(Locale.US, "%d %s", (i > 12) ? i - 12 : i, (i > 11) ? "PM" : "AM"),
                Message.NOT_RESERVE
            );
        }
    }

    private void setupListView() {
        ListView timeline = findViewById(R.id.detail_timeline);
        adapter = new ReservationAdapter(RoomDetailActivity.this, reservations);
        timeline.setAdapter(adapter);
        timeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message status = (Message) view.getTag();
                if (status != Message.USER_RESERVE) return;

                AlertDialog.Builder builder = new AlertDialog.Builder(RoomDetailActivity.this);
                builder.setTitle("CANCEL BOOKING");

                int min = position, max = position;
                while (--min > -1 && reservations[min].getStatus() == Message.USER_RESERVE) {}
                while (++max < 14 && reservations[max].getStatus() == Message.USER_RESERVE) {}
                min += 9;
                max += 8;

                builder.setMessage(String.format(
                    Locale.US,
                    "Cancel this Session?%n%d %s - %d %s",
                    (min > 12) ? min - 12 : min, (min < 12) ? "am" : "pm",
                    (max > 12) ? max - 12 : max, (max < 12) ? "am" : "pm"
                ));

                Calendar f = Calendar.getInstance();
                Calendar t = Calendar.getInstance();
                f.setTime(timeStart.getTime()); f.set(Calendar.HOUR_OF_DAY, min);
                t.setTime(timeEnd.getTime()); t.set(Calendar.HOUR_OF_DAY, max);
                final String from = DateParser.calendarToString(f, "yyyy-MM-dd HH") + ":00:00";
                final String to = DateParser.calendarToString(t, "yyyy-MM-dd HH") + ":00:00";

                DialogInterface.OnClickListener cancelBooking = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface loading, int which) {
                        String[] credential = Preferences.getLogin(RoomDetailActivity.this).split("/");
                        String user = credential[0];
                        String query = QueryBuilder.cancelRoom(user, locationId, from, to);
                        Response.Listener listener = new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                updateCalendar();
                            }
                        };
                        Database.sendQuery(RoomDetailActivity.this, query, listener);
                    }
                };
                builder.setPositiveButton("OK", cancelBooking);
                builder.setNegativeButton("CANCEL", null);
                builder.show();
            }
        });

    }

    private void queryReservation() {
        String[] credential = Preferences.getLogin(RoomDetailActivity.this).split("/");
        final String userID = credential[0];
        loading.show();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                for (int h = 8; h < 22; h++)
                    reservations[h - 8].setStatus(Message.NOT_RESERVE);
                ((BaseAdapter) adapter).notifyDataSetChanged();
                try {
                    JSONArray data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject entry = data.getJSONObject(i);
                        String id = entry.getString("1");
                        String fromStr = entry.getString("2");
                        String toStr = entry.getString("3");
                        if (fromStr.length() < 9)
                            fromStr = DateParser.calendarToString(
                                calendar,
                                "yyyy-MM-dd"
                            ) + " " + fromStr;
                        if (toStr.length() < 9)
                            toStr = DateParser.calendarToString(
                                    calendar,
                                    "yyyy-MM-dd"
                            ) + " " + toStr;
                        Calendar from = DateParser.stringToCalendar(
                            fromStr, "yyyy-MM-dd HH:mm:ss"
                        );
                        Calendar to = DateParser.stringToCalendar(
                            toStr, "yyyy-MM-dd HH:mm:ss"
                        );
                        int hourFrom = from.get(Calendar.HOUR_OF_DAY);
                        int hourTo = to.get(Calendar.HOUR_OF_DAY);

                        Message status;
                        if (userID.equals(id)) status = Message.USER_RESERVE;
                        else                   status = Message.NON_USER_RESERVE;

                        for (int h = hourFrom; h < hourTo; h++)
                            reservations[h - 8].setStatus(status);
                    }
                    ((BaseAdapter) adapter).notifyDataSetChanged();
                    updateFromAndToTime();
                } catch (JSONException e) {
                    Log.e("RDA 247", e.toString());
                }
                loading.dismiss();
            }
        };
        String query = QueryBuilder.roomSchedule(
            DateParser.calendarToString(calendar, "yyyy-MM-dd"),
            locationId
        );
        Database.sendQuery(this, query, listener);
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

        queryReservation();
    }

    private void updateFromAndToTime() {
        EditText[] timeET = {
            findViewById(R.id.from_time),
            findViewById(R.id.to_time)
        };
        int fromHour = timeStart.get(Calendar.HOUR_OF_DAY);
        int toHour   = timeEnd.get(Calendar.HOUR_OF_DAY);
        int nowHour  = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        for (int hour, i = 0; i < 2; i++) {
            hour = (i == 0) ? fromHour : toHour;
            timeET[i].setText(String.format(
                Locale.US,
                "%02d:00 %s",
                (hour > 12) ? hour - 12 : hour,
                (hour < 12) ? "AM" : "PM"
            ));
        }

        Button book = findViewById(R.id.detail_book);
        boolean isBooked = false;
        for (int h = fromHour; h < toHour; h++) {
            if (reservations[h - 8].getStatus() != Message.NOT_RESERVE) {
                isBooked = true;
                break;
            }
        }
        if (toHour <= fromHour || isBooked) {
            book.setEnabled(false);
        } else if (fromHour <= nowHour && calendar.get(Calendar.DAY_OF_MONTH) == today) {
            book.setEnabled(false);
        }
        else { book.setEnabled(true); }
    }

}
