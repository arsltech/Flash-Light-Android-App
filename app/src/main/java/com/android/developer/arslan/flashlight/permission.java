package com.android.developer.arslan.flashlight;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class permission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            window.setStatusBarColor(getResources().getColor(R.color.statusBar));
        }


        final Button permisssion=(Button) findViewById(R.id.Btn_accept);
        permisssion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    ActivityCompat.requestPermissions(permission.this,new String[]{Manifest.permission.CAMERA},12);
            }


        });
         }



    private boolean checkReadPermission(){

        int result = ActivityCompat.checkSelfPermission(permission.this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case 12:

                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Intent ii=new Intent(permission.this,MainActivity.class);
                    startActivity(ii);
                    finish();
                }
                else{
                    checkReadPermission();
                }
                break;

        }
    }
}
