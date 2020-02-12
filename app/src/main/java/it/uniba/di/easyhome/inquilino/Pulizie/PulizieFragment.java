package it.uniba.di.easyhome.inquilino.Pulizie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.User;

public class PulizieFragment extends Fragment {
     FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pulizie_fragment, container, false);
        final Spinner mySpinner = (Spinner) root.findViewById(R.id.spinnerDay);
        final Spinner spinnerInq_1 = (Spinner) root.findViewById(R.id.spinnerInq);
        Button bt= (Button) root.findViewById(R.id.SendPuli);
        final Spinner spinnerInq_2 = (Spinner) root.findViewById(R.id.spinnerInq_2);
        EditText d=root.findViewById(R.id.desc);
        final Bundle bundle=getArguments();
        mySpinner.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1 ,getResources().getStringArray(R.array.giorni) ));
         final ArrayList<String> spinnerInq=new ArrayList<>();


        FirebaseDatabase.getInstance().getReference("houses").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        House h=new House(
                                ds.getValue(House.class).getName()
                                ,ds.getValue(House.class).getOwner()
                                ,ds.getValue(House.class).getInquilini()
                                ,ds.getValue(House.class).getBills()
                                ,ds.getValue(House.class).getSsid());
                        if(h.getName().equalsIgnoreCase(bundle.getString("nomeCasa"))){
                            for (String inq : h.getInquilini().keySet()) {
                                // ricerca degli inquilini presenti nella stessa casa
                                DatabaseReference refInquilini=FirebaseDatabase.getInstance().getReference("users/"+inq);
                                refInquilini.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds1:dataSnapshot.getChildren()){

                                                    User user=dataSnapshot.getValue(User.class);
                                                        // invio delle notifiche
                                            spinnerInq.add(user.getName());
                                                    break;

                                        }
                                        if(getActivity()!=null){
                                            spinnerInq_1.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                                    android.R.layout.simple_list_item_1 ,spinnerInq ));
                                            spinnerInq_2.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                                    android.R.layout.simple_list_item_1 ,spinnerInq ));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       // d.setText(p);

bt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});


        return  root;
    }

    }


