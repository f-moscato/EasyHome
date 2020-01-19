package it.uniba.di.easyhome.pr_ui.bollette;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import it.uniba.di.easyhome.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BolletteFragment extends Fragment {

    private View root;
    int x;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FloatingActionButton fab = (getActivity().findViewById(R.id.fab_plus));
        fab.hide();
        fab.setClickable(false);
         root = inflater.inflate(R.layout.pr_fragment_bollette, container, false);

        final Button buttonNotPayed= root.findViewById(R.id.buttonNotPayed);
        final Button buttonHistory= root.findViewById(R.id.buttonHystoriBills);

        final LinearLayout ly= root.findViewById(R.id.pr_boll_layout);
        ly.removeAllViews();
        buttonNotPayed.setTextColor(getResources().getColor(R.color.colorPrimary));
        buttonNotPayed.setBackgroundColor(Color.TRANSPARENT);
        final ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        House h = new House(ds.getValue(House.class).getName(), ds.getValue(House.class).getOwner(), ds.getValue(House.class).getInquilini(), ds.getValue(House.class).getBills());

                        for (HashMap<String, String> dettagli : h.getBills().values()) {
                            String[] info = dettagli.values().toArray(new String[0]);
                            Log.d(TAG, h.getName() + " / " + dettagli.values());
                            if(info[4].equalsIgnoreCase("false")){
                                LinearLayout lyl = new LinearLayout(getActivity());
                                lyl.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                                LinearLayout.LayoutParams margin=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                margin.setMargins(15,20,15,0);
                                lyl.setLayoutParams(margin);
                                LinearLayout.LayoutParams marginImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                marginImg.setMargins(20,8,15,8);
                                ImageView img = new ImageView(getActivity());
                                img.setLayoutParams(marginImg);
                                    switch (info[3].toLowerCase()) {
                                        case "gas":
                                            img.setImageResource(R.drawable.gas_no);
                                            break;
                                        case "energy":
                                            img.setImageResource(R.drawable.energy_no);
                                            break;
                                        case "water":
                                            img.setImageResource(R.drawable.acqua_no);
                                            break;
                                        case "other":
                                            img.setImageResource(R.drawable.other_no);
                                            break;
                                        default:
                                            img.setImageResource(R.drawable.info);
                                            break;
                                    }


                                LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                tW.setMargins(45,70,45,0);
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
                                lyl.addView(img);
                                lyl.addView(tw_datascadenza);
                                lyl.addView(tw_descr);
                                lyl.addView(tw_importo);
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
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonHistory.setTextColor(getResources().getColor(R.color.colorPrimary));
                buttonHistory.setBackgroundColor(Color.TRANSPARENT);
                buttonNotPayed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                buttonNotPayed.setTextColor(getResources().getColor(R.color.white));


                ly.removeAllViews();

                FirebaseDatabase.getInstance().getReference("houses").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final LinearLayout ly=root.findViewById(R.id.pr_boll_layout);

                        if(dataSnapshot.exists()){
                            for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                House h = new House(ds.getValue(House.class).getName(), ds.getValue(House.class).getOwner(), ds.getValue(House.class).getInquilini(), ds.getValue(House.class).getBills());

                                for (HashMap<String, String> dettagli : h.getBills().values()) {
                                    String[] info = dettagli.values().toArray(new String[0]);

                                    Log.d(TAG, h.getName() + " / " + dettagli.values());
                                    //creazione linearlayout principale della bolletta con settaggio dei margini
                                    LinearLayout lyl = new LinearLayout(getActivity());
                                    lyl.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                                    LinearLayout.LayoutParams margin=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    margin.setMargins(15,20,15,0);
                                    lyl.setLayoutParams(margin);

                                    LinearLayout.LayoutParams marginImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    marginImg.setMargins(20,8,15,8);
                                    ImageView img = new ImageView(getActivity());
                                    img.setLayoutParams(marginImg);
                                    if (info[4].equalsIgnoreCase("true")) {
                                        switch (info[3].toLowerCase()) {
                                            case "gas":
                                                img.setImageResource(R.drawable.gas_yes);
                                                break;
                                            case "energy":
                                                img.setImageResource(R.drawable.energy_yes);
                                                break;
                                            case "water":
                                                img.setImageResource(R.drawable.acqua_yes);
                                                break;
                                            case "other":
                                                img.setImageResource(R.drawable.other_yes);
                                                break;
                                            default:
                                                img.setImageResource(R.drawable.info);
                                                break;
                                        }
                                    }else{
                                        switch (info[3].toLowerCase()) {
                                            case "gas":
                                                img.setImageResource(R.drawable.gas_no);
                                                break;
                                            case "energy":
                                                img.setImageResource(R.drawable.energy_no);
                                                break;
                                            case "water":
                                                img.setImageResource(R.drawable.acqua_no);
                                                break;
                                            case "other":
                                                img.setImageResource(R.drawable.other_no);
                                                break;
                                            default:
                                                img.setImageResource(R.drawable.info);
                                                break;
                                        }
                                    }

                                    LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    tW.setMargins(45,70,45,0);
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
                                    lyl.addView(img);
                                    lyl.addView(tw_datascadenza);
                                    lyl.addView(tw_descr);
                                    lyl.addView(tw_importo);
                                    ly.addView(lyl);
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
        buttonNotPayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNotPayed.setTextColor(getResources().getColor(R.color.colorPrimary));
                buttonNotPayed.setBackgroundColor(Color.TRANSPARENT);
                buttonHistory.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                buttonHistory.setTextColor(getResources().getColor(R.color.white));

                ly.removeAllViews();
                FirebaseDatabase.getInstance().getReference("houses").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final LinearLayout ly= root.findViewById(R.id.pr_boll_layout);

                        if(dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                House h = new House(ds.getValue(House.class).getName(), ds.getValue(House.class).getOwner(), ds.getValue(House.class).getInquilini(), ds.getValue(House.class).getBills());

                                for (HashMap<String, String> dettagli : h.getBills().values()) {
                                    String[] info = dettagli.values().toArray(new String[0]);
                                    Log.d(TAG, h.getName() + " / " + dettagli.values());
                                    if (info[4].equalsIgnoreCase("false")) {
                                        LinearLayout lyl = new LinearLayout(getActivity());
                                        lyl.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                                        LinearLayout.LayoutParams margin=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        margin.setMargins(15,20,15,0);
                                        lyl.setLayoutParams(margin);
                                        LinearLayout.LayoutParams marginImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        marginImg.setMargins(20,8,15,8);
                                        ImageView img = new ImageView(getActivity());
                                        img.setLayoutParams(marginImg);
                                        switch (info[3].toLowerCase()) {
                                            case "gas":
                                                img.setImageResource(R.drawable.gas_no);
                                                break;
                                            case "energy":
                                                img.setImageResource(R.drawable.energy_no);
                                                break;
                                            case "water":
                                                img.setImageResource(R.drawable.acqua_no);
                                                break;
                                            case "other":
                                                img.setImageResource(R.drawable.other_no);
                                                break;
                                            default:
                                                img.setImageResource(R.drawable.info);
                                                break;
                                        }


                                        LinearLayout.LayoutParams tW=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        tW.setMargins(45,70,45,0);
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
                                        lyl.addView(img);
                                        lyl.addView(tw_datascadenza);
                                        lyl.addView(tw_descr);
                                        lyl.addView(tw_importo);
                                        ly.addView(lyl);
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
        rootRef.addListenerForSingleValueEvent(vel);

        return root;
    }


}