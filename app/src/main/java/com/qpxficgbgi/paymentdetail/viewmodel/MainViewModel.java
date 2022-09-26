package com.qpxficgbgi.paymentdetail.viewmodel;

import static com.qpxficgbgi.paymentdetail.utils.ProjectConstants.LOG_TAG;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.qpxficgbgi.paymentdetail.models.Payment;
import com.qpxficgbgi.paymentdetail.repo.MainRepository;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private MainRepository mRepo;

    public MutableLiveData<ArrayList<Payment>> paymentDetailList;
    public MutableLiveData<Pair<Boolean,String>> dataSavedStatus;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(LOG_TAG, "init repo from view model");
        mRepo = MainRepository.getInstance();
        paymentDetailList = new MutableLiveData<>();
        dataSavedStatus = new MutableLiveData<>();
        getSavedPaymentDetail(application);
    }

    public void getSavedPaymentDetail(Context context) {
        paymentDetailList.setValue(mRepo.readPaymentDetailFromTextFile(context));
    }

    public void savePaymentDetail(Context context, ArrayList<Payment> paymentArrayList) {
        dataSavedStatus.setValue(mRepo.savePaymentDetailToTextFile(context, paymentArrayList));
    }
}
