package my.id.satria.safarea.helpers;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import my.id.satria.safarea.R;

/**
 * Kelas bantuan untuk mengaktifkan kastem toolbar atau actionbar
 */
public class ToolbarHelper {

    private Toolbar toolbarMenu;
    private TextView toolbarTitle;
    private AppCompatActivity root;

    /**
     * Konstruktor
     * @param root AppCompatActivity
     */
    public ToolbarHelper(AppCompatActivity root) {
        this.root = root;
    }

    /**
     * Atur custom toolbar
     */
    public void initToolbar() {
        toolbarMenu = root.findViewById(R.id.toolbarMenu);
        root.setSupportActionBar(toolbarMenu);

        if(root.getSupportActionBar() != null) {
            root.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
            root.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            root.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        try {
            toolbarTitle = toolbarMenu.findViewById(R.id.toolbar_title);
        } catch (NullPointerException e) {}
    }

    public void setToolbarTitle(String title) {
        try {
            toolbarTitle.setText(title);
        } catch (NullPointerException e) {}
    }



}
