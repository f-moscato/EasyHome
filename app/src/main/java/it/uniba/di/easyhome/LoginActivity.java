package it.uniba.di.easyhome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import it.uniba.di.easyhome.inquilino.InquilinoActivity;
import it.uniba.di.easyhome.proprietario.ProprietarioActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN";
    private FirebaseAuth mAuth;
    private static final int LOCATION = 1;
    SharedPref sharedpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpref=new SharedPref(this);
        if(sharedpref.loadLang().equals("en")){
            this.setAppLocale("en");
        }else{
            this.setAppLocale("it");
        }
        if(sharedpref.loadNightModeState()){
            this.setTheme(R.style.darktheme);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION);}
        setContentView(R.layout.activity_login);
        Button login= findViewById(R.id.B_login);
        Button register= findViewById(R.id.B_register);
        final EditText ET_email=findViewById(R.id.username_input);
        final EditText ET_password= findViewById(R.id.pass);
        final RelativeLayout rlEmail=findViewById(R.id.rlEmail);
        final RelativeLayout rlPass=findViewById(R.id.rlPass);


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ET_email.getText().toString().equals("") || ET_password.getText().toString().equals("")){
                    rlEmail.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                    rlPass.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                }else{
                    mAuth.signInWithEmailAndPassword(ET_email.getText().toString().trim(),ET_password.getText().toString().trim())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        rlEmail.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                                        rlPass.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                }
                            });
                }

            }
        });
    }

    public  void setAppLocale(String localeCode){
        Resources res = getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf =res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }

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
                            Toast.makeText(LoginActivity.this, "U Signed In successfully " +ds.getValue(User.class).getName(), Toast.LENGTH_LONG).show();
                            if (ds.getValue(User.class).getRole().equalsIgnoreCase("P")) {
                                startActivity(new Intent(LoginActivity.this, ProprietarioActivity.class).putExtra("Utente",ds.getValue(User.class)));
                                finish();
                            } else {
                                startActivity(new Intent(LoginActivity.this, InquilinoActivity.class).putExtra("Utente",ds.getValue(User.class)));
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




        }

    }
}
