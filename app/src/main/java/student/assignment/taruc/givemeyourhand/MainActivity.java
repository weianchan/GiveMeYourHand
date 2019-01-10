package student.assignment.taruc.givemeyourhand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private FirebaseAuth mAuth;



    private TextView navName;
    private TextView navEmail;
    private ImageView navProfile;
    private ProgressBar loadingBar;

    private static FragmentManager fragmentManager;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        navName = header.findViewById(R.id.current_username);
        navEmail = header.findViewById(R.id.current_user_email);
        navProfile = header.findViewById(R.id.current_user_profile);

        fragmentManager  = getSupportFragmentManager();

        loadingBar = findViewById(R.id.main_loadingbar);

        navName.setOnClickListener(this);
        navEmail.setOnClickListener(this);
        navProfile.setOnClickListener(this);
        if(savedInstanceState == null){
            fragmentManager.beginTransaction().replace(R.id.main_fragment_container, new ListFragment()).commit();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        loadingBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        currentUser = mAuth.getCurrentUser();
        updateUI();
        loadingBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void updateUI() {
        if(currentUser!= null){

            navName.setText(currentUser.getDisplayName());
            navEmail.setText(currentUser.getEmail());

            if(currentUser.getPhotoUrl()!=null){
                Picasso.get().load(currentUser.getPhotoUrl()).fit().centerCrop()
                        .placeholder(R.drawable.ic_image_24dp)
                        .error(R.drawable.ic_image_24dp)
                        .into(navProfile);

            }







        }
        else{
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
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
    

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragmentManager.beginTransaction().replace(R.id.main_fragment_container, new ListFragment()).addToBackStack(null).commit();
        }  else if (id == R.id.new_post) {
            fragmentManager.beginTransaction().replace(R.id.main_fragment_container, new UploadPostFragment()).addToBackStack(null).commit();
        }
        else if(id == R.id.own_post){
            fragmentManager.beginTransaction().replace(R.id.main_fragment_container, new OwnFragment()).addToBackStack(null).commit();

        }
        else if(id == R.id.nav_setting) {
            fragmentManager.beginTransaction().replace(R.id.main_fragment_container, new SettingFragment()).addToBackStack(null).commit();

        }else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.main_fragment_container, new AboutFragment()).addToBackStack(null).commit();

        } else if (id == R.id.sign_out) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();

        } else if (id == R.id.exit_app) {
            finish();
            moveTaskToBack(true);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.current_username:
            case R.id.current_user_profile:
            case R.id.current_user_email:
                fragmentManager.beginTransaction().replace(R.id.main_fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                onBackPressed();
                break;



        }
    }
}
