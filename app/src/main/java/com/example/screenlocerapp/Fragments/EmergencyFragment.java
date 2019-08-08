package com.example.screenlocerapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.screenlocerapp.IndicatorDots;
import com.example.screenlocerapp.PinLockListener;
import com.example.screenlocerapp.PinLockView;
import com.example.screenlocerapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyFragment extends Fragment {

    private Button backBtn,deleteBtn;
    private PinLockView mPinLockView;
    private TextView tv_number;
    public static final String TAG = "PinLockEmergency";
    private FrameLayout parentFrameLayout;

    int length;
    String text;
    String pinText;
    public EmergencyFragment() {
        // Required empty public constructor
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
            tv_number.setText(intermediatePin);
            //mPinLockView.clearInternalPin();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        parentFrameLayout = getActivity().findViewById(R.id.main_framelayout);


        backBtn =  view.findViewById(R.id.back_btn);
        deleteBtn =  view.findViewById(R.id.delete_btn);

        mPinLockView =  view.findViewById(R.id.pin_lock_view);
        tv_number = view.findViewById(R.id.edit_text_number);
        //mIndicatorDots =  view.findViewById(R.id.indicator_dots);


        mPinLockView.setPinLockListener(mPinLockListener);

        mPinLockView.setPinLength(15);
        //mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new PinLockFragment());
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = tv_number.getText().toString();
                if(text != null){

                }
                tv_number.setText(text.substring(0, text.length() - 1));
                mPinLockView.InternalPin();
            }
        });
        return view;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        transaction.replace(parentFrameLayout.getId(),fragment);
        transaction.commit();
    }

}
