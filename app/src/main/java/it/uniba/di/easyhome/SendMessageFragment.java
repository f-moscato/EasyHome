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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SendMessageFragment extends Fragment {
    private SendMessageViewModel mViewModel;


    public static SendMessageFragment newInstance() {
        return new SendMessageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.send_message_fragment, container, false);
        final Button btnSender= root.findViewById(R.id.buttonSendMessage);
        final EditText editTextMessage= root.findViewById(R.id.messageToSend);
        final EditText editTextTitleMessage= root.findViewById(R.id.messageTitle);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.v(TAG,"passo dal fragment" );
        btnSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "X", Toast.LENGTH_SHORT).show();
                Log.v(TAG,"prima di send" );
                String message= editTextMessage.getText().toString();

                    final Bundle bundle=getArguments();

                    sendMessage(editTextTitleMessage.getText().toString(),message,currentUser.getUid(),bundle.getString("nomeCasa"));
                Log.v(TAG,"dopo button" );

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

        Log.v(TAG,"passo dal button" );
        reference.child("messages").setValue(hashMap);
    }



}
