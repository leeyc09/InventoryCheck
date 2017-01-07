package app.miji.com.inventorycheck.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.miji.com.inventorycheck.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewItemsFragment extends Fragment {

    public NewItemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_new_items, container, false);
    }
}
