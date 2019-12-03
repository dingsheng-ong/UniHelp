package my.edu.um.fsktm.unihelp.ui.reservation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Locale;

import my.edu.um.fsktm.unihelp.R;

public class HourPickerDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private final Calendar calendar;
    private NumberPicker numberPicker;
    private DialogInterface.OnClickListener listener;

    public HourPickerDialog(Calendar calendar, DialogInterface.OnClickListener listener) {
        this.calendar = calendar;
        this.listener = listener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        calendar.set(Calendar.HOUR_OF_DAY, numberPicker.getValue());
        listener.onClick(dialog, which);
    }

    private NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
        @Override
        public String format(int value) {
            int hour = (value > 12) ? value - 12 : value;
            String period = (value < 12) ? "AM" : "PM";
            return String.format(Locale.US, "%02d   %s", hour, period);
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.reserve_hour_picker, null);
        numberPicker = view.findViewById(R.id.hour_picker);
        numberPicker.setMaxValue(22);
        numberPicker.setMinValue(8);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        numberPicker.setFormatter(formatter);
        try {
            Method method = numberPicker.getClass().getDeclaredMethod(
                "changeValueByOne",
                boolean.class
            );
            method.setAccessible(true);
            method.invoke(numberPicker, true);
        } catch (InvocationTargetException e) {

        } catch (Exception e) {
            Log.e("HPD 68", e.toString());
        }

        builder.setView(view);
        builder.setCancelable(true);
        builder.setNegativeButton("CANCEL", null);
        builder.setPositiveButton("OK", this);
        return builder.create();
    }
}
