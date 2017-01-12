package app.miji.com.inventorycheck.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.NewItemRecyclerViewAdapter;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewItemsFragment extends Fragment {


    private NewItemRecyclerViewAdapter mAdapter;
    private int recyclerviewItemQty = 1;
    Context mContext;

    public NewItemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_new_items, container, false);

        mContext = getContext();

        //setup recyclerview
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_new_items);
        int mColumnCount = getResources().getInteger(R.integer.list_item_column_count);
        //set Layout Manager
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        assert recyclerView != null;
        setupRecyclerView(recyclerView);


        //setup fab
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add another item in recyclerview
                setupRecyclerView(recyclerView);


            }
        });


        ImageView mItemImageView = (ImageView) view.findViewById(R.id.imageView);
        TextInputLayout mTxtInQty = (TextInputLayout) view.findViewById(R.id.input_name);
        TextView mTxtQty = (TextView) view.findViewById(R.id.txt_qty);
        final AutoCompleteTextView spinnerName = (AutoCompleteTextView) view.findViewById(R.id.spinner_name);
        AutoCompleteTextView spinnerUnit = (AutoCompleteTextView) view.findViewById(R.id.spinner_unit);


        setupItemSpinner(spinnerName);
        setupUnitSpinner(spinnerUnit);
        setupImagePicker(mItemImageView);


        spinnerName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //always show the dropdown list
                spinnerName.showDropDown();
                return false;
            }
        });


        return view;
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        mAdapter = new NewItemRecyclerViewAdapter(getContext(), recyclerviewItemQty++, getFragmentManager());
        recyclerView.setAdapter(mAdapter);
        //scroll to last item
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void setupItemSpinner(AutoCompleteTextView autoCompleteTextView) {
        //dummy items to show in spinner
        final String[] ITEMS = new String[]{"Cupcake", "Brownies", "Tiramisu", "Cake", "Burger"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line, ITEMS);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void setupUnitSpinner(AutoCompleteTextView autoCompleteTextView) {
        final String[] UNITS = new String[]{"pcs", "kg", "m"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line, UNITS);
        autoCompleteTextView.setAdapter(adapter);
    }


    private void setupImagePicker(final ImageView mItemImageView) {
        mItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start TedBottomPicker
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(mContext)
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                //Do something with selected uri
                                InputStream inputStream;
                                try {
                                    inputStream = mContext.getContentResolver().openInputStream(uri);
                                    //the "image" received here is the image itself
                                    Bitmap b = BitmapFactory.decodeStream(inputStream);
                                    mItemImageView.setImageBitmap(b);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .create();

                tedBottomPicker.show(getFragmentManager());
            }
        });

    }
}
