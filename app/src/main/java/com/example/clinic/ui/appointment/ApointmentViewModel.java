package com.example.clinic.ui.appointment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ApointmentViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ApointmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}