package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Item;
import app.miji.com.inventorycheck.utility.Utility;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * Adapter for adding items in the list
 */

public class NewItemRecyclerViewAdapter extends RecyclerView.Adapter<NewItemRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = NewItemRecyclerViewAdapter.class.getSimpleName();
    private List<Item> itemList;
    private int defaultImageId;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String base64Image = null;
    private FragmentManager fragmentManager;


    public NewItemRecyclerViewAdapter(Context context, List<Item> itemList, int defaultImageId, LayoutInflater layoutInflater, FragmentManager fm) {
        this.itemList = itemList;
        this.defaultImageId = defaultImageId;
        this.mContext = context;
        this.mLayoutInflater = layoutInflater;
        this.fragmentManager = fm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = itemList.get(position);

        //get values
        String name = itemList.get(position).getName();
        String qty = itemList.get(position).getQty() + " " + itemList.get(position).getUnit();
        String image = itemList.get(position).getImage();

        //set text values
        holder.mTxtName.setText(name);
        holder.mTxtQty.setText(qty);

        //set image
        if (image != null) {
            //convert base64
            Bitmap bitmap = Utility.decodeBase64Image(image);
            //display image
            holder.mItemImageView.setImageBitmap(bitmap);
        } else {
            //no image, display default image
            holder.mItemImageView.setImageResource(defaultImageId);
        }


        //delete item
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove item in list
                itemList.remove(position);
                notifyDataSetChanged();
            }
        });


        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show edit dialog
                showEditDialog(position);
            }
        });


    }

    private void showEditDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // Set the dialog title
        builder.setTitle(R.string.edit_item);

        // Get the layout inflater
        LayoutInflater inflater = mLayoutInflater;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_edit_item, null);
        builder.setView(view);

        //inflate views
        final ImageView mItemImageView = (ImageView) view.findViewById(R.id.imageView);
        final TextInputLayout mTxtInItem = (TextInputLayout) view.findViewById(R.id.input_item);
        final TextInputLayout mTxtInQty = (TextInputLayout) view.findViewById(R.id.input_qty);
        final TextInputLayout mTxtInUnit = (TextInputLayout) view.findViewById(R.id.input_unit);
        final TextView mTxtQty = (TextView) view.findViewById(R.id.txt_qty);
        final AutoCompleteTextView spinnerName = (AutoCompleteTextView) view.findViewById(R.id.spinner_name);
        final AutoCompleteTextView spinnerUnit = (AutoCompleteTextView) view.findViewById(R.id.spinner_unit);


        //setup drop down list items
        setupItemSpinner(spinnerName);
        setupUnitSpinner(spinnerUnit);
        setupImagePicker(mItemImageView);

        //set original values to view
        spinnerName.setText(itemList.get(position).getName());
        spinnerUnit.setText(itemList.get(position).getUnit().toLowerCase());
        mTxtQty.setText(itemList.get(position).getQty());

        //set image
        String image = itemList.get(position).getImage();
        if (image != null) {
            //convert base64
            Bitmap bitmap = Utility.decodeBase64Image(image);
            //display image
            mItemImageView.setImageBitmap(bitmap);
        }


        //Save button
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //validate form
                boolean isvalid = Utility.validateItemInput(mContext, spinnerName, mTxtQty, spinnerUnit, mTxtInItem, mTxtInQty, mTxtInUnit);
                if (isvalid) {

                    String item = spinnerName.getText().toString();
                    String qty = mTxtQty.getText().toString();
                    String unit = spinnerUnit.getText().toString();
                    String image = base64Image;

                    //create new item
                    Item myItem = new Item(item, qty, unit, image);
                    //edit item on list
                    itemList.set(position, myItem);
                    notifyDataSetChanged();
                }

            }
        });


        //Cancel button
        builder.setCancelable(false);
        builder.setNegativeButton(mContext.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });


        //Create and show dialog
        AlertDialog alertDialogAndroid = builder.create();
        alertDialogAndroid.show();


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

                tedBottomPicker.show(fragmentManager);
            }
        });

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

    @Override
    public int getItemCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }


    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mItemImageView;
        final TextView mTxtName;
        final TextView mTxtQty;
        final ImageView imgEdit;
        final ImageView imgDelete;

        Item mItem;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemImageView = (ImageView) itemView.findViewById(R.id.imageView);
            mTxtQty = (TextView) itemView.findViewById(R.id.txt_qty);
            mTxtName = (TextView) itemView.findViewById(R.id.txt_name);
            imgEdit = (ImageView) itemView.findViewById(R.id.img_edit);
            imgDelete = (ImageView) itemView.findViewById(R.id.img_delete);

        }
    }
}
