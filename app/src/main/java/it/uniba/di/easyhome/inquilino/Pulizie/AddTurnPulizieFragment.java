package it.uniba.di.easyhome.inquilino.Pulizie;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.Pulizia;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.User;

public class AddTurnPulizieFragment extends Fragment {
    int flag=0;
    String code_puli;
    DatabaseReference mDatabase;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pulizie_fragment, container, false);
        final Spinner spinnerDay = (Spinner) root.findViewById(R.id.spinnerDay);
        final Spinner spinnerInq_1 = (Spinner) root.findViewById(R.id.spinnerInq);
        Button bt= (Button) root.findViewById(R.id.SendPuli);
        final Spinner spinnerInq_2 = (Spinner) root.findViewById(R.id.spinnerInq_2);
        EditText d=root.findViewById(R.id.desc);
        final Calendar c=Calendar.getInstance();
        final Bundle bundle=getArguments();
        spinnerDay.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1 ,getResources().getStringArray(R.array.giorni) ));
         final ArrayList<String> spinnerInq=new ArrayList<>();


        FirebaseDatabase.getInstance().getReference("houses").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        spinnerInq.clear();
                        spinnerInq.add("nessuno");
                        House h = new House(ds.getValue(House.class).getName(), ds.getValue(House.class).getOwner(), ds.getValue(House.class).getInquilini(), ds.getValue(House.class).getBills());
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
                                            spinnerInq.add(user.getName()+" "+user.getSurname());
                                                    break;

                                        }

                                        spinnerInq_1.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                                android.R.layout.simple_list_item_1 ,spinnerInq ));
                                        spinnerInq_2.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                                android.R.layout.simple_list_item_1 ,spinnerInq ));
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

bt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        flag=0;
        String day=spinnerDay.getSelectedItem().toString();
        String t_1=spinnerInq_1.getSelectedItem().toString();
        String t_2=spinnerInq_2.getSelectedItem().toString();
        String desc= d.getText().toString();
        if(t_1!="nessuno"){
        if(t_1!=t_2){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                        DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference("houses/"+ds.getKey()+"/pulizie");
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        rootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for(final DataSnapshot ds1 : dataSnapshot.getChildren()){
                                        Pulizia p = (ds1.getValue(Pulizia.class));
                                if(p.getDay().equals(day)) {
                                flag=12;
                                code_puli=ds1.getKey();
                                }
                                    }
                                    if(flag==12){
                                        AlertDialog.Builder v = new AlertDialog.Builder(getContext());
                                        v.setTitle(getResources().getString(R.string.avviso));
                                        v.setIcon(getResources().getDrawable(R.drawable.alert));
                                        v.setMessage(getResources().getString(R.string.messageDayCopy));
                                        v.setPositiveButton(getResources().getString(R.string.si), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Pulizia d = new Pulizia(day, t_1, t_2, desc);
                                                mDatabase.child("houses").child(ds.getKey()).child("pulizie").child(code_puli).setValue(d);
                                            }
                                        });
                                        v.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        v.show();
                                    }else{
                                        Pulizia d = new Pulizia(day, t_1, t_2, desc);
                                        mDatabase.child("houses").child(ds.getKey()).child("pulizie").push().setValue(d);
                                    }
                                }
                            }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError){
                        }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle(getResources().getString(R.string.avviso));
            alert.setIcon(getResources().getDrawable(R.drawable.alert));
            alert.setMessage(getResources().getString(R.string.turn2_error));
            alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
            }
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle(getResources().getString(R.string.avviso));
            alert.setIcon(getResources().getDrawable(R.drawable.alert));
            alert.setMessage(getResources().getString(R.string.turn1_error));
            alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }
});

        return  root;
    }
}


