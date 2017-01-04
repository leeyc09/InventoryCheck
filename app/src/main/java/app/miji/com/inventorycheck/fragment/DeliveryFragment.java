package app.miji.com.inventorycheck.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import app.miji.com.inventorycheck.activity.TransferActivity;
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
        final EditText txtDelivery = (EditText) view.findViewById(R.id.txt_trans_id);
        final EditText txtReference = (EditText) view.findViewById(R.id.txt_reference);
        final TextInputLayout txtInDelivery = (TextInputLayout) view.findViewById(R.id.input_delivery_name);
        final TextInputLayout txtInRefNo = (TextInputLayout) view.findViewById(R.id.input_ref);
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
        setupSpinner(materialSpinner);


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
                    Intent intent = new Intent(getActivity(), TransferActivity.class);
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
                    materialSpinner.setError(getString(R.string.required_field));
                } else {
                    materialSpinner.setError(null);
                }

            }
        });


        return view;
    }


    private void setupSpinner(MaterialBetterSpinner materialSpinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        materialSpinner.setAdapter(adapter);
    }


}


