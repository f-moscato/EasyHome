package it.uniba.di.easyhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NtReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context,Intent intent){
        String mex = intent.getStringExtra("prova1");
        Toast.makeText(context,mex,Toast.LENGTH_SHORT).show();
    }


}
