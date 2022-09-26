package com.qpxficgbgi.paymentdetail.repo;

import static com.qpxficgbgi.paymentdetail.utils.ProjectConstants.FILE_NAME;
import static com.qpxficgbgi.paymentdetail.utils.ProjectConstants.LOG_TAG;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qpxficgbgi.paymentdetail.models.Payment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainRepository {

    private static MainRepository instance;

    public static MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }

    public Pair<Boolean,String> savePaymentDetailToTextFile(Context context, ArrayList<Payment> paymentArrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(paymentArrayList);
        String root = context.getExternalFilesDir(null).getAbsolutePath();

        Log.d(LOG_TAG, "root file " + root);
        File file = new File(root);
        file.mkdirs();
        try {
            File out_file = new File(file, FILE_NAME);
            if (out_file.exists()) {
                out_file.delete();
            }

            Log.d(LOG_TAG, "final file " + out_file);
            FileWriter writer = new FileWriter(out_file, true);
            writer.append(json);
            writer.flush();
            writer.close();

        } catch (Exception ex) {
            Log.d(LOG_TAG, "exception while saving file " + ex.getMessage());
            return new Pair(false,ex.getMessage());


        }
        return new Pair(true,"Data Saved Successfully");
    }


    public ArrayList<Payment> readPaymentDetailFromTextFile(Context context) {
        StringBuilder myData = new StringBuilder();
        try {
            String root = context.getExternalFilesDir(null).getAbsolutePath();
            File myExternalFile = new File(root, FILE_NAME);

            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData.append(strLine);
            }
            in.close();
            Log.d(LOG_TAG, "reading file completed" + myData);
            if (!myData.toString().isEmpty()) {
                Gson gson = new Gson();
                return gson.fromJson(myData.toString(), new TypeToken<ArrayList<Payment>>() {
                }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "exception while reading file " + e.getMessage());
        }

        return new ArrayList<>();
    }


}
