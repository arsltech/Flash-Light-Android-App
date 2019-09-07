package com.android.developer.arslan.flashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class info extends AppCompatActivity {

    private ImageButton btnSwitch;
    Button infoo;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private Camera.Parameters params;
    private MediaPlayer mp;
    ToggleButton btn_mute;
    // private TextView Level;
    private boolean ismute=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber)
            {
                if(state==TelephonyManager.CALL_STATE_RINGING){
                    String myString = "0101010101";
                    long blinkDelay = 50; //Delay in ms
                    for (int i = 0; i < TelephonyManager.CALL_STATE_RINGING; i++) {
                        if (myString.charAt(i) == '0') {
                            turnOnFlash();

                        } if(myString.charAt(i) == '1'){
                            turnOffFlash();
                        }


                        try {
                            Thread.sleep(blinkDelay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                }
                if(state==TelephonyManager.CALL_STATE_OFFHOOK){
                    turnOffFlash();
                }

                if(state==TelephonyManager.CALL_STATE_IDLE){

                }
            }
        };
     //   telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(info.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }

        // get the camera
        getCamera();


    }
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {

            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Intent back=new Intent(info.this,MainActivity.class);
        //startActivity(back);
        finish();
    }
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;



        }

    }

    /*
     * Turning Off flash
     */
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound


            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image

        }
    }


}
