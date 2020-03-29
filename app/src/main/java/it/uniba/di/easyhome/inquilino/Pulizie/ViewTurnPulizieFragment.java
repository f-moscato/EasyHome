package it.uniba.di.easyhome.inquilino.Pulizie;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.di.easyhome.Pulizia;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.SharedPref;
import it.uniba.di.easyhome.inquilino.home.HomeFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ViewTurnPulizieFragment extends Fragment {
    DatabaseReference mDatabase;
    SharedPref sharedpref;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.inquilino,menu);
        menu.findItem(R.id.action_add_turn).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        final Bundle c=getArguments();
        if(id==R.id.action_add_turn){
            Bundle bundle=new Bundle();
            bundle.putString("nomeCasa",(c.getString("nomeCasa")));
            AddTurnPulizieFragment pulizieFragment=new AddTurnPulizieFragment();
            pulizieFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
            fragmentTransaction.add(new HomeFragment(),"Casa").addToBackStack(HomeFragment.class.getName());
            fragmentTransaction.replace(R.id.nav_host_fragment,pulizieFragment,"PULIZIE");
            fragmentTransaction.commit();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.cleaning));
            Log.v(TAG,"Cambio");

        }
        return super.onOptionsItemSelected(item);
    }


    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.view_turn_pulizie_fragment, container, false);
            final LinearLayout lyPrincipale= root.findViewById(R.id.clean_layout);
        sharedpref=new SharedPref(getContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                        DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference("houses/" + ds.getKey() + "/pulizie");
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        rootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (final DataSnapshot ds1 : dataSnapshot.getChildren()) {
                                        Pulizia p = (ds1.getValue(Pulizia.class));

                                        //Creazione Layout dinamico con inserimento dei vari elementi quali textbox e immagini
                                        LinearLayout lySingolaBolletta = new LinearLayout(getActivity());
                                        lySingolaBolletta.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                                        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        margin.setMargins(20, 45, 15, 0);
                                        lySingolaBolletta.setLayoutParams(margin);




                                        //settaggio textbox che contengono le caratteristiche delle turni pulizie
                                        LinearLayout lyTurni=new LinearLayout(getActivity());

                                        LinearLayout.LayoutParams marginTurni = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        marginTurni.setMargins(20, 0, 15, 55);
                                        lyTurni.setLayoutParams(marginTurni);
                                        lyTurni.setOrientation(LinearLayout.VERTICAL);
                                        LinearLayout.LayoutParams tW = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        tW.setMargins(35, 60, 65, 0);
                                        TextView tw_turn_1 = new TextView(getActivity());
                                        tw_turn_1.setLayoutParams(tW);
                                        tw_turn_1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        tw_turn_1.setText(new StringBuilder().append(getString(R.string.turn_1)).append(System.getProperty("line.separator")).append(p.getTurn_1()));
                                        TextView tw_turn2 = new TextView(getActivity());
                                        tw_turn2.setLayoutParams(tW);
                                        tw_turn2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        tw_turn2.setText(new StringBuilder().append(getString(R.string.turn_2)).append(System.getProperty("line.separator")).append(p.getTurn_2()));
                                        lyTurni.addView(tw_turn_1);
                                        lyTurni.addView(tw_turn2);



                                        TextView tw_data = new TextView(getActivity());
                                        tw_data.setText(new StringBuilder().append(getString(R.string.day)).append(System.getProperty("line.separator")).append(p.getDay()));
                                        tw_data.setLayoutParams(tW);
                                        tw_data.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        TextView tw_descr = new TextView(getActivity());
                                        tw_descr.setText(new StringBuilder().append(getString(R.string.description_bollette)).append(System.getProperty("line.separator")).append(p.getDescrzione()));
                                        tw_descr.setLayoutParams(tW);
                                        tw_descr.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        if(sharedpref.loadNightModeState()){
                                            tw_data.setTextColor(Color.WHITE);
                                            tw_turn_1.setTextColor(Color.WHITE);
                                            tw_turn2.setTextColor(Color.WHITE);
                                            tw_descr.setTextColor(Color.WHITE);
                                        }
                                        //inserimento delle text box nel linear layout principale
                                        lySingolaBolletta.addView(tw_data);
                                        lySingolaBolletta.addView(tw_descr);
                                        lySingolaBolletta.addView(lyTurni);
                                        lyPrincipale.addView(lySingolaBolletta);
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

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

                    }
                });
        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals(getString(R.string.app_name))){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }
}
