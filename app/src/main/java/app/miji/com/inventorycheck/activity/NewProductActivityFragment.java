package app.miji.com.inventorycheck.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.miji.com.inventorycheck.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewProductActivityFragment extends Fragment {

    public NewProductActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_product, container, false);
    }
}
