package it.uniba.di.easyhome;


import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class InquilinoActivity extends AppCompatActivity  {

    FloatingActionButton fab1,fab2,fab3;
    Animation FabOpen,FabClose,FabClock,FabAntiClock;
    TextView boll;
    SharedPref sharedpref;
    boolean isOpen=false;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpref=new SharedPref(this);
        if(sharedpref.loadLang().equals("en")){
            this.setAppLocale("en");
        }else{
            this.setAppLocale("it");
        }
        setContentView(R.layout.activity_inquilino);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(sharedpref.loadNightModeState()==true){
            this.setTheme(R.style.darktheme);
        }

        fab1= findViewById(R.id.fab_plus);
        fab2= findViewById(R.id.fab2_plus);
        fab3= findViewById(R.id.fab3_plus);
        boll=findViewById(R.id.agg_boll);
        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClock= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabAntiClock= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    fab3.startAnimation(FabClose);
                    fab2.startAnimation(FabClose);
                    boll.startAnimation(FabClose);
                    fab1.startAnimation(FabAntiClock);
                    fab3.setClickable(false);
                    fab2.setClickable(false);
                    isOpen=false;
                }else{
                    fab3.startAnimation(FabOpen);
                    fab2.startAnimation(FabOpen);
                    boll.startAnimation(FabOpen);
                    fab1.startAnimation(FabClock);
                    fab3.setClickable(true);
                    fab2.setClickable(true);
                    isOpen=true;
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nameUserInquilino);
        User utente= (User) getIntent().getSerializableExtra("Utente");
        navUsername.setText(utente.getName());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.inq_nav_bills,
                R.id.inq_nav_tools, R.id.inq_nav_logout, R.id.inq_nav_info)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inquilino, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
    public  void setAppLocale(String localeCode){
        Resources res = getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf =res.getConfiguration();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        }else{
            conf.locale=new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf,dm);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        switch (item.getItemId()) {
            case R.id.inq_nav_bills:
                ValueEventListener vel= new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        NavController navController = Navigation.findNavController(InquilinoActivity.this, R.id.nav_host_fragment);
                        if(dataSnapshot.exists()){
                            for (DataSnapshot ds:dataSnapshot.getChildren()){
                                House h=new House(ds.getValue(House.class).getName(),ds.getValue(House.class).getOwner(),ds.getValue(House.class).getInquilini(),ds.getValue(House.class).getBills());
                                Log.v(TAG, h.getName() + " / " +h.getInquilini().size()+"/"+currentUser.getDisplayName());
                                for(String cod:h.getInquilini().keySet()){
                                    if(cod.equals(currentUser.getUid())){
                                        Bundle bundle=new Bundle();
                                        bundle.putString("nomeCasa",h.getName());
                                        navController.navigate(R.id.inq_nav_bills,bundle);
                                        DrawerLayout mDrawerLayout;
                                        mDrawerLayout = findViewById(R.id.drawer_layout);
                                        mDrawerLayout.closeDrawers();
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                rootRef.addListenerForSingleValueEvent(vel);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
