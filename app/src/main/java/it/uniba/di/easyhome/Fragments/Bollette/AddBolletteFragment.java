package it.uniba.di.easyhome.Fragments.Bollette;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import it.uniba.di.easyhome.Bill;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.proprietario.home.HomeFragment;
import it.uniba.di.easyhome.proprietario.homecard.HomeCardFragment;

public class AddBolletteFragment extends Fragment {
    DatePickerDialog dpd;
     DatabaseReference mDatabase;
     String pay;
     String type;
     String desc;
     String tot;
     String date;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_bollette_fragment, container, false);
        final FloatingActionButton back= (getActivity().findViewById(R.id.fab2_plus));

        final TextView textIndietro= (TextView) getActivity().findViewById(R.id.agg_boll);
        final Calendar c=Calendar.getInstance();
        final Spinner mySpinner = (Spinner) root.findViewById(R.id.spinner);
        final Spinner spinnerPay = (Spinner) root.findViewById(R.id.spinnerPay);
        final TextView data=(TextView) root.findViewById(R.id.data);
        textIndietro.setText(getResources().getString(R.string.back));

        final Button dataBt=(Button) root.findViewById(R.id.dataBt);
        //dichiarazione delle edit
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final EditText totale= root.findViewById(R.id.number);
        final EditText descrizione=root.findViewById(R.id.desc);
        final Button add= root.findViewById(R.id.send);

        back.setImageDrawable(getResources().getDrawable(R.drawable.indietro));

        mySpinner.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1 ,getResources().getStringArray(R.array.tipo) ));
        spinnerPay.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1 ,getResources().getStringArray(R.array.pay) ));
        dataBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day=c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);
                dpd=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        data.setText(dayOfMonth+"/"+(month+1)+"/"+ year);
                    }
                },day,month,year);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            final Bundle bd=getArguments();
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("nomeCasa",bd.getString("nomeCasa"));
                HomeCardFragment homeCardFragment = new HomeCardFragment();
                homeCardFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(new HomeFragment(), "ListaCase").addToBackStack(HomeFragment.class.getName());
                fragmentTransaction.replace(R.id.nav_host_fragment, homeCardFragment, "PROVA");
                fragmentTransaction.commit();

            }

        });
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Bundle bundle=getArguments();
                add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pay = spinnerPay.getSelectedItem().toString();
                                    type = mySpinner.getSelectedItem().toString();
                                    desc = descrizione.getText().toString();
                                    tot = totale.getText().toString();
                                    date = data.getText().toString();
                                    if(pay.equals("No")){
                                  pay="false";
                                    }else{
                                        pay="true";
                                    }
                                    String pr=(bundle.getString("Casa"));
                                    writeNewBill(date, pay, type, desc, tot,pr);

                                    Toast.makeText(getActivity(), pr,
                                            Toast.LENGTH_SHORT).show();
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

    private void writeNewBill(String date, String pay ,String type, String desc, String tot,String casa ) {
        Bill bill = new Bill( type,  tot,  desc,  date,  pay);
        mDatabase.child("houses").child(casa).child("bills").push().setValue(bill);
    }

}
