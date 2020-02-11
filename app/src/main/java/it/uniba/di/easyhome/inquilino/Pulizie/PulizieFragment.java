package it.uniba.di.easyhome.inquilino.Pulizie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;

public class PulizieFragment extends Fragment {
     FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pulizie_fragment, container, false);
        final Spinner mySpinner = (Spinner) root.findViewById(R.id.spinnerDay);
        final Spinner spinnerInq = (Spinner) root.findViewById(R.id.spinnerInq);
        final TextView d= (TextView) root.findViewById(R.id.desc);
        final Bundle bundle=getArguments();
        mySpinner.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1 ,getResources().getStringArray(R.array.giorni) ));


        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
         final List<String> inqu = new ArrayList<String>();

        FirebaseDatabase.getInstance().getReference("houses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        House h = new House(ds.getValue(House.class).getName(), ds.getValue(House.class).getOwner(), ds.getValue(House.class).getInquilini(), ds.getValue(House.class).getBills());
                        if(h.getName().equalsIgnoreCase(bundle.getString("nomeCasa"))){
                            for (String inq : h.getInquilini().keySet()) {
                                inqu.add(inq);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        spinnerInq.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1 ,inqu ));




        return  root;
    }

    }


