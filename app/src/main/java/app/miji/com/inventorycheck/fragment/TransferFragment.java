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
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Calendar;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.activity.NewItemsActivity;
import app.miji.com.inventorycheck.activity.TransferActivity;
import app.miji.com.inventorycheck.model.Location;
import app.miji.com.inventorycheck.model.Transfer;
import app.miji.com.inventorycheck.utility.Utility;

public class TransferFragment extends Fragment {

    private int flag_activity = 0;
    private final String LOG_TAG = TransferFragment.class.getSimpleName();

    //firebase database variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private Query mQuery;
    //receive events about changes in the child locations of a given DatabaseReference
    private ChildEventListener mChildEventListener;

    public TransferFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);

        //Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("location");
        //The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners.
        mDatabaseReference.keepSynced(true);

        final ImageButton btnDate = (ImageButton) view.findViewById(R.id.btn_date);
        final ImageButton btnTime = (ImageButton) view.findViewById(R.id.btn_time);
        final EditText txtDate = (EditText) view.findViewById(R.id.txt_date);
        final EditText txtTime = (EditText) view.findViewById(R.id.txt_time);
        final TextView txtTransferId = (TextView) view.findViewById(R.id.txt_transferID);
        final TextView txtFromLocation = (TextView) view.findViewById(R.id.lbl_fromLocation);
        final TextView txtToLocation = (TextView) view.findViewById(R.id.lbl_toLocation);
        final TextView txtAddLocation = (TextView) view.findViewById(R.id.txt_add_location);
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        final Spinner spinnerTo = (Spinner) view.findViewById(R.id.to_spinner);
        final Spinner spinnerFrom = (Spinner) view.findViewById(R.id.from_spinner);


        //get flag for spinner hint label
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            flag_activity = intent.getIntExtra(TransferActivity.FLAG, 0);
        }
        switch (flag_activity) {
            case 0:
                //from stock in
                txtFromLocation.setText(getString(R.string.from_location));
                txtToLocation.setText(getString(R.string.to_location));
                break;
            case 1:
                //from stock out
                txtFromLocation.setText(getString(R.string.to_location));
                txtToLocation.setText(getString(R.string.from_location));
                break;
        }


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
        Utility.setupLocationSpinnerWithDB(getActivity(), spinnerTo, mQuery);
        Utility.setupLocationSpinnerWithDB(getActivity(), spinnerFrom, mQuery);


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


        //setup floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate form
                boolean isValid = spinnerFrom.getSelectedItem() != null && spinnerTo.getSelectedItem() != null;


                //check if location is null
                if (spinnerFrom.getSelectedItem() == null) {
                    txtFromLocation.setError(getString(R.string.required_field));
                } else {
                    txtFromLocation.setError(null);
                }

                //check if location is null
                if (spinnerTo.getSelectedItem() == null) {
                    txtToLocation.setError(getString(R.string.required_field));
                } else {
                    txtToLocation.setError(null);
                }


                //if valid proceed to the next activity
                if (isValid) {
                    //convert Location to String
                    Location locationFrom = (Location) spinnerFrom.getSelectedItem();
                    Location locationTo = (Location) spinnerTo.getSelectedItem();

                    String strFromLocation = locationFrom.getName();
                    String strToLocation = locationTo.getName();

                    String strTime = txtTime.getText().toString();
                    String strDate = txtDate.getText().toString();

                    //TODO generate unique transfer id, include in transfer details
                    String strTransferId = "0000011251";


                    //create delivery item
                    //no item list yet
                    Transfer transfer = new Transfer(strDate, strTime, strTransferId, strFromLocation, strToLocation, null);

                    Intent intent = new Intent(getActivity(), NewItemsActivity.class);

                    //get details from utility
                    String details = Utility.getTransferDetails(getContext(), strDate, strTime, strTransferId, strFromLocation, strToLocation);

                    //put Extras
                    intent.putExtra(NewItemsActivity.DETAIL, details);
                    intent.putExtra(NewItemsActivity.TRANSFER, transfer);
                    intent.putExtra(NewItemsActivity.ACTIVITY, NewItemsFragment.TRANSFER);
                    //add flag for adding items
                    intent.putExtra(NewItemsActivity.FLAG, 0);
                    //if transfer is from stock in or stock out activity
                    intent.putExtra(NewItemsFragment.STOCK, flag_activity);
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
