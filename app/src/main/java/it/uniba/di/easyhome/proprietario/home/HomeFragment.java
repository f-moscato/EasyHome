package it.uniba.di.easyhome.proprietario.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import it.uniba.di.easyhome.SharedPref;
import it.uniba.di.easyhome.proprietario.homecard.HomeCardFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {

    SharedPref sharedpref;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home_proprietario, container, false);
        sharedpref=new SharedPref(getContext());

        final FloatingActionButton fab= (getActivity().findViewById(R.id.fab_plus));


        //Start FAB AddInquilino
        final FloatingActionButton add_home_fab= (getActivity().findViewById(R.id.fab2_plus));
        final TextView textIndietro= (TextView) getActivity().findViewById(R.id.agg_boll);

        textIndietro.setText(getResources().getString(R.string.home_add));

        if(sharedpref.loadNightModeState()){
            textIndietro.setTextColor(Color.WHITE);
        }

//Start FAB AddHome
        add_home_fab.setImageDrawable(getResources().getDrawable(R.drawable.home_plus));
        fab.show();
        fab.setClickable(true);
        add_home_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new AddCasaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        final Bundle bundle=getArguments();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
        ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final LinearLayout ly=root.findViewById(R.id.ly_home_inquilino);
                if(dataSnapshot.exists()){
                    for(final DataSnapshot ds: dataSnapshot.getChildren()) {
                        House h=new House(
                                ds.getValue(House.class).getName()
                                ,ds.getValue(House.class).getOwner()
                                ,ds.getValue(House.class).getInquilini()
                                ,ds.getValue(House.class).getBills()
                                ,ds.getValue(House.class).getSsid());
                        if(h.getOwner().equals(currentUser.getUid())){
                        Log.d(TAG, h.getName() + " / " + h.getOwner());
                        LinearLayout lyl = new LinearLayout(getActivity());
                        lyl.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        margin.setMargins(15, 20, 15, 0);
                        lyl.setLayoutParams(margin);
                        lyl.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.effect_button));
                        LinearLayout.LayoutParams marginImg = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        marginImg.setMargins(30, 8, 15, 8);
                        ImageView img = new ImageView(getActivity());
                        img.setLayoutParams(marginImg);
                        img.setColorFilter(getResources().getColor(R.color.colorPrimary));
                        img.setImageResource(R.drawable.casa);
                        LinearLayout.LayoutParams tW = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        tW.setMargins(90, 70, 0, 0);
                        final TextView tw = new TextView(getActivity());
                        tw.setText(h.getName());
                        tw.setLayoutParams(tW);
                        tw.setTextColor(getResources().getColor(R.color.colorPrimary));
                            if(sharedpref.loadNightModeState()){
                                img.setColorFilter(getResources().getColor(R.color.colorAccent));
                                tw.setTextColor(Color.WHITE);
                            }
                        lyl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("nomeCasa", tw.getText().toString());
                                bundle.putString("Casa", ds.getKey());
                                HomeCardFragment homeCardFragment = new HomeCardFragment();
                                homeCardFragment.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.add(new HomeFragment(), "ListaCase").addToBackStack(HomeFragment.class.getName());
                                fragmentTransaction.replace(R.id.nav_host_fragment, homeCardFragment, "PROVA");
                                fragmentTransaction.commit();
                            }
                        });


                        // Add Button to LinearLayout
                        if (ly != null) {
                            //lyl.addView(btnShow);
                            lyl.addView(img);
                            lyl.addView(tw);
                            ly.addView(lyl);
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