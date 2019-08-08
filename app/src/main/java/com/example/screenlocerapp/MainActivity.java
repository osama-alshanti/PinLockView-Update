package com.example.screenlocerapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.screenlocerapp.Fragments.EmergencyFragment;
import com.example.screenlocerapp.Fragments.PinLockFragment;


public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static boolean setEmergencyFragment = false;
    private static int currentFragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout) findViewById(R.id.main_framelayout);

        if(setEmergencyFragment){
            setEmergencyFragment = false;
            setDefaultFragment(new EmergencyFragment());
        }else {
            setDefaultFragment(new PinLockFragment());
        }

    }


    private void setDefaultFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(),fragment);
        transaction.commit();
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        transaction.replace(frameLayout.getId(),fragment);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(setEmergencyFragment){
                setEmergencyFragment = false;
                setFragment(new PinLockFragment());
                return false;
            }
        }

        if ((keyCode == KeyEvent.KEYCODE_HOME)) {

        }

        return false;

    }


}
