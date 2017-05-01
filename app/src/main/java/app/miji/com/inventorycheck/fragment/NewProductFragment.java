package app.miji.com.inventorycheck.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.activity.ProductActivity;
import app.miji.com.inventorycheck.adapter.LocationFirebaseAdapter;
import app.miji.com.inventorycheck.adapter.ProductFirebaseAdapter;
import app.miji.com.inventorycheck.model.Product;
import app.miji.com.inventorycheck.utility.Utility;
import app.miji.com.inventorycheck.widget.PlaceholderImageView;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewProductFragment extends Fragment {
    private static final String LOG_TAG = NewProductFragment.class.getSimpleName();

    private ProductFirebaseAdapter mAdapter;

    //firebase database variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    private List<Product> list;

    public NewProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_product, container, false);

        //Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("product");
        //The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners.
        mDatabaseReference.keepSynced(true);


        final PlaceholderImageView imageProduct = (PlaceholderImageView) view.findViewById(R.id.img_receipt);
        final EditText txtProdCode = (EditText) view.findViewById(R.id.txt_prod_code);
        final EditText txtBarcode = (EditText) view.findViewById(R.id.txt_barcode);
        final EditText txtName = (EditText) view.findViewById(R.id.txt_name);
        final EditText txtDescription = (EditText) view.findViewById(R.id.txt_description);
        final EditText txtPrice = (EditText) view.findViewById(R.id.txt_price);
        final EditText txtLowStock = (EditText) view.findViewById(R.id.txt_low_stock);
        final EditText txtNotes = (EditText) view.findViewById(R.id.txt_notes);
        final TextInputLayout txtInName = (TextInputLayout) view.findViewById(R.id.input_name);
        ImageView imgScan = (ImageView) view.findViewById(R.id.img_scan);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        //setup fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String productCode = txtProdCode.getText().toString();
                String barcode = txtBarcode.getText().toString();
                String productName = txtName.getText().toString();
                String description = txtDescription.getText().toString();
                String price = txtPrice.getText().toString();
                String lowStock = txtLowStock.getText().toString();
                String notes = txtNotes.getText().toString();
                float quantity = 0;
                String unit = null;
                //TODO add image
                String image = null;


                //Name is required
                int name = txtName.getText().toString().length();

                if (name != 0) {
                    txtInName.setErrorEnabled(false);

                    //create new product
                    Product product = new Product(productCode,barcode,productName,description,price,lowStock,notes,image, quantity,unit);

                    //here
                    //Utility.saveProduct(getContext(), product);

                    //add to firebase database
                    mDatabaseReference.push().setValue(product);

                    //Go to product List
                    Intent intent = new Intent(getActivity(), ProductActivity.class);
                    startActivity(intent);
                } else {
                    txtInName.setErrorEnabled(true);
                    txtInName.setError(getString(R.string.required_field));
                }
            }
        });


        //show image picker
        imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start TedBottomPicker
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getActivity())
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                //Do something with selected uri
                                InputStream inputStream;
                                try {
                                    inputStream = getActivity().getContentResolver().openInputStream(uri);
                                    //the "image" received here is the image itself
                                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                                    //assign image to your imageview
                                    imageProduct.setImageBitmap(image);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .create();

                tedBottomPicker.show(getActivity().getSupportFragmentManager());
            }
        });


        return view;
    }
}
