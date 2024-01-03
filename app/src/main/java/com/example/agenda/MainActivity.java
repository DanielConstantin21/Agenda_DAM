package com.example.agenda;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.agenda.fragments.AboutFragment;
import com.example.agenda.fragments.HomeFragment;
import com.example.agenda.fragments.UserFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
  //  private ArrayList<Calendar> calendars = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        if(savedInstanceState == null){
            currentFragment=new HomeFragment();
            openFragement();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void initComponents() {
        configNavigation();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(getSelectedItemEvent());
    }

    private void configNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private NavigationView.OnNavigationItemSelectedListener getSelectedItemEvent() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.nav_home){
                    currentFragment= new HomeFragment();
                } else
                    if(item.getItemId()==R.id.nav_user){
                    currentFragment= new UserFragment();
                    }
                    else if(item.getItemId()==R.id.nav_about){
                        currentFragment= new AboutFragment();
                    }
                    openFragement();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        };
    }

    private void openFragement() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_container,currentFragment)
                .commit();
    }
}