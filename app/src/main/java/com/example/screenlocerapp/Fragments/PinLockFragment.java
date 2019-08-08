package com.example.screenlocerapp.Fragments;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.screenlocerapp.IndicatorDots;
import com.example.screenlocerapp.PinLockListener;
import com.example.screenlocerapp.PinLockView;
import com.example.screenlocerapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinLockFragment extends Fragment {

    public static final String TAG = "PinLockPhone";
    private Button emergencyBtn,backBtn;
    private TextView tvLockPhoneTitle,tvLockPhoneTimer;
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private int count = 0;
    MediaPlayer unlockSound;
    private String myUnlockNumber = "1212";

    private FrameLayout parentFrameLayout;

    public PinLockFragment() {
        // Required empty public constructor
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
            if (count < 5){
                if(pin.equals(myUnlockNumber)){
                    count = 0;
                    Toast.makeText(getActivity(), "Unlock!", Toast.LENGTH_SHORT).show();
                    unlockSound.start();
                    mPinLockView.resetPinLockView();
                    onFinish();
                }else{
                    mPinLockView.resetPinLockView();
                    count++;
                    YoYo.with(Techniques.Tada).duration(500).repeat(0).playOn(mIndicatorDots);
                    VibrateMode();
                    Toast.makeText(getActivity(), "Try Again!", Toast.LENGTH_SHORT).show();
                }
            }else{
                count = 0;
                tvLockPhoneTitle.setVisibility(View.VISIBLE);
                tvLockPhoneTimer.setVisibility(View.VISIBLE);
                emergencyBtn.setEnabled(false);
                backBtn.setEnabled(false);
                mPinLockView.setVisibility(View.INVISIBLE);
                mIndicatorDots.setVisibility(View.INVISIBLE);

                new CountDownTimer(30000,1000) {

                    public void onTick(long millisUntilFinished) {
                        tvLockPhoneTimer.setText("Try again in "+ millisUntilFinished / 1000+" seconds");
                    }

                    public void onFinish() {
                        mPinLockView.resetPinLockView();
                        tvLockPhoneTitle.setVisibility(View.GONE);
                        tvLockPhoneTimer.setVisibility(View.GONE);
                        mPinLockView.setVisibility(View.VISIBLE);
                        mIndicatorDots.setVisibility(View.VISIBLE);
                        emergencyBtn.setEnabled(true);
                        backBtn.setEnabled(true);
                    }
                }.start();
            }

        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View view = inflater.inflate(R.layout.fragment_pin_lock, container, false);

        parentFrameLayout = getActivity().findViewById(R.id.main_framelayout);


        mPinLockView =  view.findViewById(R.id.pin_lock_view);
        mIndicatorDots =  view.findViewById(R.id.indicator_dots);

        emergencyBtn =  view.findViewById(R.id.emergency_btn);
        backBtn =  view.findViewById(R.id.back_btn);

        tvLockPhoneTitle =  view.findViewById(R.id.tv_lock_phone_title);
        tvLockPhoneTimer =  view.findViewById(R.id.tv_lock_phone_timer);

        unlockSound =  MediaPlayer.create(getActivity(),R.raw.music);
        emergencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new EmergencyFragment());
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onFinish();
            }
        });

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);


        mPinLockView.setPinLength(4);
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);


        return view;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left); //from right to left
        transaction.replace(parentFrameLayout.getId(),fragment);
        transaction.commit();
    }

    private void VibrateMode(){
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void onFinish(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out_down);
        transaction.replace(parentFrameLayout.getId(),new PinLockFragment());
        transaction.commit();
        getActivity().finish();
    }



}
