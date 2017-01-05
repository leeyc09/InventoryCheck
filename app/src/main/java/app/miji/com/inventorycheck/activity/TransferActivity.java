package app.miji.com.inventorycheck.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import app.miji.com.inventorycheck.R;

public class TransferActivity extends AppCompatActivity {

    //for determining floating label for spinner location
    //FLAG 0: from StockInActivity
    //FLAG 1: from StockOutActivity
    public static final String FLAG = "spinner_label";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
