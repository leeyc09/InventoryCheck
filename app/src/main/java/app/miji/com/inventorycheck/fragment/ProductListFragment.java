package app.miji.com.inventorycheck.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.ProductRecyclerViewAdapter;
import app.miji.com.inventorycheck.model.Product;
import app.miji.com.inventorycheck.utility.Utility;

public class ProductListFragment extends Fragment {

    ProductRecyclerViewAdapter mAdapter;
    private List<Product> list;

    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        MaterialBetterSpinner spinnerLocation = (MaterialBetterSpinner) view.findViewById(R.id.spinner_location);
        TextInputLayout txtInItem = (TextInputLayout) view.findViewById(R.id.input_item);
        AutoCompleteTextView txtItem = (AutoCompleteTextView) view.findViewById(R.id.txt_item);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_items);


        //setup location spinner
        Utility.setupLocationSpinner(getActivity(), spinnerLocation);

        int mColumnCount = getResources().getInteger(R.integer.list_stockTake_column_count);
        //set Layout Manager
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        setupRecyclerView(recyclerView);
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        list = new ArrayList<>();
        list = Utility.getProductData(getContext());

        mAdapter = new ProductRecyclerViewAdapter(getContext(), list);
        recyclerView.setAdapter(mAdapter);
    }


}
