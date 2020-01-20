package it.uniba.di.easyhome.ui.home;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.InquilinoActivity;
import it.uniba.di.easyhome.InquilinoActivity;
import it.uniba.di.easyhome.NtReceiver;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.SplashScreenActivity;
import it.uniba.di.easyhome.proprietario.bollette.BolletteFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static it.uniba.di.easyhome.Notifica.CHANNEL_1;

public class HomeFragment extends Fragment {
    private NotificationManagerCompat notificationManager;
    private EditText title;
    private EditText body;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView tw_NomeCasa= root.findViewById(R.id.text_home);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
notificationManager=NotificationManagerCompat.from(getActivity());
title=root.findViewById(R.id.editText);
body=root.findViewById(R.id.editText2);


        LinearLayout ly_ButtonBill= (LinearLayout) root.findViewById(R.id.ly_bill_inquilino);
        ly_ButtonBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("nomeCasa",tw_NomeCasa.getText().toString());
                BolletteFragment bolletteFragment=new BolletteFragment() ;
                bolletteFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,bolletteFragment,"PROVA");
                fragmentTransaction.commit();
            }
        });


        LinearLayout ly_ButtonCleaning= (LinearLayout) root.findViewById(R.id.ly_cleaning_inquilino);
        ly_ButtonCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SplashScreenActivity.class);
                startActivity(i);
            }
        });


        LinearLayout ly_ButtonChat= (LinearLayout) root.findViewById(R.id.ly_chat_inquilino);
        ly_ButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SplashScreenActivity.class);
                startActivity(i);
            }
        });

        Button go=(Button)root.findViewById(R.id.button_1);
       go.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String tit= title.getText().toString();
               Intent activityIntent = new Intent(getActivity(), InquilinoActivity.class);
               PendingIntent content=PendingIntent.getActivity(getActivity(),0,activityIntent,0);
               String bd=body.getText().toString();
               Intent broadcastIntent=new Intent(getActivity(), NtReceiver.class);
               broadcastIntent.putExtra("prova1",bd);
               PendingIntent action = PendingIntent.getBroadcast(getActivity(),0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
               Notification notification = new NotificationCompat.Builder(getActivity(),CHANNEL_1)
                       .setSmallIcon(R.drawable.easyhome)
                       .setContentTitle(tit)
                       .setContentText(bd)
                       .setPriority(NotificationCompat.PRIORITY_HIGH)
                       .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                       .setContentIntent(content)
                       .setAutoCancel(true)
                       .setOnlyAlertOnce(true)
                       .setColor(Color.BLUE)
                       .addAction(R.mipmap.ic_launcher,"Toast",action)
                       .build();
               notificationManager.notify(1,notification);
           }
       });

        ValueEventListener vel= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        House h=new House(ds.getValue(House.class).getName(),ds.getValue(House.class).getOwner(),ds.getValue(House.class).getInquilini(),ds.getValue(House.class).getBills());
                        Log.v(TAG, h.getName() + " / " +h.getInquilini().size()+"/"+currentUser.getDisplayName());
                        for(String cod:h.getInquilini().keySet()){
                            if(cod.equals(currentUser.getUid())){

                                tw_NomeCasa.setText(h.getName());
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        rootRef.addListenerForSingleValueEvent(vel);


        return root;
    }
}