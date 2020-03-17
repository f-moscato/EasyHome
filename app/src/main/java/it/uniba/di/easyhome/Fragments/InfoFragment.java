package it.uniba.di.easyhome.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.uniba.di.easyhome.proprietario.ProprietarioActivity;
import it.uniba.di.easyhome.R;

public class InfoFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Delete ProprietarioActivity's fab
        if(getActivity() instanceof ProprietarioActivity){
            final FloatingActionButton fab= (getActivity().findViewById(R.id.fab_plus));
            fab.setVisibility(View.GONE); }
        View root = inflater.inflate(R.layout.fragment_info, container, false);


        return root;
    }
}