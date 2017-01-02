package app.miji.com.inventorycheck.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
}




