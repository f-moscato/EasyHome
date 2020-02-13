package it.uniba.di.easyhome.Fragments.Bollette;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.proprietario.ProprietarioActivity;
import it.uniba.di.easyhome.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ViewBolletteFragment extends Fragment {

    private View root;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
if(getActivity().equals(ProprietarioActivity.class)) {
    FloatingActionButton fab = (getActivity().findViewById(R.id.fab_plus));
    fab.hide();
    fab.setClickable(false);}

    root = inflater.inflate(R.layout.pr_fragment_bollette, container, false);

        final Button buttonNotPayed= root.findViewById(R.id.buttonNotPayed);
        final Button buttonHistory= root.findViewById(R.id.buttonHystoriBills);

        final Bundle bundle=getArguments();

        final LinearLayout lyPrincipale= root.findViewById(R.id.pr_boll_layout);
        lyPrincipale.removeAllViews();
        buttonNotPayed.setTextColor(getResources().getColor(R.color.colorPrimary));
        buttonNotPayed.setBackgroundColor(Color.TRANSPARENT);
        //Creazione del Value Listener; oggetto che permette l'acquisizione dei dati dal database su Firebase
        //il codice seguente mostra la sezione bollette non pagate al primo avvio del fragment
        final ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        House h = new House(ds.getValue(House.class).getName(), ds.getValue(House.class).getOwner(), ds.getValue(House.class).getInquilini(), ds.getValue(House.class).getBills());
                        if(h.getName().equalsIgnoreCase(bundle.getString("nomeCasa"))){
                            for (HashMap<String, String> dettagli : h.getBills().values()) {
                                String[] info = dettagli.values().toArray(new String[0]);
                                Log.d(TAG, h.getName() + " / " + dettagli.values());
                                if(info[4].equalsIgnoreCase("false")){

                                    //Creazione Layout dinamico con inserimento dei vari elementi quali textbox e immagini
                                    LinearLayout lySingolaBolletta = new LinearLayout(getActivity());
                                    lySingolaBolletta.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                                    LinearLayout.LayoutParams margin=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    margin.setMargins(20,20,15,0);
                                    lySingolaBolletta.setLayoutParams(margin);

                                    //Settaggio margini immagine bolletta e caratteristiche
                                    LinearLayout.LayoutParams marginImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    marginImg.setMargins(30,78,15,8);
                                    ImageView img = new ImageView(getActivity());
                                    marginImg.height=80;
                                    marginImg.width=80;
                                    img.setLayoutParams(marginImg);
                                    img.setColorFilter(getResources().getColor(R.color.colorPrimary));

                                    switch (info[3].toLowerCase()) {
                                        case "gas":
                                            img.setImageResource(R.drawable.gas);
                                            break;
                                        case "energy":
                                            img.setImageResource(R.drawable.energy);
                                            break;
                                        case "water":
                                            img.setImageResource(R.drawable.acqua);
                                            break;
                                        case "other":
                                            img.setImageResource(R.drawable.other);
                                            break;
                                        default:
                                            img.setImageResource(R.drawable.info);
                                            break;
                                    }

                                    //Settaggio immagine alert per le bollette non pagate
                                    ImageView imgAlert=new ImageView(getActivity());
                                    imgAlert.setImageResource(R.drawable.alert);
                                    imgAlert.setColorFilter(getResources().getColor(R.color.colorAccent));
                                    imgAlert.setLayoutParams(marginImg);

                                    //settaggio textbox che contengono le caratteristiche delle bollette
                                    LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    tW.setMargins(35,60,35,0);
                                    TextView tw_importo = new TextView(getActivity());
                                    tw_importo.setLayoutParams(tW);
                                    tw_importo.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    tw_importo.setText(new StringBuilder().append(getString(R.string.import_bollette)).append(System.getProperty("line.separator")).append(info[0]).append(System.getProperty("line.separator")).toString());
                                    TextView tw_datascadenza = new TextView(getActivity());
                                    tw_datascadenza.setText(new StringBuilder().append(getString(R.string.expiration_bollette)).append(System.getProperty("line.separator")).append(info[2]).toString());
                                    tw_datascadenza.setLayoutParams(tW);
                                    tw_datascadenza.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    TextView tw_descr = new TextView(getActivity());
                                    tw_descr.setText(new StringBuilder().append(getString(R.string.description_bollette)).append(System.getProperty("line.separator")).append(info[1]).toString());
                                    tw_descr.setLayoutParams(tW);
                                    tw_descr.setTextColor(getResources().getColor(R.color.colorPrimary));

                                    //inserimento delle immagini e delle text box nel linear layout principale
                                    lySingolaBolletta.addView(img);
                                    lySingolaBolletta.addView(tw_datascadenza);
                                    lySingolaBolletta.addView(tw_descr);
                                    lySingolaBolletta.addView(tw_importo);
                                    lySingolaBolletta.addView(imgAlert);
                                    lyPrincipale.addView(lySingolaBolletta);
                                }



                            }
                            break;
                        }
                }
            }

        }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //Visualizzazione dello storico delle bollette
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cambio di visualizzazione dei button raffiguranti lo storico delle bollette e le bollette non pagate
                buttonHistory.setTextColor(getResources().getColor(R.color.colorPrimary));
                buttonHistory.setBackgroundColor(Color.TRANSPARENT);
                buttonNotPayed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                buttonNotPayed.setTextColor(getResources().getColor(R.color.white));

                //"pulizia" del layout
                lyPrincipale.removeAllViews();

                FirebaseDatabase.getInstance().getReference("houses").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final LinearLayout lyPrincipale=root.findViewById(R.id.pr_boll_layout);

                        if(dataSnapshot.exists()){
                            for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                House h = new House(ds.getValue(House.class).getName(), ds.getValue(House.class).getOwner(), ds.getValue(House.class).getInquilini(), ds.getValue(House.class).getBills());
                                if(h.getName().equalsIgnoreCase(bundle.getString("nomeCasa"))){
                                    for (HashMap<String, String> dettagli : h.getBills().values()) {
                                        String[] info = dettagli.values().toArray(new String[0]);

                                        Log.d(TAG, h.getName() + " / " + dettagli.values());
                                        //creazione linearlayout principale della bolletta con settaggio dei margini
                                        LinearLayout lySingolaBolletta = new LinearLayout(getActivity());
                                        lySingolaBolletta.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                                        LinearLayout.LayoutParams margin=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        margin.setMargins(20,20,15,0);
                                        lySingolaBolletta.setLayoutParams(margin);

                                        //Settaggio margini immagine bolletta e caratteristiche
                                        LinearLayout.LayoutParams marginImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        marginImg.setMargins(30,78,15,8);
                                        marginImg.height=80;
                                        marginImg.width=80;
                                        ImageView img = new ImageView(getActivity());
                                        img.setLayoutParams(marginImg);
                                        img.setColorFilter(getResources().getColor(R.color.colorPrimary));

                                        //settaggio textbox che contengono le caratteristiche delle bollette
                                        LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        tW.setMargins(35,60,35,0);
                                        TextView tw_importo = new TextView(getActivity());
                                        tw_importo.setLayoutParams(tW);
                                        tw_importo.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        tw_importo.setText(new StringBuilder().append(getString(R.string.import_bollette)).append(System.getProperty("line.separator")).append(info[0]).append(System.getProperty("line.separator")).toString());
                                        TextView tw_datascadenza = new TextView(getActivity());
                                        tw_datascadenza.setText(new StringBuilder().append(getString(R.string.expiration_bollette)).append(System.getProperty("line.separator")).append(info[2]).toString());
                                        tw_datascadenza.setLayoutParams(tW);
                                        tw_datascadenza.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        TextView tw_descr = new TextView(getActivity());
                                        tw_descr.setText(new StringBuilder().append(getString(R.string.description_bollette)).append(System.getProperty("line.separator")).append(info[1]).toString());
                                        tw_descr.setLayoutParams(tW);
                                        tw_descr.setTextColor(getResources().getColor(R.color.colorPrimary));

                                        //inserimento immagini in base alla loro natura e conseguente aggiunta di tutti gli elementi nel linear layout principale
                                        if (info[4].equalsIgnoreCase("true")) {

                                            switch (info[3].toLowerCase()) {
                                                case "gas":
                                                    img.setImageResource(R.drawable.gas);
                                                    break;
                                                case "energy":
                                                    img.setImageResource(R.drawable.energy);
                                                    break;
                                                case "water":
                                                    img.setImageResource(R.drawable.acqua);
                                                    break;
                                                case "other":
                                                    img.setImageResource(R.drawable.other);
                                                    break;
                                                default:
                                                    img.setImageResource(R.drawable.info);
                                                    break;
                                            }
                                            lySingolaBolletta.addView(img);
                                            lySingolaBolletta.addView(tw_datascadenza);
                                            lySingolaBolletta.addView(tw_descr);
                                            lySingolaBolletta.addView(tw_importo);
                                            lyPrincipale.addView(lySingolaBolletta);
                                        }else{
                                            ImageView imgAlert=new ImageView(getActivity());
                                            imgAlert.setImageResource(R.drawable.alert);
                                            imgAlert.setColorFilter(getResources().getColor(R.color.colorAccent));
                                            imgAlert.setLayoutParams(marginImg);
                                            switch (info[3].toLowerCase()) {
                                                case "gas":
                                                    img.setImageResource(R.drawable.gas);
                                                    break;
                                                case "energy":
                                                    img.setImageResource(R.drawable.energy);
                                                    break;
                                                case "water":
                                                    img.setImageResource(R.drawable.acqua);
                                                    break;
                                                case "other":
                                                    img.setImageResource(R.drawable.other);
                                                    break;
                                                default:
                                                    img.setImageResource(R.drawable.info);
                                                    break;
                                            }
                                            lySingolaBolletta.addView(img);
                                            lySingolaBolletta.addView(tw_datascadenza);
                                            lySingolaBolletta.addView(tw_descr);
                                            lySingolaBolletta.addView(tw_importo);
                                            lySingolaBolletta.addView(imgAlert);
                                            lyPrincipale.addView(lySingolaBolletta);
                                        }

                                    }
                                }

                            }



                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //Visualizzazione delle bollette non pagate
        buttonNotPayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cambio di visualizzazione dei button raffiguranti lo storico delle bollette e le bollette non pagate
                buttonNotPayed.setTextColor(getResources().getColor(R.color.colorPrimary));
                buttonNotPayed.setBackgroundColor(Color.TRANSPARENT);
                buttonHistory.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                buttonHistory.setTextColor(getResources().getColor(R.color.white));

                //"pulizia" del layout
                lyPrincipale.removeAllViews();

                FirebaseDatabase.getInstance().getReference("houses").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final LinearLayout lyPrincipale= root.findViewById(R.id.pr_boll_layout);

                        if(dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                House h = new House(ds.getValue(House.class).getName(), ds.getValue(House.class).getOwner(), ds.getValue(House.class).getInquilini(), ds.getValue(House.class).getBills());
                                if(h.getName().equalsIgnoreCase(bundle.getString("nomeCasa"))){
                                    for (HashMap<String, String> dettagli : h.getBills().values()) {
                                        String[] info = dettagli.values().toArray(new String[0]);
                                        Log.d(TAG, h.getName() + " / " + dettagli.values());
                                        if (info[4].equalsIgnoreCase("false")) {
                                            //Creazione Layout dinamico con inserimento dei vari elementi quali textbox e immagini
                                            LinearLayout lySingolaBolletta = new LinearLayout(getActivity());
                                            lySingolaBolletta.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                                            LinearLayout.LayoutParams margin=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            margin.setMargins(20,20,15,0);
                                            lySingolaBolletta.setLayoutParams(margin);

                                            //Settaggio margini immagine bolletta e caratteristiche
                                            LinearLayout.LayoutParams marginImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            marginImg.setMargins(30,78,15,8);
                                            marginImg.height=80;
                                            marginImg.width=80;
                                            ImageView img = new ImageView(getActivity());
                                            img.setLayoutParams(marginImg);
                                            img.setColorFilter(getResources().getColor(R.color.colorPrimary));

                                            switch (info[3].toLowerCase()) {
                                                case "gas":
                                                    img.setImageResource(R.drawable.gas);
                                                    break;
                                                case "energy":
                                                    img.setImageResource(R.drawable.energy);
                                                    break;
                                                case "water":
                                                    img.setImageResource(R.drawable.acqua);
                                                    break;
                                                case "other":
                                                    img.setImageResource(R.drawable.other);
                                                    break;
                                                default:
                                                    img.setImageResource(R.drawable.info);
                                                    break;
                                            }

                                            //Settaggio immagine alert per le bollette non pagate
                                            ImageView imgAlert=new ImageView(getActivity());
                                            imgAlert.setImageResource(R.drawable.alert);
                                            imgAlert.setColorFilter(getResources().getColor(R.color.colorAccent));
                                            imgAlert.setLayoutParams(marginImg);

                                            //settaggio textbox che contengono le caratteristiche delle bollette
                                            LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            tW.setMargins(35,60,35,0);
                                            TextView tw_importo = new TextView(getActivity());
                                            tw_importo.setLayoutParams(tW);
                                            tw_importo.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            tw_importo.setText(new StringBuilder().append(getString(R.string.import_bollette)).append(System.getProperty("line.separator")).append(info[0]).append(System.getProperty("line.separator")).toString());
                                            TextView tw_datascadenza = new TextView(getActivity());
                                            tw_datascadenza.setText(new StringBuilder().append(getString(R.string.expiration_bollette)).append(System.getProperty("line.separator")).append(info[2]).toString());
                                            tw_datascadenza.setLayoutParams(tW);
                                            tw_datascadenza.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            TextView tw_descr = new TextView(getActivity());
                                            tw_descr.setText(new StringBuilder().append(getString(R.string.description_bollette)).append(System.getProperty("line.separator")).append(info[1]).toString());
                                            tw_descr.setLayoutParams(tW);
                                            tw_descr.setTextColor(getResources().getColor(R.color.colorPrimary));

                                            //inserimento delle immagini e delle text box nel linear layout principale
                                            lySingolaBolletta.addView(img);
                                            lySingolaBolletta.addView(tw_datascadenza);
                                            lySingolaBolletta.addView(tw_descr);
                                            lySingolaBolletta.addView(tw_importo);
                                            lySingolaBolletta.addView(imgAlert);
                                            lyPrincipale.addView(lySingolaBolletta);
                                        }

                                    }
                                }

                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });

        //chiamata del Listener per l'esecuzione della query
        rootRef.addListenerForSingleValueEvent(vel);

        return root;
    }


}