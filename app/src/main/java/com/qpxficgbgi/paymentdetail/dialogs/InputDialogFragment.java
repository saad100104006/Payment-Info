package com.qpxficgbgi.paymentdetail.dialogs;

import static com.qpxficgbgi.paymentdetail.utils.ProjectConstants.LOG_TAG;
import static com.qpxficgbgi.paymentdetail.utils.ProjectConstants.showToast;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.qpxficgbgi.paymentdetail.R;
import com.qpxficgbgi.paymentdetail.callbacks.OnInputListener;
import com.qpxficgbgi.paymentdetail.databinding.DialogInputBinding;
import com.qpxficgbgi.paymentdetail.models.Payment;

import java.util.Objects;

public class InputDialogFragment extends DialogFragment {

    private DialogInputBinding binding;
    private int paymentId = 0;
    private String paymentType = "";

    private OnInputListener mOnInputListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInputBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        paymentType = binding.spinner.getSelectedItem().toString();

        binding.okButton.setOnClickListener(view1 -> {
            String amount = Objects.requireNonNull(binding.amount.getText()).toString();
            String provider = Objects.requireNonNull(binding.provider.getText()).toString();
            String transRef = Objects.requireNonNull(binding.transactionReference.getText()).toString();
            if (proceedToNext(amount)) {
                Payment payment = new Payment(paymentId, amount, paymentType, provider, transRef);
                Log.d(LOG_TAG, "payment id is" + paymentId + "amount is " + amount + " payment type is " + paymentType);
                mOnInputListener.sendInput(payment);
                dismiss();
            }
            showToast("Click Save to update this change", getActivity());
        });

        binding.cancelButton.setOnClickListener(view1 -> dismiss());

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paymentId = i;
                paymentType = adapterView.getItemAtPosition(i).toString();
                Log.d(LOG_TAG, "payment id is " + paymentId + " payment type is " + paymentType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean proceedToNext(String amount) {
        if (amount.isEmpty()) {
            showToast("Please Enter Amount", getContext());
            return false;
        }

        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}