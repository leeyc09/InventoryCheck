package app.miji.com.inventorycheck.object;

import java.util.ArrayList;
import java.util.List;

import app.miji.com.inventorycheck.R;

public class HomeContent {

    public static final List<HomeItem> HOME_ITEMS = new ArrayList<>();

    private static Integer[] mThumbIds = {
            R.drawable.stockin,
            R.drawable.stockout,
            R.drawable.stocktake,
            R.drawable.adjust,
            R.drawable.inventory,
            R.drawable.report,
            R.drawable.warehouse
    };

    private static String[] titles = {
            "Stock In",
            "Stock Out",
            "Stock Take",
            "Inventory Adjustment",
            "Items",
            "Report",
            "Setup"
    };


    static {
        // Add some home items.
        for (int i = 0; i <= titles.length-1; i++) {
            addHomeItem(createHomeItem(mThumbIds[i], titles[i]));
        }
    }


    private static void addHomeItem(HomeItem item) {
        HOME_ITEMS.add(item);
    }


    private static HomeItem createHomeItem(Integer thumbnail, String title) {
        return new HomeItem(thumbnail, title);
    }

    public static class HomeItem {
        public final Integer thumbnail;
        public final String title;

        public HomeItem(Integer thumbnail, String title) {
            this.thumbnail = thumbnail;
            this.title = title;
        }
    }
}
