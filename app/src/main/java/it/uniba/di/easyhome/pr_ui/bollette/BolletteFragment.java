package it.uniba.di.easyhome.pr_ui.bollette;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import it.uniba.di.easyhome.Bill;
import it.uniba.di.easyhome.R;

public class BolletteFragment extends Fragment {

    private View v;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton fab = (getActivity().findViewById(R.id.fab_plus));
        fab.hide();
        fab.setClickable(false);
         v = inflater.inflate(R.layout.pr_fragment_bollette, container, false);
        Bill b=new Bill("gas",20,"DADADADDD","",true);

        ArrayList<Bill> items=new ArrayList<>();
        items.add(b);
         ListView lv = (ListView)v.findViewById(R.id.listView1);
        ArrayAdapter viewAdapter= new ArrayAdapter(
            getActivity(),
              android.R.layout.simple_list_item_1,
               items
        );
        lv.setAdapter(viewAdapter);

        return v;
    }


}