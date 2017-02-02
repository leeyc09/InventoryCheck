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

import java.io.InputStream;
import java.util.Calendar;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.activity.NewItemsActivity;
import app.miji.com.inventorycheck.model.Delivery;
import app.miji.com.inventorycheck.model.Location;
import app.miji.com.inventorycheck.utility.Utility;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * A fragment for adding delivery information
 */
public class DeliveryFragment extends Fragment {


    //firebase database variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private Query mQuery;
    //receive events about changes in the child locations of a given DatabaseReference
    private ChildEventListener mChildEventListener;


    private final String LOG_TAG = DeliveryFragment.class.getSimpleName();
    private String base64Image;

    public DeliveryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);


        //Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("location");
        //The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners.
        mDatabaseReference.keepSynced(true);

        final ImageButton btnDate = (ImageButton) view.findViewById(R.id.btn_date);
        final ImageButton btnTime = (ImageButton) view.findViewById(R.id.btn_time);
        final EditText txtDate = (EditText) view.findViewById(R.id.txt_date);
        final EditText txtTime = (EditText) view.findViewById(R.id.txt_time);
        final EditText txtDelivery = (EditText) view.findViewById(R.id.txt_delivery);
        final EditText txtReference = (EditText) view.findViewById(R.id.txt_reference);
        final TextInputLayout txtInDelivery = (TextInputLayout) view.findViewById(R.id.input_delivery);
        final TextView txtInLocation = (TextView) view.findViewById(R.id.lbl_location);
        final TextInputLayout txtInRefNo = (TextInputLayout) view.findViewById(R.id.input_ref);
        final Spinner spinner = (Spinner) view.findViewById(R.id.my_spinner);

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
                String strDeliveredBy = txtDelivery.getText().toString();
                String strRef = txtReference.getText().toString();
                String strDate = txtDate.getText().toString();
                String strTime = txtTime.getText().toString();

                String strLoc = "";
                if (spinner.getSelectedItem() != null) {
                    Location location = (Location) spinner.getSelectedItem();
                    strLoc = location.getName();
                }

                //validate form
                int deliveredBy = strDeliveredBy.trim().length();
                int refNo = strRef.trim().length();
                int location = strLoc.trim().length();

                boolean isValid = deliveredBy != 0 && refNo != 0 && location != 0; //if formed is properly filled out

                Log.v(LOG_TAG, "Delivered By: " + deliveredBy);
                Log.v(LOG_TAG, "Reference No: " + refNo);
                Log.v(LOG_TAG, "Location: " + location);


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
                    txtInLocation.setError(getString(R.string.required_field));
                } else {
                    txtInLocation.setError(null);
                }


                //if valid proceed to the next activity
                if (isValid) {
                    Intent intent = new Intent(getActivity(), NewItemsActivity.class);

                    //create delivery item
                    //no item list yet
                    Delivery delivery = new Delivery(strDate, strTime, strLoc, strDeliveredBy, strRef, base64Image, null);


                    //Delivery details
                    String details = Utility.getDeliveryDetails(getContext(), strDate, strTime, strDeliveredBy, strRef, strLoc);

                    //put Extras
                    intent.putExtra(NewItemsActivity.DETAIL, details);//detail String
                    intent.putExtra(NewItemsActivity.BASE64_IMAGE, base64Image);//base64 string
                    intent.putExtra(NewItemsActivity.DELIVERY, delivery);
                    intent.putExtra(NewItemsActivity.ACTIVITY, NewItemsFragment.DELIVERY);

                    //add flag for adding items
                    intent.putExtra(NewItemsActivity.FLAG, 0);

                    startActivity(intent);
                }

            }
        });


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
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


