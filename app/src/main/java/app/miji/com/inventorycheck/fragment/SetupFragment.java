package app.miji.com.inventorycheck.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.SetupRecyclerViewAdapter;
import app.miji.com.inventorycheck.model.SetupContent;

public class SetupFragment extends Fragment {

    private SetupRecyclerViewAdapter mAdapter;

    public SetupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        //setup recycler view
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_setup_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mAdapter = new SetupRecyclerViewAdapter(getContext(), SetupContent.SETUP_ITEMS);
        recyclerView.setAdapter(mAdapter);
    }
}
