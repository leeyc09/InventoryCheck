package app.miji.com.inventorycheck.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.InputStream;
import java.util.Calendar;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.activity.MainActivity;
import app.miji.com.inventorycheck.model.Utility;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * A fragment for adding delivery information
 */
public class DeliveryFragment extends Fragment {


    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };


    private final String LOG_TAG = DeliveryFragment.class.getSimpleName();

    public DeliveryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);

        final ImageButton btnDate = (ImageButton) view.findViewById(R.id.btn_date);
        final ImageButton btnTime = (ImageButton) view.findViewById(R.id.btn_time);
        final EditText txtDate = (EditText) view.findViewById(R.id.txt_date);
        final EditText txtTime = (EditText) view.findViewById(R.id.txt_time);
        final EditText txtDelivery = (EditText) view.findViewById(R.id.txt_delivery);
        final EditText txtReference = (EditText) view.findViewById(R.id.txt_reference);
        final TextInputLayout txtInDelivery = (TextInputLayout) view.findViewById(R.id.input_delivery_name);
        final TextInputLayout txtInRefNo = (TextInputLayout) view.findViewById(R.id.input_ref);
        final TextInputLayout txtInLocation = (TextInputLayout) view.findViewById(R.id.input_location);
        final MaterialBetterSpinner materialSpinner = (MaterialBetterSpinner) view.findViewById(R.id.material_spinner);
        final TextView txtAddLocation = (TextView) view.findViewById(R.id.txt_add_location);
        final ImageView imgReceipt = (ImageView) view.findViewById(R.id.img_receipt);
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
        setupSpinner(view, materialSpinner);


        //when txt_add_location is clicked, show add new location dialog box
        txtAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context mContext = getActivity();
                final LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_location_input, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mContext);
                alertDialogBuilderUserInput.setView(mView);


                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // get user input here
                                String input = userInputDialogEditText.getText().toString();
                                //TODO: add input to database

                                //TODO: determine if success or not in saving location

                                boolean flagSuccess = true; //success, can be false

                                if (flagSuccess) {
                                    //inform user that location is saved
                                    //check if user stills wants to be informed based on sharedPref

                                    int status = Utility.getLocationMessageDialogStatus(mContext);
                                    if (status == 0) {
                                        //show dialog message
                                        showLocationDialogMessage(layoutInflaterAndroid, mContext, flagSuccess);
                                    }
                                } else {
                                    //need to inform user that there is failurein saving location
                                    showLocationDialogMessage(layoutInflaterAndroid, mContext, flagSuccess);
                                }


                            }
                        })

                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();

            }
        });


        //setup receipt imageview
        imgReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start TedBottomPicker
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getActivity())
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                //Do something with selected uri
                                InputStream inputStream;
                                try {
                                    inputStream = getActivity().getContentResolver().openInputStream(uri);
                                    //the "image" received here is the image itself
                                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                                    //assign image to your imageview
                                    imgReceipt.setImageBitmap(image);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .create();

                tedBottomPicker.show(getActivity().getSupportFragmentManager());

            }
        });


        //setup floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate form
                int deliveredBy = txtDelivery.getText().toString().length();
                int refNo = txtReference.getText().toString().length();
                int location = materialSpinner.getText().toString().length();
                boolean isValid = deliveredBy != 0 && refNo != 0 && location != 0; //if formed is properly filled out

                Log.v(LOG_TAG, "Delivered By: " + deliveredBy);
                Log.v(LOG_TAG, "Reference No: " + refNo);
                Log.v(LOG_TAG, "Location: " + location);

                //if valid proceed to the next activity
                if (isValid) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }

                //check if delivery is null
                if (deliveredBy == 0) {
                    //show error
                    txtInDelivery.setErrorEnabled(true);
                    txtInDelivery.setError(getString(R.string.required_field));
                } else {
                    txtInDelivery.setErrorEnabled(false);
                }

                //check if reference no. is null
                if (refNo == 0) {
                    txtInRefNo.setErrorEnabled(true);
                    txtInRefNo.setError(getString(R.string.required_field));
                } else {
                    txtInRefNo.setErrorEnabled(false);
                }

                //check if location is null
                if (location == 0) {
                    txtInLocation.setErrorEnabled(true);
                    txtInLocation.setError(getString(R.string.required_field));
                } else {
                    txtInLocation.setErrorEnabled(false);
                }

            }
        });


        return view;
    }

    private void showLocationDialogMessage(LayoutInflater layoutInflaterAndroid, final Context mContext, boolean flagSuccess) {
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_location_message, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mContext);
        alertDialogBuilderUserInput.setView(mView);

        //inflate layouts
        TextView txtTitle = (TextView) mView.findViewById(R.id.location_message_title);
        TextView txtMessage = (TextView) mView.findViewById(R.id.location_message);
        final CheckBox chkNoMessage = (CheckBox) mView.findViewById(R.id.no_message);

        //set header and message
        if (flagSuccess) {
            txtTitle.setText(getString(R.string.location_succes_header));
            txtMessage.setText(getString(R.string.location_succes_message));
            chkNoMessage.setVisibility(View.VISIBLE);
        } else {
            txtTitle.setText(getString(R.string.location_failed_header));
            txtMessage.setText(getString(R.string.location_failed_message));
            chkNoMessage.setVisibility(View.INVISIBLE);
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getString(R.string.mdtp_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // checkbox logic
                        //checked: 1 - don't show message again
                        //unchecked: 0 - can show message
                        int checkBoxResult = 0;
                        if (chkNoMessage.isChecked()) {
                            checkBoxResult = 1;
                        }

                        Utility.saveLocationMessageDialogStatus(mContext, checkBoxResult);

                    }
                })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();


    }

    private void setupSpinner(View view, MaterialBetterSpinner materialSpinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        materialSpinner.setAdapter(adapter);
    }


}


