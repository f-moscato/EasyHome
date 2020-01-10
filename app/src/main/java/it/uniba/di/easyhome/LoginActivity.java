package it.uniba.di.easyhome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login5);

        Button login= findViewById(R.id.B_login);
        Button register= findViewById(R.id.B_register);
        final EditText ET_email=findViewById(R.id.username_input);
        final EditText ET_password= findViewById(R.id.pass);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

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
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                            }
                        });
            }
        });
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


        Toast.makeText(this, "U Signed In successfully", Toast.LENGTH_LONG).show();
        /*FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
        final User utente=new User();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                utente.setEmail(u.getEmail());
                utente.setName(u.getName());
                utente.setPass(u.getPass());
                utente.setRole(u.getRole());
                utente.setSurname(u.getSurname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        rootRef.addValueEventListener(eventListener);

        if (utente.getRole().equals("P")) {
            startActivity(new Intent(this,ProprietarioActivity.class));
            finish();
        }else{
            startActivity(new Intent(this,InquilinoActivity.class));
            finish();

        }*/

        startActivity(new Intent(this,ProprietarioActivity.class));
        finish();

    }
}
