package it.uniba.di.easyhome.proprietario.bollette;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.proprietario.home.HomeFragment;

public class AddBolletteFragment extends Fragment {
    DatePickerDialog dpd;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_bollette_fragment, container, false);
        final FloatingActionButton add_boll_fab= (getActivity().findViewById(R.id.fab2_plus));
        FloatingActionButton add_std_fab= (getActivity().findViewById(R.id.fab3_plus));
        final TextView textIndietro= (TextView) getActivity().findViewById(R.id.agg_boll);
        final Calendar c=Calendar.getInstance();
        final Spinner mySpinner = (Spinner) root.findViewById(R.id.spinner);
        final Spinner spinnerPay = (Spinner) root.findViewById(R.id.spinnerPay);
        final TextView desc=(TextView) root.findViewById(R.id.data);
        textIndietro.setText(getResources().getString(R.string.back));
        final Button dataBt=(Button) root.findViewById(R.id.dataBt);
        add_boll_fab.setImageDrawable(getResources().getDrawable(R.drawable.indietro));

        mySpinner.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1 ,getResources().getStringArray(R.array.tipo) ));
        spinnerPay.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1 ,getResources().getStringArray(R.array.pay) ));
        dataBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day=c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);
                dpd=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        desc.setText(dayOfMonth+"/"+(month+1)+"/"+ year);
                    }
                },day,month,year);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });
        add_boll_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_boll_fab.setImageDrawable(getResources().getDrawable(R.drawable.bill));
                textIndietro.setText(getResources().getString(R.string.bill));
                // Create new fragment and transaction
                Fragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();

            }

        });

        return root;

    }



}
