package it.uniba.di.easyhome.Fragments.Bollette;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import it.uniba.di.easyhome.Bill;
import it.uniba.di.easyhome.Notifiche.APIService;
import it.uniba.di.easyhome.Notifiche.Client;
import it.uniba.di.easyhome.Notifiche.Data;
import it.uniba.di.easyhome.Notifiche.Response;
import it.uniba.di.easyhome.Notifiche.Sender;
import it.uniba.di.easyhome.Notifiche.Token;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.SharedPref;
import it.uniba.di.easyhome.User;
import it.uniba.di.easyhome.proprietario.home.HomeFragment;
import it.uniba.di.easyhome.proprietario.homecard.HomeCardFragment;
import retrofit2.Call;
import retrofit2.Callback;

public class AddBolletteFragment extends Fragment {
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private APIService apiService;

    DatePickerDialog dpd;
     DatabaseReference mDatabase;
     String pay;
     String type;
     String desc;
     String tot;
     String date;
    SharedPref sharedpref;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_bollette_fragment, container, false);
        final FloatingActionButton back= (getActivity().findViewById(R.id.fab_plus));
        sharedpref=new SharedPref(getContext());
        apiService= Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        final Calendar c=Calendar.getInstance();
        final Spinner mySpinner = (Spinner) root.findViewById(R.id.spinner);
        final Spinner spinnerPay = (Spinner) root.findViewById(R.id.spinnerPay);
        final TextView data=(TextView) root.findViewById(R.id.data);

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
        if(sharedpref.loadNightModeState()){
            spinnerPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
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
                                    String codCasa=(bundle.getString("Casa"));

                                    if(date.equals("")){
                                        AlertDialog.Builder popUpAlert = new AlertDialog.Builder(getContext());
                                        popUpAlert.setTitle(getResources().getString(R.string.avviso));
                                        popUpAlert.setIcon(getResources().getDrawable(R.drawable.alert));
                                        popUpAlert.setMessage(getResources().getString(R.string.insert_date));

                                        popUpAlert.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        popUpAlert.show();
                                    }else if(tot.equals("")){
                                        AlertDialog.Builder popUpAlert = new AlertDialog.Builder(getContext());
                                        popUpAlert.setTitle(getResources().getString(R.string.avviso));
                                        popUpAlert.setIcon(getResources().getDrawable(R.drawable.alert));
                                        popUpAlert.setMessage(getResources().getString(R.string.insert_import));

                                        popUpAlert.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        popUpAlert.show();
                                    }else{
                                        writeNewBill(date, pay, type, desc, tot,codCasa);
                                        //invio notifiche agli inquilini per avvisarli dell'inserimneto della nuova bolletta
                                        DatabaseReference refInquilini=FirebaseDatabase.getInstance().getReference("houses/"+codCasa+"/inquilini");
                                        refInquilini.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot ds1:dataSnapshot.getChildren()){
                                                    final DatabaseReference database=FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
                                                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            User user=dataSnapshot.getValue(User.class);

                                                            // invio delle notifiche
                                                            sendNotification(ds1.getKey(),tot+ getString(R.string.notification_new_bill_body)+""+date ,type,codCasa);

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }



                                    Toast.makeText(getActivity(), codCasa,
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


    private void sendNotification(String rec, final String mess, String title, String codCasa){
        DatabaseReference allToken=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=allToken.orderByKey().equalTo(rec);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    Token token=ds.getValue(Token.class);

                    //ricerca dei codici identificativi deii destinatari delle notifiche.
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("houses/"+codCasa+"/inquilini");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()) {

                                //selezione di tutti gli inquilini tranne che il mittente(se il mittente è un Inquilino).
                                if(!ds.getKey().equals(currentUser.getUid())){
                                    String codInquilino=ds.getKey();
                                    // craezione oggetto data contenente tutti le caratteristiche fondamentali per l'invio del messaggio.
                                    Data data= new Data(currentUser.getUid(),title+" : "+mess, getResources().getString(R.string.notification_new_bill_title),codInquilino,R.drawable.easyhome);

                                    //invio della notifica contente  il messaggio.
                                    Sender sender=new Sender(data,token.getToken());
                                    apiService.sendNotification(sender)
                                            .enqueue(new Callback<Response>() {
                                                @Override
                                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                                    Toast.makeText(getContext(), getContext().getString(R.string.message_sent)+response.message(), Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<Response> call, Throwable t) {
                                                    Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
