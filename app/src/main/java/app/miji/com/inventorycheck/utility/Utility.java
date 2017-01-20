package app.miji.com.inventorycheck.utility;

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
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
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
import app.miji.com.inventorycheck.model.Transfer;


public class Utility {
    private static final String NO_LOCATION_MESSAGE = "LocationMesageStatus";
    private static final String LOG_TAG = Utility.class.getSimpleName();

    //KEYS
    private static final String KEY_DELIVERY_ITEMS = "delivery";
    private static final String KEY_STOCK_IN_TRANSFER_ITEMS = "transfer_stockIn";
    private static final String KEY_STOCK_OUT_TRANSFER_ITEMS = "transfer_stockOut";


    private static void saveLocationMessageDialogStatus(Context context, int checkStatus) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(NO_LOCATION_MESSAGE, checkStatus).apply();
    }

    private static int getLocationMessageDialogStatus(Context context) {
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

        int mItem = item.length();
        int mQty = qty.length();
        int mUnit = unit.length();

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


    public static void setupLocationSpinner(Context context, MaterialBetterSpinner materialSpinner) {
        //TODO change this dummy location
        final String[] LOCATIONS = new String[]{
                "Belgium", "France", "Italy", "Germany", "Spain"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, LOCATIONS);
        materialSpinner.setAdapter(adapter);
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


    public static void saveDelivery(Context context, Delivery delivery) {

        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_DELIVERY_ITEMS, Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString(KEY_DELIVERY_ITEMS, "");
        String jsonNewproductToAdd = gson.toJson(delivery);
        Log.e(LOG_TAG, "DELIVERY JSON----------->" + jsonNewproductToAdd);

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
        editor.putString(KEY_DELIVERY_ITEMS, jsonArrayProduct.toString());
        editor.commit();
    }


    public static List<Delivery> getDeliveryData(Context context) {
        Gson gson = new Gson();
        List<Delivery> productFromShared = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_DELIVERY_ITEMS, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(KEY_DELIVERY_ITEMS, "");

        Type type = new TypeToken<List<Delivery>>() {
        }.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        if (productFromShared != null) {
            for (Delivery delivery : productFromShared) {
                Log.e(LOG_TAG, "DELIVERY LIST----------->" + delivery.getDeliveryMan().toString());
            }
        }

        return productFromShared;
    }


    public static void saveTransfer_StockIn(Context context, Transfer transfer) {
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_STOCK_IN_TRANSFER_ITEMS, Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString(KEY_STOCK_IN_TRANSFER_ITEMS, "");
        String jsonNewproductToAdd = gson.toJson(transfer);
        Log.e(LOG_TAG, "TRANSFER JSON STOCK IN----------->" + jsonNewproductToAdd);

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
        editor.putString(KEY_STOCK_IN_TRANSFER_ITEMS, jsonArrayProduct.toString());
        editor.commit();
    }

    public static List<Transfer> getTransferData_StockIn(Context context) {
        Gson gson = new Gson();
        List<Transfer> productFromShared = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_STOCK_IN_TRANSFER_ITEMS, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(KEY_STOCK_IN_TRANSFER_ITEMS, "");

        Type type = new TypeToken<List<Transfer>>() {
        }.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        if (productFromShared != null) {
            for (Transfer transfer : productFromShared) {
                Log.e(LOG_TAG, "TRANSFER LIST STOCK IN----------->" + transfer.getFromLocation().toString());
            }
        }

        return productFromShared;
    }

    public static void saveTransfer_StockOut(Context context, Transfer transfer) {
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_STOCK_OUT_TRANSFER_ITEMS, Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString(KEY_STOCK_OUT_TRANSFER_ITEMS, "");
        String jsonNewproductToAdd = gson.toJson(transfer);
        Log.e(LOG_TAG, "TRANSFER JSON STOCK OUT----------->" + jsonNewproductToAdd);

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
        editor.putString(KEY_STOCK_OUT_TRANSFER_ITEMS, jsonArrayProduct.toString());
        editor.commit();
    }


    public static List<Transfer> getTransferData_StockOut(Context context) {
        Gson gson = new Gson();
        List<Transfer> productFromShared = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_STOCK_OUT_TRANSFER_ITEMS, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(KEY_STOCK_OUT_TRANSFER_ITEMS, "");

        Type type = new TypeToken<List<Transfer>>() {
        }.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        if (productFromShared != null) {
            for (Transfer transfer : productFromShared) {
                Log.e(LOG_TAG, "TRANSFER LIST STOCK OUT----------->" + transfer.getFromLocation().toString());
            }
        }

        return productFromShared;
    }
}




