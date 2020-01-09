package it.uniba.di.easyhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "REGISTER";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]


        final EditText ET_mail= findViewById(R.id.usernameToRegister);
        final EditText ET_pass= findViewById(R.id.passToRegister);
        final EditText ET_repass= findViewById(R.id.repassToRegister);
        final EditText ET_name=findViewById(R.id.nameToRegister);
        final EditText ET_surname=findViewById(R.id.surnameToRegister);
        final RadioButton RB_Inq= findViewById(R.id.InqButton);
        final RadioButton RB_Pr= findViewById(R.id.PrButton);
        Button BT_register=findViewById(R.id.buttonToRegister);



        BT_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=ET_pass.getText().toString().trim();
                String repass=ET_repass.getText().toString().trim();
                if(pass.equals(repass)){
                    mAuth.createUserWithEmailAndPassword(ET_mail.getText().toString().trim(), ET_pass.getText().toString().trim())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    final String name=ET_name.getText().toString().trim();
                                    String mail=ET_mail.getText().toString().trim();
                                    String pass=ET_pass.getText().toString().trim();
                                    String repass=ET_repass.getText().toString().trim();
                                    String surname=ET_surname.getText().toString().trim();

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");

                                        if(!RB_Inq.isChecked() && !RB_Pr.isChecked()){
                                            Toast.makeText(RegisterActivity.this, "Choose Role!",
                                                    Toast.LENGTH_SHORT).show();
                                        }else if(name.matches("") || mail.matches("")
                                                    || pass.matches("") || surname.matches("")
                                                    || repass.matches("")){
                                            Toast.makeText(RegisterActivity.this, "Compile all fields",
                                                    Toast.LENGTH_SHORT).show();
                                        } else{
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                            if(RB_Inq.isChecked()){
                                                writeNewUser("6",mail,pass,name,surname,"I");
                                            }else{
                                                writeNewUser("6", mail,pass,name,surname,"P");
                                            }


                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Registration failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                }
                            });
                }else {
                    Toast.makeText(RegisterActivity.this, "Different Password.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }


    private void writeNewUser(String userId, String email,String pass, String name, String surname, String role) {
        User user = new User(email, pass,name,surname,role);

        mDatabase.child("users").child(userId).setValue(user);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}
