package com.android.developer.arslan.flashlight;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        infoo=(Button) findViewById(R.id.btninfo);
        infoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent in=new Intent(MainActivity.this,info.class);
                startActivity(in);




            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            window.setStatusBarColor(getResources().getColor(R.color.statusBar));
        }
        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);

        //  Level=(TextView) findViewById(R.id.level);

        btn_mute = (ToggleButton) findViewById(R.id.mute);

        //  this.registerReceiver(this.batteryinfo,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        btn_mute.setText(null);
        btn_mute.setTextOff(null);
        btn_mute.setTextOn(null);



        if(checkReadPermission()){

            functionsMain();
        }
        else{


            Intent i=new Intent(MainActivity.this,permission.class);
            startActivity(i);
            finish();



        }
    }


        public void functionsMain(){

            btn_mute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (ismute) {
                        mute();
                        ismute = false;
                    } else {
                        unmute();
                        ismute = true;
                    }
                }
            });

            btnSwitch.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                       if (isFlashOn) {
                           // turn off flash
                           turnOffFlash();
                       } else {
                           // turn on flash
                           turnOnFlash();
                       }


                }
            });

            // flash switch button


		/*
		 * First check if device is supporting flashlight or not
		 */
            hasFlash = getApplicationContext().getPackageManager()
                    .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

            if (!hasFlash) {
                // device doesn't support flash
                // Show alert message and close the application
                AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
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

            // displaying button image
            toggleButtonImage();
        }
    /*
     * Get the camera
     */
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {

            }
        }
    }

    /*
     * Turning On flash
     */
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            toggleButtonImage();
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
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            toggleButtonImage();
        }
    }

    /*
     * Playing sound will play button toggle sound on flash on / off
     */
    private void playSound() {
        if (isFlashOn) {
            mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_off);
        } else {
            mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_on);
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }

    /*
     * Toggle switch button images changing image states to on / off
     */
    private void toggleButtonImage() {
        if (isFlashOn) {
            btnSwitch.setImageResource(R.drawable.btn_switch_on);
        } else {
            btnSwitch.setImageResource(R.drawable.btn_switch_off);
        }
    }


    private void mute() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    public void unmute() {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    private boolean checkReadPermission(){

        int result = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case 12:

                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {

                }
                else{
                    checkReadPermission();
                }
                break;

        }

    }
    private void blink(final int delay, final int times) {
        Thread t = new Thread() {
            public void run() {
                try {

                    for (int i=0; i < times*2; i++) {
                        if (isFlashOn) {
                            turnOffFlash();
                        } else {
                            turnOnFlash();
                        }
                        sleep(delay);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
/*
    private BroadcastReceiver batteryinfo=new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            int level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            Level.setText(level+"%");

        }
    };


*/

}