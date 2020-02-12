package it.uniba.di.easyhome.proprietario.homecard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.firebase.database.ValueEventListener;

import it.uniba.di.easyhome.Fragments.AddBolletteFragment;
import it.uniba.di.easyhome.Fragments.SendMessageFragment;
import it.uniba.di.easyhome.Fragments.ViewBolletteFragment;
import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.User;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeCardFragment extends Fragment{
    Animation FabOpen,FabClose;
    boolean isOpen=false;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.home_card_fragment, container, false);
        final FloatingActionButton add_bill_fab= (getActivity().findViewById(R.id.fab2_plus));
        final TextView textIndietro= (TextView) getActivity().findViewById(R.id.agg_boll);

        //Setting Back's Button
        add_bill_fab.setImageDrawable(getResources().getDrawable(R.drawable.bill));
        textIndietro.setText(getResources().getString(R.string.add_bill));

        final Bundle bundle=getArguments();
        final TextView twNomeCasa=root.findViewById(R.id.nomeCasaProprietario);
        twNomeCasa.setText(bundle.getString("nomeCasa"));
          final  String pr;
       pr=bundle.getString("Casa");
        DatabaseReference queryRicercaCasa= FirebaseDatabase.getInstance().getReference("houses");
        queryRicercaCasa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final LinearLayout ly_AtHome=root.findViewById(R.id.ly_AtHomeOwner);
                ly_AtHome.removeAllViews();
                TextView twAtHome=new TextView(getActivity());
                twAtHome.setText(getResources().getString(R.string.atHome));
                LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tW.setMargins(55,0,35,0);
                twAtHome.setLayoutParams(tW);
                ly_AtHome.addView(twAtHome);
                for(DataSnapshot dsH:dataSnapshot.getChildren()){
                    Log.v(TAG,dsH.getValue(House.class).getName()+"/"+twNomeCasa.getText().toString());
                    if(dsH.getValue(House.class).getName().equals(twNomeCasa.getText().toString())){
                        final DatabaseReference query = FirebaseDatabase.getInstance().getReference("houses/"+dsH.getKey()+"/inquilini");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChildren()){
                                    for(final DataSnapshot ds:dataSnapshot.getChildren()){
                                            DatabaseReference queryPerNomeUtente=FirebaseDatabase.getInstance().getReference("users");
                                            queryPerNomeUtente.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                    for(DataSnapshot ds1:dataSnapshot.getChildren()){

                                                        if(ds1.getKey().equals(ds.getKey())){
                                                            LinearLayout lyInquilino = new LinearLayout(getActivity());
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
                                                            if(ds.getValue().toString().equals("true")){
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
                                }else{
                                    Log.v(TAG,"ciao");
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

        LinearLayout linearLayoutNotices=root.findViewById(R.id.ly_chat_Casa);
        linearLayoutNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("nomeCasa",twNomeCasa.getText().toString());
                SendMessageFragment sendMessageFragment=new SendMessageFragment();
                sendMessageFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.nav_host_fragment,sendMessageFragment,"Notices");
                fragmentTransaction.commit();
            }
        });

        LinearLayout linearLayoutBills=root.findViewById(R.id.ly_bill_casa_proprietario);
        linearLayoutBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("nomeCasa",twNomeCasa.getText().toString());
                ViewBolletteFragment viewBolletteFragment =new ViewBolletteFragment() ;
                viewBolletteFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.nav_host_fragment, viewBolletteFragment,"Bills");
                fragmentTransaction.commit();
            }
        });

        add_bill_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("nomeCasa",twNomeCasa.getText().toString());
                bundle.putString("Casa",pr);
                Fragment newFragment = new AddBolletteFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment,"AddBills");
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return root;


    }
    public void showCode(){
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot ds: dataSnapshot.getChildren()) {
                        House h = new House(ds.getValue(House.class).getName(), ds.getValue(House.class).getOwner(), ds.getValue(House.class).getInquilini(), ds.getValue(House.class).getBills());
                        if(h.getOwner().equals(currentUser.getUid())){
                            AlertDialog.Builder mBuilder= new AlertDialog.Builder(getContext());
                            mBuilder.setTitle(getResources().getText(R.string.id));
                            mBuilder.setIcon(R.drawable.ic_person_add);
                            final String code=h.getOwner();
                            mBuilder.setMessage(code);
                            mBuilder.setPositiveButton(getResources().getText(R.string.share), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent myIntent=new Intent(Intent.ACTION_SEND);
                                    myIntent.setType("text/plain");
                                    myIntent.putExtra(Intent.EXTRA_SUBJECT,code);
                                    myIntent.putExtra(Intent.EXTRA_TEXT,code);
                                    startActivity(Intent.createChooser(myIntent,"Share using"));
                                }
                            });
                            mBuilder.show();
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
