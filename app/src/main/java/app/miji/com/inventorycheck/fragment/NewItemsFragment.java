package app.miji.com.inventorycheck.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import app.miji.com.inventorycheck.activity.NewItemsActivity;
import app.miji.com.inventorycheck.activity.StockInActivity;
import app.miji.com.inventorycheck.activity.StockOutActivity;
import app.miji.com.inventorycheck.adapter.NewItemRecyclerViewAdapter;
import app.miji.com.inventorycheck.model.Delivery;
import app.miji.com.inventorycheck.model.Item;
import app.miji.com.inventorycheck.model.Sales;
import app.miji.com.inventorycheck.model.Transfer;
import app.miji.com.inventorycheck.utility.Utility;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewItemsFragment extends Fragment {


    private static final String LOG_TAG = NewItemsFragment.class.getSimpleName();

    private NewItemRecyclerViewAdapter mAdapter;
    private Context mContext;

    //if transfer is from stock in or stock out activity
    public static final String STOCK = "stock_activity";
    public int flag_stock;

    //text input
    private TextInputLayout mTxtInQty;
    private TextInputLayout mTxtInItem;
    private TextInputLayout mTxtInUnit;

    //common stock details
    private String mDate;
    private String mTime;
    private String mLocation;
    private String mReferenceNo;
    private List<Item> mItemList;
    private String mBase64Image = null;

    //Delivery details
    private Delivery mDelivery;
    private String mDeliveryMan;
    private String imageReceipt;

    //Transfer details
    private Transfer mTransfer;
    private String mToLocation;
    private String mFromLocation;
    private String mTransferId;

    //sales details
    private Sales mSales;
    private String mCustomer;

    //activity flags
    private String fromActivity = null;
    public static final String ACTVITY_FLAG = "activity_flag";
    public static final String DELIVERY = "delivery";
    public static final String SALES = "sales";
    public static final String TRANSFER = "transfer";


    public NewItemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_new_items, container, false);
        mContext = getContext();


        Intent intent = getActivity().getIntent();

        //check if intent is from mDelivery
        if (intent.hasExtra(NewItemsActivity.DELIVERY)) {
            Log.v(LOG_TAG, "------------intent from Delivery--------");
            mDelivery = intent.getParcelableExtra(NewItemsActivity.DELIVERY);
            fromActivity = intent.getStringExtra(NewItemsActivity.ACTIVITY);
            setDeliveryDetails(mDelivery);
        }

        if (intent.hasExtra(NewItemsActivity.TRANSFER)) {
            Log.v(LOG_TAG, "------------intent from Transfer--------");
            mTransfer = intent.getParcelableExtra(NewItemsActivity.TRANSFER);
            fromActivity = intent.getStringExtra(NewItemsActivity.ACTIVITY);
            setTransferDetails(mTransfer);
        }

        if (intent.hasExtra(STOCK)) {
            //used for determining which stock activity does transfer fragment fragment originate
            flag_stock = intent.getIntExtra(STOCK, 0);
        }

        if(intent.hasExtra(NewItemsActivity.SALES)){
            Log.v(LOG_TAG, "------------intent from Sales--------");
            mSales = intent.getParcelableExtra(NewItemsActivity.SALES);
            fromActivity = intent.getStringExtra(NewItemsActivity.ACTIVITY);
            setSalesDetails(mSales);
        }



        //create new list
        mItemList = new ArrayList<>();

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
        Utility.setupItemSpinner(getActivity(), spinnerName);
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
                    String image = mBase64Image;

                    //create new item
                    Item myItem = new Item(item, qty, unit, image);
                    //add item to list
                    mItemList.add(myItem);
                    //set list
                    mAdapter.setItemList(mItemList);
                    //notify recycler view that there's a change
                    mAdapter.notifyDataSetChanged();
                    recyclerView.getAdapter().notifyDataSetChanged();

                    //clear views
                    mTxtQty.setText("");
                    spinnerName.setText("");
                    spinnerUnit.setText("");

                    mItemImageView.setImageResource(R.drawable.image_placeholder);
                    //reset image to default
                    mBase64Image = null;
                }


            }
        });

        return view;
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        mAdapter = new NewItemRecyclerViewAdapter(getActivity(), mItemList, R.drawable.image_placeholder, getActivity().getLayoutInflater(), getFragmentManager());
        recyclerView.setAdapter(mAdapter);
        //scroll to last item
        //recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
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
                                    mBase64Image = Utility.convertBitmapToBase64(image);

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

    private void setDeliveryDetails(Delivery delivery) {
        mDate = delivery.getDate();
        mTime = delivery.getTime();
        mDeliveryMan = delivery.getDeliveryMan();
        mReferenceNo = delivery.getReferenceNo();
        mLocation = delivery.getLocation();
        imageReceipt = delivery.getImage();
    }

    private void setSalesDetails(Sales sales) {
        mDate = sales.getDate();
        mTime = sales.getTime();
        mCustomer = sales.getCustomer();
        mReferenceNo = sales.getReferenceNo();
        mLocation = sales.getLocation();
        imageReceipt = sales.getImage();
    }

    private void setTransferDetails(Transfer transfer) {
        mDate = transfer.getDate();
        mTime = transfer.getTime();
        mToLocation = transfer.getToLocation();
        mFromLocation = transfer.getFromLocation();
        mTransferId = transfer.getTransferID();
    }

    private void showDeliveryLogs() {
        Log.e(LOG_TAG, "DATE--------> " + mDate);
        Log.e(LOG_TAG, "TIME--------> " + mTime);
        Log.e(LOG_TAG, "DELIVERYMAN--------> " + mDeliveryMan);
        Log.e(LOG_TAG, "REFERENCE NO--------> " + mReferenceNo);
        Log.e(LOG_TAG, "LOCATION--------> " + mLocation);
        Log.e(LOG_TAG, "IMAGE--------> " + mBase64Image);
        Log.e(LOG_TAG, "ITEMS--------> :");

        for (Item item : mItemList) {
            Log.e(LOG_TAG, "--------> :" + item.getName().toString());
        }
    }

    private void showSalesLogs() {
        Log.e(LOG_TAG, "DATE--------> " + mDate);
        Log.e(LOG_TAG, "TIME--------> " + mTime);
        Log.e(LOG_TAG, "CUSTOMER--------> " + mCustomer);
        Log.e(LOG_TAG, "LOCATION--------> " + mLocation);
        Log.e(LOG_TAG, "REFERENCE NO--------> " + mReferenceNo);
        Log.e(LOG_TAG, "ITEMS--------> :");

        for (Item item : mItemList) {
            Log.e(LOG_TAG, "--------> :" + item.getName().toString());
        }
    }

    private void showTransferLogs() {
        Log.e(LOG_TAG, "DATE--------> " + mDate);
        Log.e(LOG_TAG, "TIME--------> " + mTime);
        Log.e(LOG_TAG, "TO LOCATION--------> " + mToLocation);
        Log.e(LOG_TAG, "FROM LOCATION--------> " + mFromLocation);
        Log.e(LOG_TAG, "TRANSFER ID--------> " + mTransferId);
        Log.e(LOG_TAG, "ITEMS--------> :");

        for (Item item : mItemList) {
            Log.e(LOG_TAG, "--------> :" + item.getName().toString());
        }
    }


    private void saveStockDetails() {
        Log.e(LOG_TAG, "From Activity ---------------->" + fromActivity);
        Intent intent;
        switch (fromActivity) {

            case DELIVERY:
                showDeliveryLogs();
                //create new mDelivery
                mDelivery = new Delivery(mDate, mTime, mLocation, mDeliveryMan, mReferenceNo, imageReceipt, mItemList);
                //save Delivery
                Utility.saveDelivery(mContext, mDelivery);
                //go to stock in activity
                intent = new Intent(getActivity(), StockInActivity.class);
                startActivity(intent);
                break;

            case TRANSFER:
                showTransferLogs();
                //create new delivery
                mTransfer = new Transfer(mDate, mTime, mTransferId, mFromLocation, mToLocation, mItemList);
                Log.e(LOG_TAG, "FROM ACTIVITY===============>" + flag_stock);
                //save transfer depending from Stock in/stock out activity
                if (flag_stock == 0) {
                    Utility.saveTransfer_StockIn(mContext, mTransfer);//from stock in
                    //go to stock in activity
                    intent = new Intent(getActivity(), StockInActivity.class);
                } else {
                    Utility.saveTransfer_StockOut(mContext, mTransfer);//from stock out
                    //go to stock in activity
                    intent = new Intent(getActivity(), StockOutActivity.class);
                }

                intent.putExtra(StockInActivity.TAB, 1); //Transfer fragment is in second tab
                startActivity(intent);
                break;

            case SALES:
                showSalesLogs();
                //create new sales
                mSales = new Sales(mDate,mTime,mCustomer,mReferenceNo,mLocation,imageReceipt, mItemList);
                //save Delivery
                Utility.saveSales(mContext, mSales);
                //go to stock in activity
                intent = new Intent(getActivity(), StockOutActivity.class);
                startActivity(intent);
                break;

        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                saveStockDetails();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
