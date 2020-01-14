package it.uniba.di.easyhome.pr_ui.home;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniba.di.easyhome.Bill;
import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.User;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    final ArrayList<House> caseProprietario= new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FloatingActionButton fab= (getActivity().findViewById(R.id.fab_plus));
        fab.show();
        fab.setClickable(true);
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.pr_fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        /*homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        final Query query=rootRef.orderByChild("owner").equalTo("mdL9Gbzk0LR4I8NFiJov19yVk2a2");



        /*query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                caseProprietario.clear();
                Button b1= root.findViewById(R.id.button1);
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                       final House h=new House(ds.getValue(House.class).getName(),ds.getValue(House.class).getProprietario());
                        Query queryBill=query.orderByChild("bills");
                        queryBill.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                        h.addBill(ds.getValue(Bill.class).getTipo(),ds.getValue(Bill.class).getTotale(),ds.getValue(Bill.class).getDescrizione(),ds.getValue(Bill.class).getExpiration(),ds.getValue(Bill.class).isPayed());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Query queryOccupant=query.orderByChild("inquilini");
                        queryOccupant.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        caseProprietario.add(h);
                    }
                    OnComplete(b1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/




        return root;
    }


    private void OnComplete(Button b1) {


        b1.setText(caseProprietario.get(0).getName());


    }
}