package it.uniba.di.easyhome.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.di.easyhome.inquilino.InquilinoActivity;
import it.uniba.di.easyhome.LoginActivity;
import it.uniba.di.easyhome.proprietario.ProprietarioActivity;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.User;

public class Logout extends Fragment {


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    User user;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(getActivity().equals(ProprietarioActivity.class)) {
            FloatingActionButton fab = (getActivity().findViewById(R.id.fab_plus));
            fab.hide();
            fab.setClickable(false);}
        final View root = inflater.inflate(R.layout.logout_proprietario, container, false);
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
                    if(dataSnapshot.exists()){
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.getValue(User.class).getEmail().equalsIgnoreCase(currentUser.getEmail())) {
                                user = ds.getValue(User.class);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle(R.string.pop_up_logout_title).
                                        setMessage(user.getName()+getString(R.string.pop_up_logout_text));
                                builder.setPositiveButton(R.string.pop_up_logout_yes,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                FirebaseAuth.getInstance().signOut();
                                                Intent i = new Intent(getActivity(),
                                                        LoginActivity.class);
                                                startActivity(i);
                                                getActivity().finish();

                                            }
                                        });
                                builder.setNegativeButton(R.string.pop_out_logout_no,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (user.getRole().equalsIgnoreCase("P")) {
                                                    startActivity(new Intent(getActivity(), ProprietarioActivity.class).putExtra("Utente",user));
                                                    getActivity().finish();
                                                } else {
                                                    startActivity(new Intent(getActivity(), InquilinoActivity.class).putExtra("Utente",user));
                                                    getActivity().finish();

                                                }
                                            }
                                        });
                                AlertDialog alert11 = builder.create();
                                alert11.show();
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
        return root;
    }
}