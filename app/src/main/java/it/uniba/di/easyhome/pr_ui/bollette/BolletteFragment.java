package it.uniba.di.easyhome.pr_ui.bollette;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.pr_ui.fornitura.FornituraFragment;

public class BolletteFragment extends Fragment {

    private View v;
    private BolletteViewModel bolletteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton fab = (getActivity().findViewById(R.id.fab_plus));
        fab.hide();
        fab.setClickable(false);
         v = inflater.inflate(R.layout.pr_fragment_bollette, container, false);
        configureImageButton();
        return v;
    }

    private void configureImageButton() {
        // TODO Auto-generated method stub
        ImageButton btn = (ImageButton) v.findViewById(R.id.pr_luceButton);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment newFragment = new FornituraFragment();//dichiarazione in quale fragment si vuole passare
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);//definizione in quale layout si va a mettere il nuovo fragmente, cioè layout corrente
                transaction.addToBackStack(null);//permette di aggiungere il fragmente 'vecchio' nella backStack in modo da poter tornare indietro
                transaction.commit();

            }
        });
    }
}