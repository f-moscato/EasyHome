package it.uniba.di.easyhome.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.SharedPref;
import it.uniba.di.easyhome.proprietario.ProprietarioActivity;

public class ToolsFragment extends Fragment {
    SharedPref sharedpref;


private Switch myswitch;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        ImageButton it= root.findViewById(R.id.ButtonItalia);
        ImageButton gb= root.findViewById(R.id.ButtonGb);
        //Delete ProprietarioActivity's fab
        if(getActivity() instanceof ProprietarioActivity){
        final FloatingActionButton fab= (getActivity().findViewById(R.id.fab_plus));
        fab.setVisibility(View.GONE);}
        sharedpref=new SharedPref(getActivity());
        myswitch=(Switch)root.findViewById(R.id.switch1);
                if(sharedpref.loadNightModeState()==true){
                    myswitch.setChecked(true);
                }else{
                    myswitch.setChecked(false);
                }
                myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                          sharedpref.setNightModeState(true);

                        }
                        else{
                            sharedpref.setNightModeState(false);

                        }
                        restartApp();
                    }
                });

        gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpref.changeLang("en");
                restartApp();
            }

        });
        it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedpref.changeLang("it");
                restartApp();
            }
        });


        return root;
    }
    public void restartApp() {
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().overridePendingTransition(0, 0);
        getActivity().finish();

        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
    }

}