package it.uniba.di.easyhome.pr_ui.fornitura;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.uniba.di.easyhome.ProprietarioActivity;
import it.uniba.di.easyhome.R;

public class FornituraFragment extends Fragment {


    public static FornituraFragment newInstance() {
        return new FornituraFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_plus);
        fab.hide();
        fab.setClickable(false);
        ((ProprietarioActivity) getActivity()).setActionBarTitle("Your title");//cambiare il nome nell'actionBar in base alla other oppure all'ente
         return inflater.inflate(R.layout.fornitura_fragment, container, false);
    }



}
