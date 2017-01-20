package app.miji.com.inventorycheck.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import app.miji.com.inventorycheck.adapter.DeliveryRecyclerViewAdapter;
import app.miji.com.inventorycheck.model.Delivery;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeliveryListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeliveryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private DeliveryRecyclerViewAdapter mAdapter;
    private List<Delivery> list;

    public DeliveryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryListFragment newInstance(String param1, String param2) {
        DeliveryListFragment fragment = new DeliveryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_list, container, false);

        //inflate views
        MaterialBetterSpinner spinnerLocation = (MaterialBetterSpinner) view.findViewById(R.id.spinner_location);
        AutoCompleteTextView txtItem = (AutoCompleteTextView) view.findViewById(R.id.txt_item);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_delivery);

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
        list = Utility.getDeliveryData(getContext());

        mAdapter = new DeliveryRecyclerViewAdapter(getActivity(), list);
        recyclerView.setAdapter(mAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
