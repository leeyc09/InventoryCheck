package app.miji.com.inventorycheck.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;

import app.miji.com.inventorycheck.R;

/**
 * Created by isse on 02/01/2017.
 */

public class Utility {
    private static final String NO_LOCATION_MESSAGE = "LocationMesageStatus";

    public static void saveLocationMessageDialogStatus(Context context, int checkStatus) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(NO_LOCATION_MESSAGE, checkStatus).commit();
    }

    public static int getLocationMessageDialogStatus(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(NO_LOCATION_MESSAGE, 0);
    }

    public static void showLocationDialogBox(final Context context, View mView, final EditText userInputDialogEditText, final LayoutInflater layoutInflaterAndroid) {


        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.save), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // TODO: do something with the user input
                        String input = userInputDialogEditText.getText().toString();
                        //TODO: add input to database

                        //TODO: determine if success or not in saving location

                        boolean flagSuccess = true; //success, can be false

                        if (flagSuccess) {
                            //inform user that location is saved
                            //check if user stills wants to be informed based on sharedPref

                            int status = Utility.getLocationMessageDialogStatus(context);
                            if (status == 0) {
                                //show dialog message
                                showLocationDialogMessage(layoutInflaterAndroid, context, flagSuccess);
                            }
                        } else {
                            //need to inform user that there is failurein saving location
                            showLocationDialogMessage(layoutInflaterAndroid, context, flagSuccess);
                        }


                    }
                })

                .setNegativeButton(context.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    private static void showLocationDialogMessage(LayoutInflater layoutInflaterAndroid, final Context context, boolean flagSuccess) {
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_location_message, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        //inflate layouts
        TextView txtTitle = (TextView) mView.findViewById(R.id.location_message_title);
        TextView txtMessage = (TextView) mView.findViewById(R.id.location_message);
        final CheckBox chkNoMessage = (CheckBox) mView.findViewById(R.id.no_message);

        //set header and message
        if (flagSuccess) {
            txtTitle.setText(context.getString(R.string.location_succes_header));
            txtMessage.setText(context.getString(R.string.location_succes_message));
            chkNoMessage.setVisibility(View.VISIBLE);
        } else {
            txtTitle.setText(context.getString(R.string.location_failed_header));
            txtMessage.setText(context.getString(R.string.location_failed_message));
            chkNoMessage.setVisibility(View.INVISIBLE);
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.mdtp_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // checkbox logic
                        //checked: 1 - don't show message again
                        //unchecked: 0 - can show message
                        int checkBoxResult = 0;
                        if (chkNoMessage.isChecked()) {
                            checkBoxResult = 1;
                        }

                        Utility.saveLocationMessageDialogStatus(context, checkBoxResult);

                    }
                })
                .setNegativeButton(context.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();


    }

    public static void showImagePopup(Context context, ImageView imageView, WindowManager windowManager, Drawable drawable){
        //get screen's height and width
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final ImagePopup imagePopup = new ImagePopup(context);
        imagePopup.setBackgroundColor(Color.BLACK);
        imagePopup.setWindowWidth(width);
        imagePopup.setWindowHeight(height);

        imagePopup.initiatePopup(drawable);

    }

}




