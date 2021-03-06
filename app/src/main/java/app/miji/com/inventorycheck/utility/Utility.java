package app.miji.com.inventorycheck.utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Delivery;
import app.miji.com.inventorycheck.model.Location;
import app.miji.com.inventorycheck.model.Product;
import app.miji.com.inventorycheck.model.Sales;
import app.miji.com.inventorycheck.model.Transfer;
import app.miji.com.inventorycheck.model.Unit;


public class Utility {
    private static final String NO_LOCATION_MESSAGE = "LocationMesageStatus";
    private static final String LOG_TAG = Utility.class.getSimpleName();

    //KEYS
    private static final String KEY_SALES_ITEMS = "sales";


    private static void saveLocationMessageDialogStatus(Context context, int checkStatus) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(NO_LOCATION_MESSAGE, checkStatus).apply();
    }

    private static int getLocationMessageDialogStatus(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(NO_LOCATION_MESSAGE, 0);
    }

    public static void showLocationDialogBox(final Context context, View mView, final EditText userInputDialogEditText, final LayoutInflater layoutInflaterAndroid, final DatabaseReference databaseReference) {

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.save), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String input = userInputDialogEditText.getText().toString();
                        //validate user input
                        if (input.trim().length() != 0) {

                            //capitalize each word of location
                            input = capitalize(input);

                            //add input to database
                            //create location object to send to firebase
                            Location location = new Location(input);
                            //add to firebase database
                            databaseReference.push().setValue(location);

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

    private static String capitalize(String text) {
        String c = (text != null) ? text.trim() : "";
        String[] words = c.split(" ");
        String result = "";
        for (String w : words) {
            result += (w.length() > 1 ? w.substring(0, 1).toUpperCase() + w.substring(1, w.length()).toLowerCase() : w) + " ";
        }
        return result.trim();
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

    public static void showImagePopup(Context context, WindowManager windowManager, Drawable drawable) {
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

    public static String convertBitmapToBase64(Bitmap bitmap) {
        String encodedString = "";


        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);

        encodedString = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

        return encodedString;
    }


    public static Bitmap decodeBase64Image(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }


    public static Bitmap resizeImageFromFile(InputStream in) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
            InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is1, null, o);

            final int IMAGE_MAX_SIZE = 512;

            System.out.println("h:" + o.outHeight + " w:" + o.outWidth);
            int scale = 100;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(is2, null, o2);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean validateItemInput(Context context, AutoCompleteTextView spinnerName, TextView mTxtQty, AutoCompleteTextView spinnerUnit, TextInputLayout mTxtInItem, TextInputLayout mTxtInQty, TextInputLayout mTxtInUnit) {
        String item = spinnerName.getText().toString();
        String qty = mTxtQty.getText().toString();
        String unit = spinnerUnit.getText().toString();

        Log.e(LOG_TAG, "ITEM------------------>" + item);
        Log.e(LOG_TAG, "QTY------------------>" + qty);
        Log.e(LOG_TAG, "UNIT------------------>" + unit);

        int mItem = item.trim().length();
        int mQty = qty.trim().length();
        int mUnit = unit.trim().length();

        boolean isValid = mItem != 0 && mQty != 0 && mUnit != 0; //if formed is properly filled out


        //check if name is null
        if (mItem == 0) {
            //show error
            mTxtInItem.setErrorEnabled(true);
            mTxtInItem.setError(context.getString(R.string.required_field));
        } else {
            mTxtInItem.setErrorEnabled(false);
        }

        //check if quantity no. is null
        if (mQty == 0) {
            mTxtInQty.setErrorEnabled(true);
            mTxtInQty.setError(context.getString(R.string.required_field));
        } else {
            mTxtInQty.setErrorEnabled(false);
        }

        //check if unit is null
        if (mUnit == 0) {
            mTxtInUnit.setError(context.getString(R.string.required_field));
        } else {
            mTxtInUnit.setError(null);
        }


        return isValid;
    }


    public static void setupLocationSpinnerWithDB(Activity activity, Spinner materialSpinner, Query ref) {

        FirebaseListAdapter firebaseListAdapter = new FirebaseListAdapter(activity, Location.class, R.layout.item_location_spinner, ref) {
            @Override
            protected void populateView(View v, Object o, int position) {
                Location location = (Location) o;
                ((TextView) v.findViewById(R.id.txt_location)).setText(location.getName());
            }
        };

        materialSpinner.setAdapter(firebaseListAdapter);
    }

      public static void setupItemSpinner(Context context, final AutoCompleteTextView autoCompleteTextView) {
        //TODO change this dummy items
        final String[] ITEMS = new String[]{"Cupcake", "Brownies", "Tiramisu", "Cake", "Burger"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, ITEMS);
        autoCompleteTextView.setAdapter(adapter);

        //show drop down list on click
        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //always show the dropdown list
                autoCompleteTextView.showDropDown();
                return false;
            }
        });
    }


    public static void saveSales(Context context, Sales sales) {
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_SALES_ITEMS, Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString(KEY_SALES_ITEMS, "");
        String jsonNewproductToAdd = gson.toJson(sales);
        Log.e(LOG_TAG, "SALES JSON----------->" + jsonNewproductToAdd);

        JSONArray jsonArrayProduct = new JSONArray();

        try {
            if (jsonSaved.length() != 0) {
                jsonArrayProduct = new JSONArray(jsonSaved);
            }
            jsonArrayProduct.put(new JSONObject(jsonNewproductToAdd));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //SAVE NEW ARRAY
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_SALES_ITEMS, jsonArrayProduct.toString());
        editor.commit();
    }

    public static List<Sales> getSalesData(Context context) {
        Gson gson = new Gson();
        List<Sales> productFromShared = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_SALES_ITEMS, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(KEY_SALES_ITEMS, "");

        Type type = new TypeToken<List<Sales>>() {
        }.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        if (productFromShared != null) {
            for (Sales sales : productFromShared) {
                Log.e(LOG_TAG, "SALES LIST----------->" + sales.getCustomer().toString());
            }
        }

        return productFromShared;
    }


    public static String getDeliveryDetails(Context context, String strDate, String strTime, String strDeliveredBy, String strRef, String strLoc) {
        //create detail string
        StringBuffer stringBuffer = new StringBuffer();

        String title = context.getResources().getString(R.string.delivery_details).toUpperCase();
        stringBuffer.append(title);
        stringBuffer.append("\n"); //new line
        stringBuffer.append("\n"); //new line
        //"Date: and Time "
        stringBuffer.append(context.getResources().getString(R.string.mdtp_date) + ": " + strDate);
        stringBuffer.append(" " + strTime);
        stringBuffer.append("\n"); //new line
        //"Delivered by: "
        stringBuffer.append(context.getResources().getString(R.string.delivered_by) + ": " + strDeliveredBy);
        stringBuffer.append("\n"); //new line
        //"Reference No: "
        stringBuffer.append(context.getResources().getString(R.string.reference) + ": " + strRef);
        stringBuffer.append("\n"); //new line
        //"Location: "
        stringBuffer.append(context.getResources().getString(R.string.location) + ": " + strLoc);

        String details = stringBuffer.toString();
        return details;
    }

    public static String getTransferDetails(Context context, String strDate, String strTime, String strTransferId, String strFromLocation, String strToLocation) {
        //create detail string
        StringBuffer stringBuffer = new StringBuffer();

        //Delivery details
        String title = context.getResources().getString(R.string.transfer_details).toUpperCase();
        stringBuffer.append(title);
        stringBuffer.append("\n"); //new line
        stringBuffer.append("\n"); //new line
        //"Date: and Time "
        stringBuffer.append(context.getResources().getString(R.string.mdtp_date) + ": " + strDate);
        stringBuffer.append(" " + strTime);
        stringBuffer.append("\n"); //new line
        //"Transfer ID: "
        stringBuffer.append(context.getResources().getString(R.string.trans_id) + ": " + strTransferId);
        stringBuffer.append("\n"); //new line
        //"From Location: "
        stringBuffer.append(context.getResources().getString(R.string.from) + ": " + strFromLocation);
        stringBuffer.append("\n"); //new line
        //"To Location: "
        stringBuffer.append(context.getResources().getString(R.string.to) + ": " + strToLocation);
        stringBuffer.append("\n"); //new line

        String details = stringBuffer.toString();
        return details;
    }

    public static String getSalesDetails(Context context, String strDate, String strTime, String strCustomer, String strRefNo, String strLocation) {

        //create detail string
        StringBuffer stringBuffer = new StringBuffer();

        //Delivery details
        String title = context.getResources().getString(R.string.sales_details).toUpperCase();
        stringBuffer.append(title);
        stringBuffer.append("\n"); //new line
        stringBuffer.append("\n"); //new line
        //"Date: and Time "
        stringBuffer.append(context.getResources().getString(R.string.mdtp_date) + ": " + strDate);
        stringBuffer.append(" " + strTime);
        stringBuffer.append("\n"); //new line
        //"Delivered by: "
        stringBuffer.append(context.getResources().getString(R.string.customer) + ": " + strCustomer);
        stringBuffer.append("\n"); //new line
        //"Reference No: "
        stringBuffer.append(context.getResources().getString(R.string.reference) + ": " + strRefNo);
        stringBuffer.append("\n"); //new line
        //"Location: "
        stringBuffer.append(context.getResources().getString(R.string.location) + ": " + strLocation);
        stringBuffer.append("\n"); //new line

        String details = stringBuffer.toString();
        return details;
    }

    public static void showUnitDialogBox(final Context context, View mView, final EditText userInputDialogEditText, final LayoutInflater layoutInflaterAndroid, final DatabaseReference databaseReference) {

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.save), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String input = userInputDialogEditText.getText().toString();
                        //validate user input
                        if (input.trim().length() != 0) {

                            //all units should be in lowercase
                            input = input.toLowerCase();

                            //add input to database
                            //create location object to send to firebase
                            Unit unit = new Unit(input);
                            //add to firebase database
                            databaseReference.push().setValue(unit);

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
}




