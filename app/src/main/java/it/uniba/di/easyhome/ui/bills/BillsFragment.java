package it.uniba.di.easyhome.ui.bills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.pr_ui.bollette.BolletteViewModel;

public class BillsFragment extends Fragment {

    private BolletteViewModel bolletteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton fab= (getActivity().findViewById(R.id.fab_plus));
        fab.hide();
        fab.setClickable(false);
        bolletteViewModel =
                ViewModelProviders.of(this).get(BolletteViewModel.class);
        View root = inflater.inflate(R.layout.inq_fragment_bills, container, false);

        return root;
    }
}