package my.edu.um.fsktm.unihelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import my.edu.um.fsktm.unihelp.util.Preferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // debugging - reset login status
        // Preferences.setLogin(MainActivity.this, null);

        // go to LoginActivity if not logged in
        if (Preferences.getLogin(MainActivity.this) == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(intent.getFlags() |  Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }

        setContentView(R.layout.main_layout);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(
        this, R.id.nav_host_fragment
        );
        NavigationUI.setupWithNavController(navView, navController);
    }
}
