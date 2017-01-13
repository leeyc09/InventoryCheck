package app.miji.com.inventorycheck.fragment;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.util.ArrayList;
import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.NewItemRecyclerViewAdapter;
import app.miji.com.inventorycheck.model.Item;
import app.miji.com.inventorycheck.utility.Utility;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewItemsFragment extends Fragment {


    private static final String LOG_TAG = NewItemsFragment.class.getSimpleName();
    private NewItemRecyclerViewAdapter mAdapter;
    private Context mContext;


    private TextInputLayout mTxtInQty;
    private TextInputLayout mTxtInItem;
    private TextInputLayout mTxtInUnit;

    private List<Item> itemList;
    private String base64Image = null;

    public NewItemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_new_items, container, false);

        mContext = getContext();

        //create new list
        itemList = new ArrayList<>();

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

        //inflate views
        final ImageView mItemImageView = (ImageView) view.findViewById(R.id.imageView);
        mTxtInItem = (TextInputLayout) view.findViewById(R.id.input_item);
        mTxtInQty = (TextInputLayout) view.findViewById(R.id.input_qty);
        mTxtInUnit = (TextInputLayout) view.findViewById(R.id.input_unit);
        final TextView mTxtQty = (TextView) view.findViewById(R.id.txt_qty);
        final AutoCompleteTextView spinnerName = (AutoCompleteTextView) view.findViewById(R.id.spinner_name);
        final AutoCompleteTextView spinnerUnit = (AutoCompleteTextView) view.findViewById(R.id.spinner_unit);

        //setup drop down list items
        setupItemSpinner(spinnerName);
        setupUnitSpinner(spinnerUnit);
        setupImagePicker(mItemImageView);

        //setup fab
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //validate form before adding item to list
                boolean isvalid = Utility.validateItemInput(getActivity(), spinnerName, mTxtQty, spinnerUnit, mTxtInItem, mTxtInQty, mTxtInUnit);
                if (isvalid) {

                    String item = spinnerName.getText().toString();
                    String qty = mTxtQty.getText().toString();
                    String unit = spinnerUnit.getText().toString();
                    String image = base64Image;

                    //create new item
                    Item myItem = new Item(item, qty, unit, image);
                    //add item to list
                    itemList.add(myItem);
                    //set list
                    mAdapter.setItemList(itemList);
                    //notify recycler view that there's a change
                    mAdapter.notifyDataSetChanged();
                    recyclerView.getAdapter().notifyDataSetChanged();

                    //clear views
                    mTxtQty.setText("");
                    spinnerName.setText("");
                    spinnerUnit.setText("");

                    mItemImageView.setImageResource(R.drawable.image_placeholder);
                    //reset image to default
                    base64Image = null;
                }


            }
        });

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mAdapter = new NewItemRecyclerViewAdapter(getActivity(), itemList, R.drawable.image_placeholder, getActivity().getLayoutInflater(), getFragmentManager());
        recyclerView.setAdapter(mAdapter);
        //scroll to last item
        //recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void setupItemSpinner(final AutoCompleteTextView autoCompleteTextView) {
        //dummy items to show in spinner
        final String[] ITEMS = new String[]{"Cupcake", "Brownies", "Tiramisu", "Cake", "Burger"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
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

    private void setupUnitSpinner(final AutoCompleteTextView autoCompleteTextView) {
        final String[] UNITS = new String[]{"pcs", "kg", "m"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line, UNITS);
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
                                    //the "inputStream" received here is the image itself
                                    inputStream = mContext.getContentResolver().openInputStream(uri);

                                    //resize image before saving
                                    Bitmap image = Utility.resizeImageFromFile(inputStream);

                                    //assign image to your imageview
                                    mItemImageView.setImageBitmap(image);

                                    //Convert bitmap to base64 to so you
                                    base64Image = Utility.convertBitmapToBase64(image);

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
