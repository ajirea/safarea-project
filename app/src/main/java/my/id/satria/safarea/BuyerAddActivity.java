package my.id.satria.safarea;

import androidx.appcompat.app.AppCompatActivity;
import my.id.satria.safarea.helpers.ToolbarHelper;

import android.os.Bundle;
import android.view.Menu;

public class BuyerAddActivity extends AppCompatActivity {
    private ToolbarHelper toolbarHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_add);

        // atur custom toolbar
        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar();
        toolbarHelper.setToolbarTitle(getString(R.string.text_add_buyer));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
