package it.uniba.di.easyhome.inquilino.Pulizie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.di.easyhome.Pulizia;
import it.uniba.di.easyhome.R;

public class ViewTurnPulizieFragment extends Fragment {
    DatabaseReference mDatabase;
    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.view_turn_pulizie_fragment, container, false);
            final Bundle bundle=getArguments();
            final LinearLayout lyPrincipale= root.findViewById(R.id.clean_layout);
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


                                        //settaggio textbox che contengono le caratteristiche delle bollette
                                        LinearLayout.LayoutParams tW = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        tW.setMargins(35, 60, 65, 0);
                                        TextView tw_importo = new TextView(getActivity());
                                        tw_importo.setLayoutParams(tW);
                                        tw_importo.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        tw_importo.setText(new StringBuilder().append(getString(R.string.turn_1)).append(System.getProperty("line.separator")).append(p.getTurn_1()));
                                        TextView tw_importo_2 = new TextView(getActivity());
                                        tw_importo_2.setLayoutParams(tW);
                                        tw_importo_2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        tw_importo_2.setText(new StringBuilder().append(getString(R.string.turn_2)).append(System.getProperty("line.separator")).append(p.getTurn_2()));
                                        TextView tw_datascadenza = new TextView(getActivity());
                                        tw_datascadenza.setText(new StringBuilder().append(getString(R.string.day)).append(System.getProperty("line.separator")).append(p.getDay()));
                                        tw_datascadenza.setLayoutParams(tW);
                                        tw_datascadenza.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        TextView tw_descr = new TextView(getActivity());
                                        tw_descr.setText(new StringBuilder().append(getString(R.string.description_bollette)).append(System.getProperty("line.separator")).append(p.getDescrzione()));
                                        tw_descr.setLayoutParams(tW);
                                        tw_descr.setTextColor(getResources().getColor(R.color.colorPrimary));

                                        //inserimento delle immagini e delle text box nel linear layout principale
                                        lySingolaBolletta.addView(tw_datascadenza);
                                        lySingolaBolletta.addView(tw_descr);
                                        lySingolaBolletta.addView(tw_importo);
                                        lySingolaBolletta.addView(tw_importo_2);
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

        }
