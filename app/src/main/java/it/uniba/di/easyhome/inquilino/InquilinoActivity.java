package it.uniba.di.easyhome.inquilino;


import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.SharedPref;
import it.uniba.di.easyhome.User;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class InquilinoActivity extends AppCompatActivity  {

    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final int LOCATION = 1;
    SharedPref sharedpref;
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
        if(sharedpref.loadNightModeState()){
            this.setTheme(R.style.darktheme);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nameUserInquilino);
        User utente= (User) getIntent().getSerializableExtra("Utente");
        navUsername.setText(utente.getName());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_annunci, R.id.inq_nav_bills,
                R.id.inq_nav_tools, R.id.inq_nav_logout, R.id.inq_nav_info)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        checkWiFi();

    }

    @Override
    protected void onStart() {
        checkWiFi();
        super.onStart();
    }

    public void onPause() {
        checkWiFi();
        super.onPause();
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
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        switch (item.getItemId()) {
            case R.id.inq_nav_bills:
                ValueEventListener vel= new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        NavController navController = Navigation.findNavController(InquilinoActivity.this, R.id.nav_host_fragment);
                        if(dataSnapshot.exists()){
                            for (DataSnapshot ds:dataSnapshot.getChildren()){
                                House h=new House(
                                        ds.getValue(House.class).getName()
                                        ,ds.getValue(House.class).getOwner()
                                        ,ds.getValue(House.class).getInquilini()
                                        ,ds.getValue(House.class).getBills()
                                        ,ds.getValue(House.class).getSsid());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == LOCATION){
            //User allowed the location and you can read it now
            tryToReadSSID();
        }
    }
    private String tryToReadSSID() {
        //If requested permission isn't Granted yet

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return "bo";
            }else{//Permission already granted
                WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
                if (wifiManager != null && wifiManager.getConnectionInfo() != null) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                        return  wifiInfo.getBSSID();//Here you can access your SSID
                    }
                }

            }

        return "";

    }

    public  void checkWiFi(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ValueEventListener vel= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        House h=new House(
                                ds.getValue(House.class).getName()
                                ,ds.getValue(House.class).getOwner()
                                ,ds.getValue(House.class).getInquilini()
                                ,ds.getValue(House.class).getBills()
                                ,ds.getValue(House.class).getSsid());
                        for(String cod:h.getInquilini().keySet()){
                            if(cod.equals(currentUser.getUid())) {
                                if(!(ds.hasChild("ssid"))){
                                    rootRef.child("ssid").setValue(tryToReadSSID());
                                }else{
                                    if (h.getSsid().equals(tryToReadSSID())) {
                                        Log.v(TAG, "uguale");
                                        DatabaseReference referenceSameSSID = FirebaseDatabase.getInstance().getReference("houses/" + ds.getKey() + "/inquilini/" + cod);
                                        referenceSameSSID.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                referenceSameSSID.setValue("true");
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        DatabaseReference referenceNotSameSSID = FirebaseDatabase.getInstance().getReference("houses/" + ds.getKey() + "/inquilini/" + cod);
                                        referenceNotSameSSID.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                referenceNotSameSSID.setValue("false");
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }}
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
    }
}
