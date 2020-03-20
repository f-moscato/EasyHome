package it.uniba.di.easyhome;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "REGISTER";
    SharedPref sharedpref;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpref=new SharedPref(this);
        if(sharedpref.loadLang().equals("it")){
            this.setAppLocale("it");
        }else{
            this.setAppLocale("en");
        }
        if(sharedpref.loadNightModeState()){
            this.setTheme(R.style.darktheme);
        }
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
        RB_Inq.setChecked(false);
        final RadioButton RB_Pr= findViewById(R.id.PrButton);
        RB_Pr.setChecked(false);
        Button BT_register=findViewById(R.id.buttonToRegister);

        RelativeLayout rlEmail=findViewById(R.id.rlEmailRegistration);
        RelativeLayout rlPass=findViewById(R.id.rlPasswordRegistration);
        RelativeLayout rlRePass=findViewById(R.id.rlRePassRegistration);
        RelativeLayout rlName=findViewById(R.id.rlNameRegistration);
        RelativeLayout rlSurname=findViewById(R.id.rlSurnameRegistration);



        BT_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=ET_pass.getText().toString().trim();
                String repass=ET_repass.getText().toString().trim();
                final String name=ET_name.getText().toString().trim();
                String mail=ET_mail.getText().toString().trim();
                String surname=ET_surname.getText().toString().trim();

                rlName.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                rlSurname.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                rlEmail.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                rlPass.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                rlRePass.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));

                if(!name.equals("") && !mail.equals("") && !pass.equals("") && !repass.equals("") && !surname.equals("")){
                    if(pass.equals(repass)){
                        mAuth.createUserWithEmailAndPassword(ET_mail.getText().toString().trim(), ET_pass.getText().toString().trim())
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                                            } else {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                updateUI(user);

                                                if (RB_Inq.isChecked()) {
                                                    writeNewUser(user.getUid(), mail, pass, name, surname, "I");
                                                } else {
                                                    writeNewUser(user.getUid(), mail, pass, name, surname, "P");
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
                        rlPass.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                        rlRePass.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                        Toast.makeText(RegisterActivity.this, "Different Password", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }else{
                    if(name.equals("")){
                        rlName.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                    }
                    if(surname.equals("")){
                        rlSurname.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                    }
                    if(mail.equals("")){
                        rlEmail.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                    }
                    if(pass.equals("")){
                        rlPass.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                    }
                    if(repass.equals("")){
                        rlRePass.setBackground(getResources().getDrawable(R.drawable.red_border_rounded_cornwe));
                    }
                }

            }
        });

        RB_Inq.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked){
                    RB_Pr.setChecked(false);
                }
            }
        });

        RB_Pr.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked){
                    RB_Inq.setChecked(false);
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

    private void writeNewUser(String userId, String email,String pass, String name, String surname, String role ) {
        User user = new User(email,pass,name,surname,role);

        mDatabase.child("users").child(userId).setValue(user);
    }


    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this,LoginActivity.class);
            i.putExtra("fromRegister","yes");
            startActivity(i);
            finish();
        }
    }
}
