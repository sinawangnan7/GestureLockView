package com.wangnan.gesturelockview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wangnan.gesturelockview.R;
import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;

/**
 * @ClassName: ImageGestureLockActivity
 * @Description: 手势解锁视图（图片代替绘制）
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public class ImageGestureLockActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    TextView mPassword;

    GestureLockView mGestureLockView;

    CheckBox mNormalCheckBox, mPressCheckBox, mErrorCheckBox, mAnimCheckBox, mLineTopCheckBox;

    SeekBar mAnimDurationSeekbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initView();
        initViewListener();
    }

    private void initView() {
        mPassword = (TextView) findViewById(R.id.tv_current_passord);
        mGestureLockView = (GestureLockView) findViewById(R.id.glv);
        mNormalCheckBox = (CheckBox) findViewById(R.id.cb_normal);
        mPressCheckBox = (CheckBox) findViewById(R.id.cb_press);
        mErrorCheckBox = (CheckBox) findViewById(R.id.cb_error);
        mAnimCheckBox = (CheckBox) findViewById(R.id.cb_anim);
        mAnimDurationSeekbar = (SeekBar) findViewById(R.id.sb_anim_duration);
        mLineTopCheckBox = (CheckBox) findViewById(R.id.cb_linetop);
    }

    private void initViewListener() {

        // 设置手势解锁监听器
        mGestureLockView.setGestureLockListener(new OnGestureLockListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(String progress) {
                mPassword.setText("当前密码: " + progress);
            }

            @Override
            public void onComplete(String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                } else if ("012345678".equals(result)) {
                    Toast.makeText(ImageGestureLockActivity.this, "密码正确O(∩_∩)O~", Toast.LENGTH_SHORT).show();
                    mGestureLockView.clearView();
                } else {
                    Toast.makeText(ImageGestureLockActivity.this, "密码错误o(╯□╰)o~", Toast.LENGTH_SHORT).show();
                    mGestureLockView.showErrorStatus(1000);
                }
            }
        });

        // 设置CheckBox改变监听器
        mNormalCheckBox.setOnCheckedChangeListener(this);
        mPressCheckBox.setOnCheckedChangeListener(this);
        mErrorCheckBox.setOnCheckedChangeListener(this);
        mAnimCheckBox.setOnCheckedChangeListener(this);
        mLineTopCheckBox.setOnCheckedChangeListener(this);

        mAnimDurationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGestureLockView.setAnimationDuration(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mNormalCheckBox) {
            if (isChecked) {
                mGestureLockView.setNormalImageResource(R.drawable.normal_image);
            } else {
                mGestureLockView.setNormalImageResource(0);
            }
        } else if (buttonView == mPressCheckBox) {
            if (isChecked) {
                mGestureLockView.setPressImageResource(R.drawable.press_image);
            } else {
                mGestureLockView.setPressImageResource(0);
            }
        } else if (buttonView == mErrorCheckBox) {
            if (isChecked) {
                mGestureLockView.setErrorImageResource(R.drawable.error_image);
            } else {
                mGestureLockView.setErrorImageResource(0);
            }
        } else if (buttonView == mAnimCheckBox) {
            mGestureLockView.setUseAnim(isChecked);
        } else if (buttonView == mLineTopCheckBox) {
            mGestureLockView.setLineTop(isChecked);
        }
    }
}
