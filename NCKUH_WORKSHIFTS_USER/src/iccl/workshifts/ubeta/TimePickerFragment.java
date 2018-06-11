package iccl.workshifts.ubeta;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the time picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create and return a new instance of TimePickerDialog
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	// onTimeSet() callback method
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String S = "";
		S = "Your chosen time is...\n\n";
		MainActivity.toast(getActivity(), S);
		// Display the user changed time on TextView
		S = "Hour : " + String.valueOf(hourOfDay) + "\nMinute : " + String.valueOf(minute) + "\n";
		MainActivity.toast(getActivity(), S);
	}
}