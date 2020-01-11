package it.uniba.di.easyhome.pr_ui.bollette;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BolletteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BolletteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}