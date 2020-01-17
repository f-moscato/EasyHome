package it.uniba.di.easyhome.ui.bills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.uniba.di.easyhome.R;

public class BillsFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton fab= (getActivity().findViewById(R.id.fab_plus));
        fab.hide();
        fab.setClickable(false);
        View root = inflater.inflate(R.layout.inq_fragment_bills, container, false);

        return root;
    }
}