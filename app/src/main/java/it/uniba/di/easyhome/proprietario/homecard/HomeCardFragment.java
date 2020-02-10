package it.uniba.di.easyhome.proprietario.homecard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.proprietario.bollette.AddBolletteFragment;
import it.uniba.di.easyhome.proprietario.bollette.BolletteFragment;

public class HomeCardFragment extends Fragment{

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.home_card_fragment, container, false);
        final FloatingActionButton add_bill_fab= (getActivity().findViewById(R.id.fab2_plus));
        final Bundle bundle=getArguments();


        final TextView twNomeCasa=root.findViewById(R.id.nomeCasaProprietario);
        twNomeCasa.setText(bundle.getString("nomeCasa"));
      final  String pr;
       pr=bundle.getString("Casa");

        LinearLayout linearLayoutBills=root.findViewById(R.id.ly_bill_casa_proprietario);
        linearLayoutBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("nomeCasa",twNomeCasa.getText().toString());
                BolletteFragment bolletteFragment=new BolletteFragment() ;
                bolletteFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.nav_host_fragment,bolletteFragment,"Bills");
                fragmentTransaction.commit();
            }
        });
        add_bill_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("Casa",pr);
                Fragment newFragment = new AddBolletteFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment,"AddBills");
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return root;


    }

}
