package com.alperguclu.cs.music.verifyproducerdoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alperguclu.cs.music.verifyproducerdoc.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        try {
            ProgressDialog progress = new ProgressDialog(ScanActivity.this);
            progress.setMessage("Kontrol ediliyor...");
            progress.show();
            HttpGetRequest getRequest = new HttpGetRequest();
            String response = getRequest.execute("http://www.telifhaklari.gov.tr/belge-dogrulama-islemi?kod=" + result.getContents()).get();
            progress.hide();

            Document doc = Jsoup.parse(response);
            Element table = doc.select("table").get(0);
            String docType=table.select("tr").get(0).select("td").get(1).text();
            String companyName=table.select("tr").get(3).select("td").get(1).text();;
            String docDate=table.select("tr").get(2).select("td").get(1).text();;
            String producerCode=table.select("tr").get(4).select("td").get(1).text();;

            if(companyName!=null && !companyName.equals("")){
                ResultActivity.resultText="Bu "+docType+" "+companyName+" adına "+docDate+" tarihinde verilmiştir. Yapımcı Kodu: "+producerCode;
            }else{
                ResultActivity.resultText="Bulunamadı.";
            }
        }catch(Exception e){
            ResultActivity.resultText="Bulunamadı.";
            e.printStackTrace();
        }

        Intent intent = new Intent(ScanActivity.this, ResultActivity.class);
        startActivity(intent);
    }
}
