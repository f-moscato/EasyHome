package it.uniba.di.easyhome;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

import it.uniba.di.easyhome.Notifiche.APIService;
import it.uniba.di.easyhome.Notifiche.Client;
import it.uniba.di.easyhome.Notifiche.Data;
import it.uniba.di.easyhome.Notifiche.MyRespnse;
import it.uniba.di.easyhome.Notifiche.Sender;
import it.uniba.di.easyhome.Notifiche.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SendMessageFragment extends Fragment {

    private SendMessageViewModel mViewModel;
    APIService apiService;
    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    boolean notify = false;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.send_message_fragment, container, false);

        final Button btnSender= root.findViewById(R.id.buttonSendMessage);
        final EditText editTextMessage= root.findViewById(R.id.messageToSend);
        final EditText editTextTitleMessage= root.findViewById(R.id.messageTitle);

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        final String message= editTextMessage.getText().toString();
        Log.v(TAG,"passo dal fragment" );
        btnSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "X", Toast.LENGTH_SHORT).show();
                Log.v(TAG,"prima di send" );
                final Bundle bundle=getArguments();
                sendMessage(editTextTitleMessage.getText().toString(),message,currentUser.getUid(),bundle.getString("nomeCasa"));
                Log.v(TAG,"dopo button" );

            }
        });

        final String msg= message;
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("houses/").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //sendNotification();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SendMessageViewModel.class);
        // TODO: Use the ViewModel
    }

    private void sendMessage(String title, String message, String sender,String nomeCasa){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("houses/"+nomeCasa);
        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("title",title);
        hashMap.put("message",message);
        reference.child("messages").setValue(hashMap);


        updateToken(FirebaseInstanceId.getInstance().getToken());
    }


    private void updateToken(String token){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(currentUser.getUid()).setValue(token1);
    }

    private void sendNotification(ArrayList<String> receiver, final String username, final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("token");
        for(final String rec:receiver){
            Query query=tokens.orderByKey().equalTo(rec);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Token token=snapshot.getValue(Token.class);
                        Data data= new Data(username, "new Message",message, rec);
                        Sender sender=new Sender(data,token.getToken());
                        apiService.sendNotification(sender)
                                .enqueue(new Callback<MyRespnse>() {
                                    @Override
                                    public void onResponse(Call<MyRespnse> call, Response<MyRespnse> response) {
                                        if(response.code()==200){
                                            if(response.body().success!=1){
                                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyRespnse> call, Throwable t) {

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
