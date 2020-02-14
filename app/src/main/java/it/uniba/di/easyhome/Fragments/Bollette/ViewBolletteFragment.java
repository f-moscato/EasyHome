package it.uniba.di.easyhome.Fragments.Bollette;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import java.util.ArrayList;

import it.uniba.di.easyhome.Bill;
import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.SharedPref;
import it.uniba.di.easyhome.User;
import it.uniba.di.easyhome.proprietario.ProprietarioActivity;

public class ViewBolletteFragment extends Fragment {
    SharedPref sharedpref;
    private View root;

    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
    if(getActivity().equals(ProprietarioActivity.class)) {
        FloatingActionButton fab = (getActivity().findViewById(R.id.fab_plus));
        fab.hide();
        fab.setClickable(false);
    }

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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dsCase: dataSnapshot.getChildren()) {
                        House h=new House(
                                dsCase.getValue(House.class).getName()
                                ,dsCase.getValue(House.class).getOwner()
                                ,dsCase.getValue(House.class).getInquilini()
                                ,dsCase.getValue(House.class).getBills()
                                ,dsCase.getValue(House.class).getSsid());
                        if(h.getName().equalsIgnoreCase(bundle.getString("nomeCasa"))){

                            ArrayList<Bill> sortedBill=h.ordinamentoTemporaleBollette();
                            for (Bill dettagli : sortedBill) {

                                if(dettagli.isPayed().equalsIgnoreCase("false")){

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
                                    sharedpref=new SharedPref(getContext());
                                    marginImg.height=80;
                                    marginImg.width=80;
                                    img.setLayoutParams(marginImg);
                                    img.setColorFilter(getResources().getColor(R.color.colorPrimary));
                                    if(sharedpref.loadNightModeState()){
                                         img.setColorFilter(getResources().getColor(R.color.colorAccent));
                                    }
                                    switch (dettagli.getType().toLowerCase()) {
                                        case "gas":
                                            img.setImageResource(R.drawable.gas);
                                            break;
                                        case "energy":
                                        case "elettricità":
                                            img.setImageResource(R.drawable.energy);
                                            break;
                                        case "water":
                                        case "acqua":
                                            img.setImageResource(R.drawable.acqua);
                                            break;
                                        case "other":
                                        case "altro":
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
                                    if(sharedpref.loadNightModeState()){
                                        imgAlert.setColorFilter(getResources().getColor(R.color.colorNotAtHome));
                                    }

                                    //controllo se l'utente che ha effettuato l'accesso al fragemnt è proprietario
                                    // SOLO I PROPIETARI POSSONO DEFINIRE UN BOLLETTA PAGATA O MENO
                                    DatabaseReference refUser=FirebaseDatabase.getInstance().getReference("users");
                                    refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dsUser:dataSnapshot.getChildren()){
                                                if(currentUser.getUid().equals(dsUser.getKey()) && dsUser.getValue(User.class).getRole().equalsIgnoreCase("P")){

                                                    //creazione del pop up per avere la conferma del pagamento della bolletta in questione
                                                    imgAlert.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setTitle(getString(R.string.popup_pagamentoBolletta)).
                                                                    setMessage(getString(R.string.popup_pagamentoBolletta_corpo));
                                                            builder.setPositiveButton(R.string.pop_up_logout_yes,
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("houses/"+dsCase.getKey()+"/bills");
                                                                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    for(DataSnapshot dsBill:dataSnapshot.getChildren()){
                                                                                        if(dsBill.getValue(Bill.class).getType().equalsIgnoreCase(dettagli.getType()) && dsBill.getValue(Bill.class).getExpiration().equalsIgnoreCase(dettagli.getExpiration()) && dsBill.getValue(Bill.class).getTotal().equalsIgnoreCase(dettagli.getTotal())){
                                                                                            FirebaseDatabase.getInstance().getReference("houses/"+dsCase.getKey()+"/bills/"+dsBill.getKey()+"/payed").setValue("true");

                                                                                            //refresh del fragment
                                                                                            Fragment frg = null;
                                                                                            frg = getFragmentManager().findFragmentByTag("Bills");
                                                                                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                                            ft.detach(frg);
                                                                                            ft.attach(frg);
                                                                                            ft.commit();
                                                                                            break;
                                                                                        }

                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                        }
                                                                    });
                                                            builder.setNegativeButton(R.string.pop_out_logout_no,
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            Fragment frg = null;
                                                                            frg = getFragmentManager().findFragmentByTag("Bills");
                                                                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                            ft.detach(frg);
                                                                            ft.attach(frg);
                                                                            ft.commit();
                                                                        }
                                                                    });
                                                            AlertDialog alert11 = builder.create();
                                                            alert11.show();
                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    //settaggio textbox che contengono le caratteristiche delle bollette
                                    LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    tW.setMargins(35,60,35,0);
                                    TextView tw_importo = new TextView(getActivity());
                                    tw_importo.setLayoutParams(tW);
                                    tw_importo.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    tw_importo.setText(new StringBuilder().append(getString(R.string.import_bollette)).append(System.getProperty("line.separator")).append(dettagli.getTotal()).append(System.getProperty("line.separator")).toString());
                                    TextView tw_datascadenza = new TextView(getActivity());
                                    tw_datascadenza.setText(new StringBuilder().append(getString(R.string.expiration_bollette)).append(System.getProperty("line.separator")).append(dettagli.getExpiration()).toString());
                                    tw_datascadenza.setLayoutParams(tW);
                                    tw_datascadenza.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    TextView tw_descr = new TextView(getActivity());
                                    tw_descr.setText(new StringBuilder().append(getString(R.string.description_bollette)).append(System.getProperty("line.separator")).append(dettagli.getDescription()).toString());
                                    tw_descr.setLayoutParams(tW);
                                    tw_descr.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    if(sharedpref.loadNightModeState()){
                                        tw_importo.setTextColor(Color.WHITE);
                                        tw_datascadenza.setTextColor(Color.WHITE);
                                        tw_descr.setTextColor(Color.WHITE);
                                    }

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
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final LinearLayout lyPrincipale=root.findViewById(R.id.pr_boll_layout);

                        if(dataSnapshot.exists()){
                            for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                House h=new House(
                                        ds.getValue(House.class).getName()
                                        ,ds.getValue(House.class).getOwner()
                                        ,ds.getValue(House.class).getInquilini()
                                        ,ds.getValue(House.class).getBills()
                                        ,ds.getValue(House.class).getSsid());
                                if(h.getName().equalsIgnoreCase(bundle.getString("nomeCasa"))){
                                    ArrayList<Bill> sortedBill=h.ordinamentoTemporaleBolletteStorico();
                                    for (Bill dettagli : sortedBill) {


                                        if (dettagli.isPayed().equalsIgnoreCase("true")) {
                                            //creazione linearlayout principale della bolletta con settaggio dei margini
                                            LinearLayout lySingolaBolletta = new LinearLayout(getActivity());
                                            lySingolaBolletta.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                                            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            margin.setMargins(20, 20, 15, 0);
                                            lySingolaBolletta.setLayoutParams(margin);

                                            //Settaggio margini immagine bolletta e caratteristiche
                                            LinearLayout.LayoutParams marginImg = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            marginImg.setMargins(30, 78, 15, 8);
                                            marginImg.height = 80;
                                            marginImg.width = 80;
                                            ImageView img = new ImageView(getActivity());
                                            img.setLayoutParams(marginImg);
                                            img.setColorFilter(getResources().getColor(R.color.colorPrimary));
                                            if(sharedpref.loadNightModeState()){
                                                img.setColorFilter(getResources().getColor(R.color.colorAccent));
                                            }

                                            //settaggio textbox che contengono le caratteristiche delle bollette
                                            LinearLayout.LayoutParams tW = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            tW.setMargins(35, 60, 35, 0);
                                            TextView tw_importo = new TextView(getActivity());
                                            tw_importo.setLayoutParams(tW);
                                            tw_importo.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            tw_importo.setText(new StringBuilder().append(getString(R.string.import_bollette)).append(System.getProperty("line.separator")).append(dettagli.getTotal()).append(System.getProperty("line.separator")).toString());
                                            TextView tw_datascadenza = new TextView(getActivity());
                                            tw_datascadenza.setText(new StringBuilder().append(getString(R.string.expiration_bollette)).append(System.getProperty("line.separator")).append(dettagli.getExpiration()).toString());
                                            tw_datascadenza.setLayoutParams(tW);
                                            tw_datascadenza.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            TextView tw_descr = new TextView(getActivity());
                                            tw_descr.setText(new StringBuilder().append(getString(R.string.description_bollette)).append(System.getProperty("line.separator")).append(dettagli.getDescription()).toString());
                                            tw_descr.setLayoutParams(tW);
                                            tw_descr.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            if(sharedpref.loadNightModeState()){
                                                tw_importo.setTextColor(Color.WHITE);
                                                tw_datascadenza.setTextColor(Color.WHITE);
                                                tw_descr.setTextColor(Color.WHITE);
                                            }

                                            //inserimento immagini in base alla loro natura e conseguente aggiunta di tutti gli elementi nel linear layout principale
                                            if (dettagli.isPayed().equalsIgnoreCase("true")) {

                                                switch (dettagli.getType().toLowerCase()) {
                                                    case "gas":
                                                        img.setImageResource(R.drawable.gas);
                                                        break;
                                                    case "energy":
                                                    case "elettricità":
                                                        img.setImageResource(R.drawable.energy);
                                                        break;
                                                    case "water":
                                                    case "acqua":
                                                        img.setImageResource(R.drawable.acqua);
                                                        break;
                                                    case "other":
                                                    case "altro":
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
                                            }
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

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final LinearLayout lyPrincipale= root.findViewById(R.id.pr_boll_layout);

                        if(dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                House h=new House(
                                        ds.getValue(House.class).getName()
                                        ,ds.getValue(House.class).getOwner()
                                        ,ds.getValue(House.class).getInquilini()
                                        ,ds.getValue(House.class).getBills()
                                        ,ds.getValue(House.class).getSsid());
                                if(h.getName().equalsIgnoreCase(bundle.getString("nomeCasa"))){
                                    ArrayList<Bill> sortedBill=h.ordinamentoTemporaleBollette();
                                    for (Bill dettagli : sortedBill) {
                                        if (dettagli.isPayed().equalsIgnoreCase("false")) {
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
                                            if(sharedpref.loadNightModeState()){
                                                img.setColorFilter(getResources().getColor(R.color.colorAccent));
                                            }

                                            switch (dettagli.getType().toLowerCase()) {
                                                case "gas":
                                                    img.setImageResource(R.drawable.gas);
                                                    break;
                                                case "energy":
                                                case "elettricità":
                                                    img.setImageResource(R.drawable.energy);
                                                    break;
                                                case "water":
                                                case "acqua":
                                                    img.setImageResource(R.drawable.acqua);
                                                    break;
                                                case "other":
                                                case "altro":
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
                                            if(sharedpref.loadNightModeState()){
                                                imgAlert.setColorFilter(getResources().getColor(R.color.colorNotAtHome));
                                            }
                                            //settaggio textbox che contengono le caratteristiche delle bollette
                                            LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            tW.setMargins(35,60,35,0);
                                            TextView tw_importo = new TextView(getActivity());
                                            tw_importo.setLayoutParams(tW);
                                            tw_importo.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            tw_importo.setText(new StringBuilder().append(getString(R.string.import_bollette)).append(System.getProperty("line.separator")).append(dettagli.getTotal()).append(System.getProperty("line.separator")).toString());
                                            TextView tw_datascadenza = new TextView(getActivity());
                                            tw_datascadenza.setText(new StringBuilder().append(getString(R.string.expiration_bollette)).append(System.getProperty("line.separator")).append(dettagli.getExpiration()).toString());
                                            tw_datascadenza.setLayoutParams(tW);
                                            tw_datascadenza.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            TextView tw_descr = new TextView(getActivity());
                                            tw_descr.setText(new StringBuilder().append(getString(R.string.description_bollette)).append(System.getProperty("line.separator")).append(dettagli.getDescription()).toString());
                                            tw_descr.setLayoutParams(tW);
                                            tw_descr.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            if(sharedpref.loadNightModeState()){
                                                tw_importo.setTextColor(Color.WHITE);
                                                tw_datascadenza.setTextColor(Color.WHITE);
                                                tw_descr.setTextColor(Color.WHITE);
                                            }

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