package it.uniba.di.easyhome;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SplashScreenActivity extends AppCompatActivity {


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]



        Handler handler = new Handler();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        AnimatedVectorDrawable animatedVectorDrawable =
                (AnimatedVectorDrawable) getDrawable(R.drawable.avd_anim);
        imageView.setImageDrawable(animatedVectorDrawable);
        animatedVectorDrawable.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {}
        }, 4000);

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {//controlla le credenziali all'avvio dell'app
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]
    public void  updateUI(FirebaseUser account) {// controlla se l'utente ha già fatto l'accesso e se è vero salta il form login



        final FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference query= rootRef.child("users");

        if(account!=null) {

            ValueEventListener eventListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.getValue(User.class).getEmail().equalsIgnoreCase(currentUser.getEmail())){
                            Toast.makeText(SplashScreenActivity.this, "U Signed In successfully " +ds.getValue(User.class).getName(), Toast.LENGTH_LONG).show();
                            if (ds.getValue(User.class).getRole().equalsIgnoreCase("P")) {
                                startActivity(new Intent(SplashScreenActivity.this, ProprietarioActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashScreenActivity.this, InquilinoActivity.class));
                                finish();

                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            query.addListenerForSingleValueEvent(eventListener);




        }else {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }

    }
}
