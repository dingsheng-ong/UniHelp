package my.edu.um.fsktm.unihelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import my.edu.um.fsktm.unihelp.ui.LoginActivity;
import my.edu.um.fsktm.unihelp.util.Preferences;

public class MainActivity extends AppCompatActivity {

    private View.OnClickListener logout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Preferences.setLogin(MainActivity.this, null);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // go to LoginActivity if not logged in
        if (Preferences.getLogin(MainActivity.this) == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(intent.getFlags() |  Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } else {
            setContentView(R.layout.main_layout);
            setupDrawer();

            Toolbar toolbar = findViewById(R.id.main_toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            BottomNavigationView navView = findViewById(R.id.nav_view);
            NavController navController = Navigation.findNavController(
                    this, R.id.nav_host_fragment
            );
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    private void setupDrawer() {
        String[] credential = Preferences.getLogin(MainActivity.this).split("/");
        String name = credential[2];
        String email = credential[1];

        TextView tvName = findViewById(R.id.tv_name);
        TextView tvEmail = findViewById(R.id.tv_email);
        tvName.setText(name);
        tvEmail.setText(email);

        findViewById(R.id.btn_logout).setOnClickListener(logout);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
