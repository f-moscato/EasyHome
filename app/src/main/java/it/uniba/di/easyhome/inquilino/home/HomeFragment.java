package it.uniba.di.easyhome.inquilino.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.Fragments.SendMessageFragment;
import it.uniba.di.easyhome.User;
import it.uniba.di.easyhome.inquilino.Pulizie.PulizieFragment;
import it.uniba.di.easyhome.Fragments.ViewBolletteFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {
    DatabaseReference mDatabase;
    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView tw_NomeCasa= root.findViewById(R.id.text_home);
          final EditText input =new EditText(getContext());
        ImageView imgChat=root.findViewById(R.id.inqImgAnnunci);
        ImageView imgClean=root.findViewById(R.id.inqImgPulizie);




        imgChat.setColorFilter(getResources().getColor(R.color.colorPrimary));
        imgClean.setColorFilter(getResources().getColor(R.color.colorPrimary));

        final LinearLayout ly_AtHome=root.findViewById(R.id.ly_AtHome);
        ly_AtHome.removeAllViews();

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");

        ValueEventListener vel= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                TextView twAtHome=new TextView(getActivity());
                twAtHome.setText(getResources().getString(R.string.atHome));
                LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tW.setMargins(55,0,35,0);
                twAtHome.setLayoutParams(tW);
                ly_AtHome.addView(twAtHome);
                if(dataSnapshot.exists()){
                    for (DataSnapshot dsCase:dataSnapshot.getChildren()){
                        House h=new House(
                                dsCase.getValue(House.class).getName()
                                ,dsCase.getValue(House.class).getOwner()
                                ,dsCase.getValue(House.class).getInquilini()
                                ,dsCase.getValue(House.class).getBills()
                                ,dsCase.getValue(House.class).getSsid());
                        for(String cod:h.getInquilini().keySet()){
                            if(cod.equals(currentUser.getUid())){
                                tw_NomeCasa.setText(h.getName());
                                if(h.getName().equals(tw_NomeCasa.getText().toString())){
                                    final DatabaseReference query = FirebaseDatabase.getInstance().getReference("houses/"+dsCase.getKey()+"/inquilini");
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChildren()){
                                                for(final DataSnapshot dsInquilini:dataSnapshot.getChildren()){

                                                    if(!dsInquilini.getKey().equals(currentUser.getUid())){

                                                        DatabaseReference queryPerNomeUtente=FirebaseDatabase.getInstance().getReference("users");
                                                        queryPerNomeUtente.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                for(DataSnapshot ds1:dataSnapshot.getChildren()){
                                                                    if(ds1.getKey().equals(dsInquilini.getKey())){
                                                                        LinearLayout lyInquilino = new LinearLayout(getContext());
                                                                        LinearLayout.LayoutParams margin=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                                        margin.setMargins(20,20,15,0);
                                                                        lyInquilino.setLayoutParams(margin);
                                                                        LinearLayout.LayoutParams marginImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                                        marginImg.setMargins(30,45,15,8);
                                                                        marginImg.height=80;
                                                                        marginImg.width=80;
                                                                        ImageView imgAtHome=new ImageView(getActivity());
                                                                        imgAtHome.setLayoutParams(marginImg);
                                                                        imgAtHome.setImageResource(R.drawable.athome);
                                                                        if(dsInquilini.getValue().toString().equals("true")){
                                                                            imgAtHome.setColorFilter(getResources().getColor(R.color.colorAtHome));
                                                                        }else{

                                                                            imgAtHome.setColorFilter(getResources().getColor(R.color.colorNotAtHome));
                                                                        }
                                                                        lyInquilino.addView(imgAtHome);
                                                                        LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                                        tW.setMargins(35,60,35,0);
                                                                        TextView twNomeInquilino = new TextView(getActivity());
                                                                        twNomeInquilino.setLayoutParams(tW);
                                                                        twNomeInquilino.setText(ds1.getValue(User.class).getName()+" "+ds1.getValue(User.class).getSurname());
                                                                        lyInquilino.addView(twNomeInquilino);
                                                                        ly_AtHome.addView(lyInquilino);
                                                                    }
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

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
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
        LinearLayout ly_ButtonBill= (LinearLayout) root.findViewById(R.id.ly_bill_inquilino);
        ly_ButtonBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("nomeCasa",tw_NomeCasa.getText().toString());
                ViewBolletteFragment viewBolletteFragment =new ViewBolletteFragment() ;
                viewBolletteFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.add(new HomeFragment(),"Casa").addToBackStack(HomeFragment.class.getName());
                fragmentTransaction.replace(R.id.nav_host_fragment, viewBolletteFragment,"BILL");
                fragmentTransaction.commit();
            }
        });
        LinearLayout ly_ButtonCleaning= (LinearLayout) root.findViewById(R.id.ly_cleaning_inquilino);
        ly_ButtonCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("nomeCasa",tw_NomeCasa.getText().toString());
                PulizieFragment pulizieFragment=new PulizieFragment();
                pulizieFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.add(new HomeFragment(),"Casa").addToBackStack(HomeFragment.class.getName());
                fragmentTransaction.replace(R.id.nav_host_fragment,pulizieFragment,"PULIZIE");
                fragmentTransaction.commit();
            }
        });
        LinearLayout ly_ButtonChat= (LinearLayout) root.findViewById(R.id.ly_chat_inquilino);
        ly_ButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("nomeCasa",tw_NomeCasa.getText().toString());
                SendMessageFragment sendMessageFragment=new SendMessageFragment() ;
                sendMessageFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.add(new HomeFragment(),"Casa").addToBackStack(HomeFragment.class.getName());
                fragmentTransaction.replace(R.id.nav_host_fragment,sendMessageFragment,"CHAT");
                fragmentTransaction.commit();

            }
        });

        vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int flag = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        House h=new House(
                                ds.getValue(House.class).getName()
                                ,ds.getValue(House.class).getOwner()
                                ,ds.getValue(House.class).getInquilini()
                                ,ds.getValue(House.class).getBills()
                                ,ds.getValue(House.class).getSsid());
                        Log.v(TAG, h.getName() + " / " + h.getInquilini().size() + "/" + currentUser.getDisplayName());
                        for (String cod : h.getInquilini().keySet()) {
                            if (cod.equals(currentUser.getUid())) {
                                flag = 12;
                                tw_NomeCasa.setText(h.getName());
                                break;
                            }
                        }
                    }
                    if (flag != 12) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Entra in una casa");
                        alert.setIcon(getResources().getDrawable(R.drawable.ic_person_add));
                        alert.setMessage("Incolla il codice della casa a cui vui aggiungerti");
                        final EditText edittext = new EditText(getActivity());
                        alert.setView(edittext);
                            alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //What ever you want to do with the value
                                    String code = edittext.getText().toString();
                                    //Add inq in Home
                                    addToHome(code);
                                }
                            });

                            alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // what ever you want to do with No option.
                                }
                            });

                            alert.show();
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


    private void addToHome(final String code){
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                        House h=new House(
                                ds.getValue(House.class).getName()
                                ,ds.getValue(House.class).getOwner()
                                ,ds.getValue(House.class).getInquilini()
                                ,ds.getValue(House.class).getBills()
                                ,ds.getValue(House.class).getSsid());
                        final HashMap<String,String> inq = new HashMap<>();
                        inq.put(currentUser.getUid(),"true");
                        if (h.getOwner().equals(code)) {
                            House user = new House(inq);
                            mDatabase.child("houses").child(ds.getKey()).child("inquilini").push().setValue(user);
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}