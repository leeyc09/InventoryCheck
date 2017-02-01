package app.miji.com.inventorycheck;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by isse on 01/02/2017.
 * https://firebase.google.com/docs/database/android/offline-capabilities
 * <p>
 * When you enable disk persistence, your app writes the data locally
 * to the device so your app can maintain state while offline, even if
 * the user or operating system restarts the app.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
         /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
