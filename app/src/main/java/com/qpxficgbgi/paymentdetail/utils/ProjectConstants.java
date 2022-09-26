package com.qpxficgbgi.paymentdetail.utils;

import android.content.Context;
import android.widget.Toast;

public class ProjectConstants {

    public static final String FILE_NAME="LastPayment.txt";
    public static final String INPUT_DIALOG_NAME="input_dialog";
    public static final String LOG_TAG="Payment_log_tag";

    public static void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
