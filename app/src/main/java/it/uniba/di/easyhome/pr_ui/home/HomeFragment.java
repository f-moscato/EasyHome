package it.uniba.di.easyhome.pr_ui.home;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniba.di.easyhome.Bill;
import it.uniba.di.easyhome.House;
import it.uniba.di.easyhome.R;
import it.uniba.di.easyhome.User;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        FloatingActionButton fab= (getActivity().findViewById(R.id.fab_plus));
        fab.show();
        fab.setClickable(true);

        final View root = inflater.inflate(R.layout.pr_fragment_home, container, false);


        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("houses");



        ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final LinearLayout ly=root.findViewById(R.id.ly_home_proprietario);
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        House h=new House(ds.getValue(House.class).getName(),ds.getValue(House.class).getOwner(),ds.getValue(House.class).getInquilini());
                        Log.d(TAG, h.getName() + " / " +h.getOwner());

                        LinearLayout lyl= new LinearLayout(getActivity());
                        lyl.setBackground(getResources().getDrawable(R.drawable.blue_border_rounded_cornwe));
                        LinearLayout.LayoutParams lly_button=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        
                        ImageView img= new ImageView(getActivity());
                        img.setImageResource(R.drawable.bill_inquilino);
                        TextView tw= new TextView(getActivity());
                        tw.setText(h.getName());

                        tw.setGravity(15);



                       /* Button btnShow = new Button(getActivity());
                        btnShow.setText(h.getName());
                        btnShow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        btnShow.setTextColor(getResources().getColor(R.color.fab1_color));
                        LinearLayout.LayoutParams lly_button=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lly_button.setMargins(0,0,0,50);
                        btnShow.setHeight(200);
                        btnShow.setTextSize(25);
                        btnShow.setLayoutParams(lly_button);
                        btnShow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(),"ok", Toast.LENGTH_LONG).show();
                            }
                        });*/

                        // Add Button to LinearLayout
                        if (ly != null) {
                            //lyl.addView(btnShow);
                            lyl.addView(img);
                            lyl.addView(tw);
                            ly.addView(lyl);
                        }
                        /*Query queryBill=query.orderByChild("bills");
                        queryBill.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                        h.addBill(ds.getValue(Bill.class).getTipo(),ds.getValue(Bill.class).getTotale(),ds.getValue(Bill.class).getDescrizione(),ds.getValue(Bill.class).getExpiration(),ds.getValue(Bill.class).isPayed());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Query queryOccupant=query.orderByChild("inquilini");
                        queryOccupant.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/
                    }




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        rootRef.addListenerForSingleValueEvent(vel);



        return root;
    }



}