package app.miji.com.inventorycheck.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Location;

/**
 * Created by isse on 01/02/2017.
 */

public class LocationArrayAdapter extends ArrayAdapter<String> {

    public LocationArrayAdapter(Context context, int resource) {
        super(context, resource);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_location_spinner, parent, false);
        }

        String location = getItem(position);
        TextView txtLocation = (TextView) convertView.findViewById(R.id.txt_location);

        txtLocation.setText(location);
        return convertView;

    }
}
