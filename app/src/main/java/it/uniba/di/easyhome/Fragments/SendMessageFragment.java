package it.uniba.di.easyhome.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Objects;

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.Notifiche.APIService;
import it.uniba.di.easyhome.Notifiche.Client;
import it.uniba.di.easyhome.Notifiche.Data;
import it.uniba.di.easyhome.Notifiche.Response;
import it.uniba.di.easyhome.Notifiche.Sender;
import it.uniba.di.easyhome.Notifiche.Token;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.User;
import retrofit2.Call;
import retrofit2.Callback;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SendMessageFragment extends Fragment {

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private APIService apiService;
    private boolean notify=false;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.send_message_fragment, container, false);
        final Button btnSender= root.findViewById(R.id.buttonSendMessage);
        final EditText editTextMessage= root.findViewById(R.id.messageToSend);
        final EditText editTextTitleMessage= root.findViewById(R.id.messageTitle);
        //creazione servizio API
        apiService= Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        btnSender.setOnClickListener(new View.OnClickListener() {
            private void onComplete(Task<Void> task) {
                String msg = "subs";
                if (!task.isSuccessful()) {
                    msg = "fail";
                }
            }

            @Override
            public void onClick(View v) {
                notify=true;
                if (currentUser != null) {
                    sendMessage(editTextTitleMessage.getText().toString(), editTextMessage.getText().toString(),currentUser.getUid());
                }
                editTextMessage.setText("");
                editTextTitleMessage.setText("");

                FirebaseMessaging.getInstance().subscribeToTopic("weather")
                        .addOnCompleteListener(this::onComplete);

            }
        });

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){
                    Log.v(TAG,"getIstanceID failed", task.getException());
                }

            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * metodo che inserisce il messaggio all'interno del database Firebase e manda la notifiche agli inquilini presenti nella casa.
     * @param title titolo del messaggio
     * @param message corpo del messaggio
     * @param sender mittente
     */
    private void sendMessage(String title, final String message, String sender ){

        final DatabaseReference database=FirebaseDatabase.getInstance().getReference("users").child(sender);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);
                Log.v(TAG,"notify:"+notify);
                if(notify){

                    //ricerca del codice identificativo della casa, dato il nome prelevato dal bundle.
                    DatabaseReference refHouse=FirebaseDatabase.getInstance().getReference("houses");
                    refHouse.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                final Bundle bundle=getArguments();
                                Log.v(TAG, String.valueOf(dataSnapshot.getChildrenCount()));
                                if(ds.getValue(House.class).getName().equals(bundle.getString("nomeCasa"))){
                                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("houses/"+ds.getKey());

                                    /* inserimento del messaggio nel db*/
                                    HashMap<String,Object> hashMap= new HashMap<>();
                                    hashMap.put("sender",sender);
                                    hashMap.put("title",title);
                                    hashMap.put("message",message);
                                    reference.child("messages").setValue(hashMap);

                                    // ricerca degli inquilini presenti nella stessa casa
                                    DatabaseReference refInquilini=FirebaseDatabase.getInstance().getReference("houses/"+ds.getKey()+"/inquilini");
                                    refInquilini.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds1:dataSnapshot.getChildren()){
                                                final DatabaseReference database=FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
                                                database.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        User user=dataSnapshot.getValue(User.class);
                                                        if(notify){
                                                            // invio delle notifiche
                                                            sendNotification(ds1.getKey(),user.getName(), message,title,ds.getKey());
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

    /**
     * metodo che effettua l'invio delle notifiche
     * @param rec destinatario della notifica
     * @param userName nome del mittente
     * @param mess corpo del messaggio
     * @param title titolo del messaggio
     * @param codCasa codice identificativo della casa dove sono presenti i destinatari delle notifiche
     */
    private void sendNotification(String rec, final String userName, final String mess, String title, String codCasa){
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
                                    Data data= new Data(currentUser.getUid(),title+" : "+mess, Objects.requireNonNull(getContext()).getString(R.string.new_message_from)+userName,codInquilino,R.drawable.easyhome);

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


    @Override
    public void onStop() {
        super.onStop();
        if(!((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals(getString(R.string.app_name))){
            Log.v(TAG,"passo toolbar");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }


}
