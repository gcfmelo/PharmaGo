package br.com.phago.pharmago;

import android.app.ListActivity;
import android.os.Bundle;

public class AccountMenuActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we dont need a XML Layout for a ListActivity
        // setContentView(R.layout.activity_user_menu);
    }
}
