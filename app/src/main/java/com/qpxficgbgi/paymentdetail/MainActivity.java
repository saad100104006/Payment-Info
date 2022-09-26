package com.qpxficgbgi.paymentdetail;

import static com.qpxficgbgi.paymentdetail.utils.ProjectConstants.INPUT_DIALOG_NAME;
import static com.qpxficgbgi.paymentdetail.utils.ProjectConstants.LOG_TAG;
import static com.qpxficgbgi.paymentdetail.utils.ProjectConstants.showToast;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.qpxficgbgi.paymentdetail.callbacks.OnInputListener;
import com.qpxficgbgi.paymentdetail.databinding.ActivityMainBinding;
import com.qpxficgbgi.paymentdetail.dialogs.InputDialogFragment;
import com.qpxficgbgi.paymentdetail.models.Payment;
import com.qpxficgbgi.paymentdetail.viewmodel.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnInputListener {

    private ActivityMainBinding binding;

    private MainViewModel activityViewModel;

    private ArrayList<Payment> paymentArrayList;
    private Double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        paymentArrayList = new ArrayList<>();
        setSupportActionBar(binding.toolbar);

        activityViewModel = new ViewModelProvider(MainActivity.this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MainViewModel.class);

        toggleSavedButtonState(false);


        activityViewModel.paymentDetailList.observe(this, list -> {
            Log.d(LOG_TAG, "getting payment detail " + paymentArrayList);
            paymentArrayList.addAll(list);
            showViewForPaymentDetail();
        });

        activityViewModel.dataSavedStatus.observe(this, status -> {
            showToast(status.second, this);
            toggleSavedButtonState(!status.first);

        });

        binding.addPayment.setOnClickListener(view -> {
            InputDialogFragment dialogFragment = new InputDialogFragment();
            dialogFragment.setCancelable(false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(INPUT_DIALOG_NAME);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            dialogFragment.show(ft, INPUT_DIALOG_NAME);
        });

        binding.saveButton.setOnClickListener(view -> {
            activityViewModel.savePaymentDetail(this, paymentArrayList);
        });
    }

    private void showViewForPaymentDetail() {
        totalAmount = 0.0;
        binding.paymentDataContainer.removeAllViews();
        for (Payment payment : paymentArrayList) {
            String chipText = payment.getPaymentType() + "= " + String.format("%.02f", Double.valueOf(payment.getAmount()));

            totalAmount = totalAmount + Double.valueOf(payment.getAmount());
            Chip chip = new Chip(
                    binding.paymentDataContainer.getContext(),
                    null,
                    R.attr.CustomChipChoiceStyle
            );
            chip.setId(payment.getPaymentId());
            Log.d(LOG_TAG, "id for chip" + chip.getId());

            chip.setCloseIconVisible(true);
            chip.setText(chipText);

            chip.setOnCloseIconClickListener(view -> {
                Log.d(LOG_TAG, "Chip click event" + ((Chip) view).getText().toString() + " id " + view.getId());
                int position = getExistingSingleRecord(view.getId());
                if (position != -1) {
                    toggleSavedButtonState(true);
                    totalAmount -= Double.parseDouble(paymentArrayList.get(position).getAmount());
                    binding.totalAmount.setText(String.format("%.02f", totalAmount));
                    paymentArrayList.remove(position);
                    binding.paymentDataContainer.removeViewAt(position);
                }
                showToast(getResources().getString(R.string.click_save), this);
            });

            binding.paymentDataContainer.addView(chip);

        }
        binding.totalAmount.setText(String.format("%.02f", totalAmount));
    }


    private int getExistingSingleRecord(int paymentId) {
        for (int i = 0; i < paymentArrayList.size(); i++) {
            if (paymentArrayList.get(i).getPaymentId() == paymentId) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void sendInput(Payment payment) {
        int position = getExistingSingleRecord(payment.getPaymentId());
        if (getExistingSingleRecord(position) == -1) {
            paymentArrayList.add(payment);
            showViewForPaymentDetail();
            toggleSavedButtonState(true);
        } else {
            showToast(getResources().getString(R.string.payment_exists), this);
        }
    }

    private void toggleSavedButtonState(boolean enable) {
        binding.saveButton.setEnabled(enable);
        binding.saveButton.setClickable(enable);
    }

}