package it.uniba.di.easyhome.pr_ui.logout;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.di.easyhome.InquilinoActivity;
import it.uniba.di.easyhome.ProprietarioActivity;
import it.uniba.di.easyhome.SplashScreenActivity;
import it.uniba.di.easyhome.User;

public class LogoutViewModel extends ViewModel {

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    User user;

    public LogoutViewModel() {

    }

    public String getText() {
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        final FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference query = rootRef.child("users");

        if (currentUser != null) {

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(User.class).getEmail().equalsIgnoreCase(currentUser.getEmail())) {
                            user = ds.getValue(User.class);
                        }
                    }

                    onComplete(user.getName());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            };
            query.addListenerForSingleValueEvent(eventListener);

        }
        return user.getName();
    }


    public void onComplete(String name){
        user.setName(name);
    }
}
