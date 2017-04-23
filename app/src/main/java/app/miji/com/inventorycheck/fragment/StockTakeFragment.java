package app.miji.com.inventorycheck.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.StockTakeRecyclerViewAdapter;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * A placeholder fragment containing a simple view.
 */
public class StockTakeFragment extends Fragment {


    StockTakeRecyclerViewAdapter mAdapter;

    public StockTakeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_take, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_items);

        int mColumnCount = getResources().getInteger(R.integer.list_stockTake_column_count);
        //set Layout Manager
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        setupRecyclerView(recyclerView);

        //bottom sheet
        final BottomSheetLayout bottomSheet = (BottomSheetLayout) view.findViewById(R.id.bottomsheet);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupBottomSheet(bottomSheet);
            }
        });

        return view;

    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mAdapter = new StockTakeRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private void setupBottomSheet(final BottomSheetLayout bottomSheet){

        MenuSheetView menuSheetView = new MenuSheetView(getActivity(), MenuSheetView.MenuType.LIST, "Create Stock Take", new MenuSheetView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
                if (bottomSheet.isSheetShowing()) {
                    bottomSheet.dismissSheet();
                }

                int id = item.getItemId();

                if (id == R.id.action_manual) {
                    //TODO: go to another activity or show a dialog box
                }
                if (id == R.id.action_scan) {
                    //TODO: go to another implicit activity for scanning
                }
                if (id == R.id.action_bluetooth_scan) {
                    //TODO: go to bluetooth scanning
                }

                return true;
            }
        });
        menuSheetView.inflateMenu(R.menu.menu_scan);
        bottomSheet.showWithSheetView(menuSheetView);

    }
}
