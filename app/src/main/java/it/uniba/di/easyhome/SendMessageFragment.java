package it.uniba.di.easyhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DatabaseReference;

public class SendMessageFragment extends Fragment {
    private String mUsername;
    private String mPhotoUrl;
    private SendMessageViewModel mViewModel;
    private EditText mMessageEditText;
    private DatabaseReference mFirebaseDatabaseReference;


    public static SendMessageFragment newInstance() {
        return new SendMessageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.send_message_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SendMessageViewModel.class);
        // TODO: Use the ViewModel
    }

}
