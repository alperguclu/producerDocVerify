package com.alperguclu.cs.music.verifyproducerdoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    public static String resultText;
    public static TextView tvresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvresult = (TextView) findViewById(R.id.textView);
        tvresult.setText(resultText);
    }
}
