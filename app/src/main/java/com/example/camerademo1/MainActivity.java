package com.example.camerademo1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private VideoRecord mRecord;
    private StringLogger mVideoLog;
    private String mFile;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"demo.mp4").getAbsolutePath();
        mActivity = this;
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                boolean success = false;
                try {
                    mRecord = new VideoRecord(mFile, mActivity);
                    mVideoLog = new StringLogger(mFile+".txt");
                    mRecord.setOnImageWrittenCallback(new VideoRecord.OnImageWritten() {
                        @Override
                        public void callback() {
                            try {
                                mVideoLog.log("dd");
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    success = mRecord.start();
                } catch (IOException | CameraAccessException e) {
                    mRecord = null;
                    mVideoLog = null;
                    e.printStackTrace();
                }
                if (success == false){
                    mRecord = null;
                    mVideoLog = null;
                }
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecord != null)
                    mRecord.stop();
                if (mVideoLog!= null) {
                    try {
                        mVideoLog.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}