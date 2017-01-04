package app.miji.com.inventorycheck.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.activity.DeliveryActivity;
import app.miji.com.inventorycheck.model.Utility;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransferFragment extends Fragment {

    //TODO: change these location base on database
    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };


    private final String LOG_TAG = TransferFragment.class.getSimpleName();

    public TransferFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);


        final ImageButton btnDate = (ImageButton) view.findViewById(R.id.btn_date);
        final ImageButton btnTime = (ImageButton) view.findViewById(R.id.btn_time);
        final EditText txtDate = (EditText) view.findViewById(R.id.txt_date);
        final EditText txtTime = (EditText) view.findViewById(R.id.txt_time);
        final TextView txtTransferId = (TextView) view.findViewById(R.id.txt_trans_id);
        final MaterialBetterSpinner spinnerFromLocation = (MaterialBetterSpinner) view.findViewById(R.id.material_spinner_from_loc);
        final MaterialBetterSpinner spinnerToLocation = (MaterialBetterSpinner) view.findViewById(R.id.material_spinner_to_loc);
        final TextView txtAddLocation = (TextView) view.findViewById(R.id.txt_add_location);
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        final int mYear, mMonth, mDay;
        final int mHour, mMinute;

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        //display current date and time
        String dateNow = mDay + "-" + (mMonth + 1) + "-" + mYear;
        String timeNow = mHour + ":" + mMinute;
        txtDate.setText(dateNow);
        txtTime.setText(timeNow);


        //when btnDate is clicked, show DatePickerDiaolog
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //launch datepicker dialog initially picking current date
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String pickedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        txtDate.setText(pickedDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show(getActivity().getFragmentManager(), getString(R.string.Datepickerdialog));

            }
        });

        //when btnTime is clicked,show TimePickerDialog
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //launch datepicker dialog initially picking current time
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        String pickedTime = hourOfDay + ":" + minute;
                        txtTime.setText(pickedTime);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show(getActivity().getFragmentManager(), getString(R.string.Timepickerdialog));
            }
        });

        //location spinner
        setupSpinner(spinnerFromLocation);
        setupSpinner(spinnerToLocation);


        //when txt_add_location is clicked, show add new location dialog box
        txtAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context mContext = getActivity();
                final LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_location_input, null);
                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

                //show location dialog box
                Utility.showLocationDialogBox(mContext, mView, userInputDialogEditText, layoutInflaterAndroid);

            }
        });


        //setup floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate form
                int toLocation = spinnerFromLocation.getText().toString().length();
                int fromLocation = spinnerToLocation.getText().toString().length();
                boolean isValid = toLocation != 0 && toLocation != 0; //if formed is properly filled out


                Log.v(LOG_TAG, "To Location: " + toLocation);
                Log.v(LOG_TAG, "From Location: " + fromLocation);

                //if valid proceed to the next activity
                if (isValid) {
                    Intent intent = new Intent(getActivity(), DeliveryActivity.class);
                    startActivity(intent);
                }


                //check if reference no. is null
                if (toLocation == 0) {
                    spinnerToLocation.setError(getString(R.string.required_field));
                } else {
                    spinnerToLocation.setError(null);
                }

                //check if location is null
                if (fromLocation == 0) {
                    spinnerFromLocation.setError(getString(R.string.required_field));
                } else {
                    spinnerFromLocation.setError(null);
                }

            }
        });


        return view;
    }

    private void setupSpinner(MaterialBetterSpinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        spinner.setAdapter(adapter);
    }
}
