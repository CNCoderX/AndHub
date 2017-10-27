package com.cncoderx.github;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.kohsuke.github.GitHub;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        try {
//            GitHub gitHub = GitHub.connect();
//            gitHub.createToken()
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
