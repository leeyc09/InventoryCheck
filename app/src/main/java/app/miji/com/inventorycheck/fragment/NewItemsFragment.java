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
import android.util.Log;
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


    private static final String LOG_TAG = NewItemsFragment.class.getSimpleName();
    private NewItemRecyclerViewAdapter mAdapter;
    private int recyclerviewItemQty = 1;
    Context mContext;


    TextInputLayout mTxtInQty;
    TextInputLayout mTxtInItem;
    TextInputLayout mTxtInUnit;

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


        ImageView mItemImageView = (ImageView) view.findViewById(R.id.imageView);
        mTxtInItem = (TextInputLayout) view.findViewById(R.id.input_item);
        mTxtInQty = (TextInputLayout) view.findViewById(R.id.input_qty);
        mTxtInUnit = (TextInputLayout) view.findViewById(R.id.input_unit);
        final TextView mTxtQty = (TextView) view.findViewById(R.id.txt_qty);
        final AutoCompleteTextView spinnerName = (AutoCompleteTextView) view.findViewById(R.id.spinner_name);
        final AutoCompleteTextView spinnerUnit = (AutoCompleteTextView) view.findViewById(R.id.spinner_unit);


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


        //inflate textviews


        //setup fab
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = spinnerName.getText().toString();
                String qty = mTxtQty.getText().toString();
                String unit = spinnerUnit.getText().toString();

                //validate form before adding item to list
                boolean isvalid = validateInput(item, qty, unit);
                if (isvalid) {
                    //add another item in recyclerview
                    setupRecyclerView(recyclerView);

                    //clear textviews
                    mTxtQty.setText("");
                    spinnerName.setText("");
                    spinnerUnit.setText("");
                }


            }
        });

        return view;
    }

    private boolean validateInput(String item, String qty, String unit) {
        int mItem = item.length();
        int mQty = qty.length();
        int mUnit = unit.length();

        boolean isValid = mItem != 0 && mQty != 0 && mUnit != 0; //if formed is properly filled out

        Log.v(LOG_TAG, "ITEM ------------> " + item);
        Log.v(LOG_TAG, "QTY------------> " + qty);
        Log.v(LOG_TAG, "UNIT------------> " + unit);


        //check if delivery is null
        if (mItem == 0) {
            //show error
            mTxtInItem.setErrorEnabled(true);
            mTxtInItem.setError(getString(R.string.required_field));
        } else {
            mTxtInItem.setErrorEnabled(false);
        }

        //check if reference no. is null
        if (mQty == 0) {
            mTxtInQty.setErrorEnabled(true);
            mTxtInQty.setError(getString(R.string.required_field));
        } else {
            mTxtInQty.setErrorEnabled(false);
        }

        //check if location is null
        if (mUnit == 0) {
            mTxtInUnit.setError(getString(R.string.required_field));
        } else {
            mTxtInUnit.setError(null);
        }


        return isValid;
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
