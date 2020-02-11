package it.uniba.di.easyhome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.firebase.iid.FirebaseInstanceId;

import it.uniba.di.easyhome.Notifiche.Token;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SplashScreenActivity extends AppCompatActivity {

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    String mUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        Handler handler = new Handler();
        ImageView imageView =  findViewById(R.id.imageView);
        AnimatedVectorDrawable animatedVectorDrawable =
                (AnimatedVectorDrawable) getDrawable(R.drawable.avd_anim);
        imageView.setImageDrawable(animatedVectorDrawable);
        animatedVectorDrawable.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                updateUI(currentUser);
                updateToken(FirebaseInstanceId.getInstance().getToken());
            }
        }, 800);


    }

    public void  updateUI(FirebaseUser account) {// controlla se l'utente ha già fatto l'accesso e se è vero salta il form login



        final FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference query= rootRef.child("users");

        if(account!=null) {


            mUID= currentUser.getUid();
            Log.v(TAG,"muid:"+mUID);

            //salvataggeio uid nel sharedPreferences
            SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor= sp.edit();
            editor.putString("Current_USERID", mUID);
            editor.apply();


            ValueEventListener eventListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.getValue(User.class).getEmail().equalsIgnoreCase(currentUser.getEmail())){
                            Toast.makeText(SplashScreenActivity.this, "U Signed In successfully " +ds.getValue(User.class).getName(), Toast.LENGTH_LONG).show();
                            if (ds.getValue(User.class).getRole().equalsIgnoreCase("P")) {
                                startActivity(new Intent(SplashScreenActivity.this, ProprietarioActivity.class).putExtra("Utente",ds.getValue(User.class)));
                                finish();
                            } else {
                                startActivity(new Intent(SplashScreenActivity.this, InquilinoActivity.class).putExtra("Utente",ds.getValue(User.class)));
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

    public void updateToken(String token){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
        it.uniba.di.easyhome.Notifiche.Token mToken= new Token(token);
        ref.child(mUID).setValue(mToken);
    }

}
