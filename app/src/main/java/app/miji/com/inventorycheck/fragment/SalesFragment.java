package app.miji.com.inventorycheck.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import app.miji.com.inventorycheck.activity.NewItemsActivity;
import app.miji.com.inventorycheck.model.Delivery;
import app.miji.com.inventorycheck.model.Sales;
import app.miji.com.inventorycheck.utility.Utility;
import gun0912.tedbottompicker.TedBottomPicker;

import static app.miji.com.inventorycheck.R.string.delivery;

/**
 * A placeholder fragment containing a simple view.
 */
public class SalesFragment extends Fragment {

    private String base64Image = null;

    public SalesFragment() {
    }

    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    private final String LOG_TAG = DeliveryFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        final ImageButton btnDate = (ImageButton) view.findViewById(R.id.btn_date);
        final ImageButton btnTime = (ImageButton) view.findViewById(R.id.btn_time);
        final EditText txtDate = (EditText) view.findViewById(R.id.txt_date);
        final EditText txtTime = (EditText) view.findViewById(R.id.txt_time);
        final EditText txtCustomer = (EditText) view.findViewById(R.id.txt_customer);
        final EditText txtReference = (EditText) view.findViewById(R.id.txt_reference);
        final TextInputLayout txtInCustomer = (TextInputLayout) view.findViewById(R.id.input_customer);
        final TextInputLayout txtInRefNo = (TextInputLayout) view.findViewById(R.id.input_ref);
        final MaterialBetterSpinner spinnerLocation = (MaterialBetterSpinner) view.findViewById(R.id.material_spinner);
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
        Utility.setupLocationSpinner(getActivity(), spinnerLocation);


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
                                    //the "inputStream" received here is the image itself
                                    inputStream = getActivity().getContentResolver().openInputStream(uri);

                                    //resize image before saving
                                    Bitmap image = Utility.resizeImageFromFile(inputStream);

                                    //assign image to your imageview
                                    imgReceipt.setImageBitmap(image);

                                    //Convert bitmap to base64 to so you
                                    base64Image = Utility.convertBitmapToBase64(image);


                                    Log.e(LOG_TAG, "BASE 64 ------------> " + base64Image);
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
                String strDate = txtDate.getText().toString();
                String strTime = txtTime.getText().toString();
                String strCustomer = txtCustomer.getText().toString();
                String strRefNo = txtReference.getText().toString();
                String strLocation = spinnerLocation.getText().toString();

                int customer = txtCustomer.getText().toString().length();
                int refNo = txtReference.getText().toString().length();
                int location = spinnerLocation.getText().toString().length();
                boolean isValid = customer != 0 && refNo != 0 && location != 0; //if formed is properly filled out

                Log.v(LOG_TAG, "Delivered By: " + customer);
                Log.v(LOG_TAG, "Reference No: " + refNo);
                Log.v(LOG_TAG, "Location: " + location);


                //check if delivery is null
                if (customer == 0) {
                    //show error
                    txtInCustomer.setErrorEnabled(true);
                    txtInCustomer.setError(getString(R.string.required_field));
                } else {
                    txtInCustomer.setErrorEnabled(false);
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
                    spinnerLocation.setError(getString(R.string.required_field));
                } else {
                    spinnerLocation.setError(null);
                }


                //if valid proceed to the next activity
                if (isValid) {
                    // go to NewItemsActivity
                    Intent intent = new Intent(getActivity(), NewItemsActivity.class);

                    //create delivery item
                    //no item list yet
                    Sales sales = new Sales( strDate,  strTime,  strCustomer,  strRefNo,  strLocation,  base64Image,  null);


                    //create detail string
                    StringBuffer stringBuffer = new StringBuffer();

                    //Delivery details
                    String title = getResources().getString(R.string.sales_details).toUpperCase();
                    stringBuffer.append(title);
                    stringBuffer.append("\n"); //new line
                    stringBuffer.append("\n"); //new line
                    //"Date: and Time "
                    stringBuffer.append(getResources().getString(R.string.mdtp_date) + ": " + strDate);
                    stringBuffer.append(" " + strTime);
                    stringBuffer.append("\n"); //new line
                    //"Delivered by: "
                    stringBuffer.append(getResources().getString(R.string.customer) + ": " + strCustomer);
                    stringBuffer.append("\n"); //new line
                    //"Reference No: "
                    stringBuffer.append(getResources().getString(R.string.reference) + ": " + strRefNo);
                    stringBuffer.append("\n"); //new line
                    //"Location: "
                    stringBuffer.append(getResources().getString(R.string.location) + ": " + strLocation);
                    stringBuffer.append("\n"); //new line

                    intent.putExtra(NewItemsActivity.DETAIL, stringBuffer.toString());
                    intent.putExtra(NewItemsActivity.BASE64_IMAGE, base64Image);
                    //add flag for adding items
                    intent.putExtra(NewItemsActivity.FLAG, 0);

                    intent.putExtra(NewItemsActivity.SALES, sales);
                    intent.putExtra(NewItemsActivity.ACTIVITY, NewItemsFragment.SALES);

                    //add flag for adding items
                    intent.putExtra(NewItemsActivity.FLAG, 0);


                    startActivity(intent);
                }

            }
        });


        return view;
    }


}
