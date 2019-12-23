package my.id.satria.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // activate custom toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // get logout button component
        Button btnLogout = findViewById(R.id.btnLogout);
        // set on click listener
        btnLogout.setOnClickListener(l -> {
            // start LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
        });

    }
}
