package it.uniba.di.easyhome.proprietario.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;

public class AddHouseFragment extends Fragment {
     FirebaseAuth mAuth;

    public static AddHouseFragment newInstance() {
        return new AddHouseFragment();
    }
    private DatabaseReference mDatabase;
    String home;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_casa_fragment, container, false);
        final FloatingActionButton back= (getActivity().findViewById(R.id.fab_plus));
        back.setVisibility(View.GONE);
        final Bundle bundle=getArguments();
        final Button btSendt=(Button) root.findViewById(R.id.HomeSend);
        //dichiarazione delle edit
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final EditText casa= root.findViewById(R.id.casa);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                btSendt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        home = casa.getText().toString();
                        if (home.equals("")){
                            AlertDialog.Builder popUpAlert = new AlertDialog.Builder(getContext());
                            popUpAlert.setTitle(getResources().getString(R.string.avviso));
                            popUpAlert.setIcon(getResources().getDrawable(R.drawable.alert));
                            popUpAlert.setMessage(getResources().getString(R.string.insert_homename));

                            popUpAlert.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            popUpAlert.show();
                        }else{
                            writeNewHome(home);
                            Toast.makeText(getActivity(), getString(R.string.add_home),Toast.LENGTH_SHORT).show();
                        }


                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        rootRef.addListenerForSingleValueEvent(vel);

        return root;

    }

    private void writeNewHome(String home ) {
        String owner;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        owner=mAuth.getUid();
        House casa = new House(home,owner, UUID.randomUUID().toString().substring(0,5));
        mDatabase.child("houses").push().setValue(casa);
    }

}


