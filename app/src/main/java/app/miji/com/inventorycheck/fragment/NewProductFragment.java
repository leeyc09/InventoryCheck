package app.miji.com.inventorycheck.fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.widget.PlaceholderImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewProductFragment extends Fragment {

    public NewProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_new_product, container, false);

        PlaceholderImageView imageProduct = (PlaceholderImageView) view.findViewById(R.id.img_receipt);
        EditText txtProdCode = (EditText) view.findViewById(R.id.txt_prod_code);
        EditText txtBarcode = (EditText) view.findViewById(R.id.txt_barcode);
        final EditText txtName = (EditText) view.findViewById(R.id.txt_name);
        EditText txtDescription = (EditText) view.findViewById(R.id.txt_description);
        EditText txtQuantity = (EditText) view.findViewById(R.id.txt_qty);
        EditText txtPrice = (EditText) view.findViewById(R.id.txt_price);
        EditText txtLowStock = (EditText) view.findViewById(R.id.txt_low_stock);
        EditText txtNotes = (EditText) view.findViewById(R.id.txt_notes);
        final TextInputLayout txtInName = (TextInputLayout) view.findViewById(R.id.input_name);
        ImageView imgScan = (ImageView) view.findViewById(R.id.img_scan);
        MaterialBetterSpinner spinnerUnit = (MaterialBetterSpinner) view.findViewById(R.id.spinner_unit);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        //setup fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //txtName is required
                int name = txtName.getText().toString().length();
                if(name!=0){
                    txtInName.setErrorEnabled(false);
                    //TODO insert fields to database
                }else{
                    txtInName.setErrorEnabled(true);
                    txtInName.setError(getString(R.string.required_field));
                }
            }
        });



        return view;
    }
}
