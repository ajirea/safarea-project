package my.id.satria.safarea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import my.id.satria.safarea.helpers.ToolbarHelper;

import android.os.Bundle;

public class SettingActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar();
    }

    /**
     * Handle tombol back pada toolbar ketika di klik
     * @return boolean
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void setToolbarTitle(String title) {
        toolbarHelper.setToolbarTitle(title);
    }

    public Toolbar getToolbar() {
        return toolbarHelper.getToolbar();
    }
}
