package it.uniba.di.easyhome.proprietario.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.proprietario.bollette.BolletteFragment;
import it.uniba.di.easyhome.proprietario.homecard.HomeCardFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        FloatingActionButton fab= (getActivity().findViewById(R.id.fab_plus));
        fab.show();
        fab.setClickable(true);

        final View root = inflater.inflate(R.layout.pr_fragment_home, container, false);


        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");



        ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final LinearLayout ly=root.findViewById(R.id.ly_home_inquilino);
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        House h=new House(ds.getValue(House.class).getName(),ds.getValue(House.class).getOwner(),ds.getValue(House.class).getInquilini(),ds.getValue(House.class).getBills());
                        Log.d(TAG, h.getName() + " / " +h.getOwner());

                        LinearLayout lyl= new LinearLayout(getActivity());
                        lyl.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                        LinearLayout.LayoutParams margin=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        margin.setMargins(15,20,15,0);
                        lyl.setLayoutParams(margin);

                        lyl.setBackground(getResources().getDrawable(R.drawable.effect_button));

                        LinearLayout.LayoutParams marginImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        marginImg.setMargins(30,8,15,8);
                        ImageView img = new ImageView(getActivity());
                        img.setLayoutParams(marginImg);
                        img.setColorFilter(getResources().getColor(R.color.colorPrimary));
                        img.setImageResource(R.drawable.casa);

                        LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        tW.setMargins(90,70,0,0);
                        final TextView tw= new TextView(getActivity());
                        tw.setText(h.getName());
                        tw.setLayoutParams(tW);
                        tw.setTextColor(getResources().getColor(R.color.colorPrimary));


                        lyl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle=new Bundle();
                                bundle.putString("nomeCasa",tw.getText().toString());
                                HomeCardFragment homeCardFragment=new HomeCardFragment();
                                homeCardFragment.setArguments(bundle);
                                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment,homeCardFragment,"PROVA");
                                fragmentTransaction.commit();
                            }
                        });


                       /* Button btnShow = new Button(getActivity());
                        btnShow.setText(h.getName());
                        btnShow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        btnShow.setTextColor(getResources().getColor(R.color.fab1_color));
                        LinearLayout.LayoutParams lly_button=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lly_button.setMargins(0,0,0,50);
                        btnShow.setHeight(200);
                        btnShow.setTextSize(25);
                        btnShow.setLayoutParams(lly_button);
                        btnShow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(),"ok", Toast.LENGTH_LONG).show();
                            }
                        });*/

                        // Add Button to LinearLayout
                        if (ly != null) {
                            //lyl.addView(btnShow);
                            lyl.addView(img);
                            lyl.addView(tw);
                            ly.addView(lyl);
                        }
                        /*Query queryBill=query.orderByChild("bills");
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
                        });*/
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