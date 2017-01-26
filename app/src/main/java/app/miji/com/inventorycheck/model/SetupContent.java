package app.miji.com.inventorycheck.model;

import java.util.ArrayList;
import java.util.List;


public class SetupContent {

    public static final List<SetupItem> SETUP_ITEMS = new ArrayList<>();

    private static String[] titles = {
            "Location",
            "Unit",
            "Currency"
    };


    static {
        // Add some home items.
        for (int i = 0; i <= titles.length - 1; i++) {
            addSetupItem(createSetupItem(titles[i]));
        }
    }


    private static void addSetupItem(SetupItem item) {
        SETUP_ITEMS.add(item);
    }


    private static SetupItem createSetupItem(String title) {
        return new SetupItem(title);
    }

    public static class SetupItem {
        public final String title;

        public SetupItem(String title) {
            this.title = title;
        }
    }
}
