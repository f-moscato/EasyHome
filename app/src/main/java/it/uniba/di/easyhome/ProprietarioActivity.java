package it.uniba.di.easyhome;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;



public class ProprietarioActivity extends AppCompatActivity {
    FloatingActionButton fab1,fab2,fab3;
    Animation FabOpen,FabClose,FabClock,FabAntiClock;
    TextView boll;
    boolean isOpen=false;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proprietario);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nameUserProprietario);
        User utente= (User) getIntent().getSerializableExtra("Utente");
        navUsername.setText(utente.getName());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,
                R.id.pr_nav_tools, R.id.pr_nav_logout, R.id.pr_nav_info)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);// collega il menu laterale con il "selettore" dei fragment
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration); //crea l'hamburger per il menu laterale
        NavigationUI.setupWithNavController(navigationView, navController); // permette la selezione dei fragment(log out..)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.proprietario, menu);
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

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
