package aara.tech.rootless_auto_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import aara.tech.rootless_auto_admin.ui.Fragments.BillingsFragment;
import aara.tech.rootless_auto_admin.ui.Fragments.BookingsFragment;
import aara.tech.rootless_auto_admin.ui.Fragments.CustomerFragment;
import aara.tech.rootless_auto_admin.ui.Fragments.InventoryFragment;
import aara.tech.rootless_auto_admin.ui.Fragments.MechanicsFragment;
import aara.tech.rootless_auto_admin.ui.Fragments.MessagingsFragment;
import aara.tech.rootless_auto_admin.ui.Fragments.ProductFragment;
import aara.tech.rootless_auto_admin.ui.Fragments.ServiceCenterFragment;
import aara.tech.rootless_auto_admin.ui.Fragments.UserListFragment;
import aara.tech.rootless_auto_admin.ui.Fragments.VehicleFragment;
import aara.tech.rootless_auto_admin.utils.Commonhelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Commonhelper commonhelper;
    ActionBar actionBar;
    Fragment selectedFragment = null;
    TextView nav_username;
    String username;
    Context context;

    public void initView(){

       /* drawerLayout = findViewById(R.id.activity_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nv);*/

        commonhelper = new Commonhelper(this);
        //get username from sharedPreference
        username = commonhelper.getSharedPreferences("uname", null);
        //Set username to Nav_Header Username Textview
//        setNavHeaderUsername();
       /* actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_actionbar_gradient));
        }*/
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ServiceCenterFragment()).commit();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setVisibility(View.GONE);
//            toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_actionbar_gradient));
        //Setting Email on Nav Header...............................................................
        String email = commonhelper.getSharedPreferences("admin_email", null);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName =  navigationView.getHeaderView(0).findViewById(R.id.email);
        txtProfileName.setText(email);
        //..........................................................................................
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_service_center:
                selectedFragment = new ServiceCenterFragment();
                setActionBarTitle("Service Center");
//                Toast.makeText(MainActivity.this, "Service Center", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_vehicle:
                selectedFragment = new VehicleFragment();
                setActionBarTitle("Vehicle");
//                Toast.makeText(MainActivity.this, "Vehicle", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_mechanics:
                selectedFragment = new MechanicsFragment();
                setActionBarTitle("Mechanics");
//                Toast.makeText(MainActivity.this, "Oops, It's Under development", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_inventory:
                selectedFragment = new InventoryFragment();
                setActionBarTitle("Inventory");
//                Toast.makeText(MainActivity.this, "Oops, It's Under development", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_products:
                selectedFragment = new ProductFragment();
                setActionBarTitle("Products");
//                Toast.makeText(MainActivity.this, "Products", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_customers:
                selectedFragment = new CustomerFragment();
                setActionBarTitle("Customers");
//                Toast.makeText(MainActivity.this, "Oops, It's Under development", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_bookings:
                selectedFragment = new BookingsFragment();
                setActionBarTitle("Bookings");
//                Toast.makeText(MainActivity.this, "Oops, It's Under development", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_billings:
                selectedFragment = new BillingsFragment();
                setActionBarTitle("Billings");
                Toast.makeText(MainActivity.this, "Oops, It's Under development", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_messaging:
                selectedFragment = new MessagingsFragment();
                setActionBarTitle("Messaging");
                Toast.makeText(MainActivity.this, "Oops, It's Under development", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_users:
                selectedFragment = new UserListFragment();
                setActionBarTitle("Users");
                break;
            case R.id.nav_logout:
                if (commonhelper.ClearSharedPreference()) {
                    commonhelper.ShowMesseage("Log Out Successfull");
                    commonhelper.callintent(MainActivity.this, SplashActivity.class);
                } else {
                    commonhelper.ShowMesseage("Something Went Wrong");
                }
                break;
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;

    }
        /*
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
*/

    private void setActionBarTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
