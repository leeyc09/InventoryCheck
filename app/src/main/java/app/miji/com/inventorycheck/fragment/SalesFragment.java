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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.InputStream;
import java.util.Calendar;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.activity.NewItemsActivity;
import app.miji.com.inventorycheck.model.Location;
import app.miji.com.inventorycheck.model.Sales;
import app.miji.com.inventorycheck.utility.Utility;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * A placeholder fragment containing a simple view.
 */
public class SalesFragment extends Fragment {

    private String base64Image = null;

    //firebase database variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private Query mQuery;
    //receive events about changes in the child locations of a given DatabaseReference
    private ChildEventListener mChildEventListener;


    public SalesFragment() {
    }

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
        final TextView txtAddLocation = (TextView) view.findViewById(R.id.txt_add_location);
        final ImageView imgReceipt = (ImageView) view.findViewById(R.id.img_receipt);
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        final Spinner spinner = (Spinner) view.findViewById(R.id.my_spinner);


        //Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("location");
        //The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners.
        mDatabaseReference.keepSynced(true);



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

        //read firebase location data
        attachDatabaseReadListener();

        //location spinner
        Utility.setupLocationSpinnerWithDB(getActivity(), spinner, mQuery);

        //when txt_add_location is clicked, show add new location dialog box
        txtAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context mContext = getActivity();
                final LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_location_input, null);
                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

                //show location dialog box
                Utility.showLocationDialogBox(mContext, mView, userInputDialogEditText, layoutInflaterAndroid, mDatabaseReference);
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

                String strLocation = "";
                if (spinner.getSelectedItem() != null) {
                    Location location = (Location) spinner.getSelectedItem();
                    strLocation = location.getName();
                }

                int customer = txtCustomer.getText().toString().length();
                int refNo = txtReference.getText().toString().length();
                int location = strLocation.trim().length();
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

                //if valid proceed to the next activity
                if (isValid) {
                    // go to NewItemsActivity
                    Intent intent = new Intent(getActivity(), NewItemsActivity.class);

                    //create delivery item
                    //no item list yet
                    Sales sales = new Sales(strDate, strTime, strCustomer, strRefNo, strLocation, base64Image, null);

                    String details = Utility.getSalesDetails(getContext(), strDate, strTime, strCustomer, strRefNo, strLocation);

                    intent.putExtra(NewItemsActivity.DETAIL, details);
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

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {

            mQuery = mDatabaseReference.orderByChild("name");

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String location = dataSnapshot.child("name").getValue(String.class);
                    Log.v(LOG_TAG, "LOCATION FROM DB -------->  :  " + location);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(LOG_TAG, databaseError.getMessage());
                }
            };

            mQuery.addChildEventListener(mChildEventListener);
        }
    }


}
