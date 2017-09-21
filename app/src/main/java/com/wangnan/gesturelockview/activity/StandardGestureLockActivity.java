package com.wangnan.gesturelockview.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wangnan.gesturelockview.R;
import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;
import com.wangnan.library.painter.Painter;

/**
 * @ClassName: StandardGestureLockActivity
 * @Description: 手势解锁视图（原始）
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public class StandardGestureLockActivity extends AppCompatActivity {

    GestureLockView mGestureLockView;

    TextView mPassword;

    CheckBox mGuidesCheckBox, mColorCheckBox, mAnimCheckBox, mVibrateCheckBox;

    SeekBar mAnimDurationSeekbar, mAnimScaleRateSeekbar, mVibrateDurationSeekbar, mRadiusSeekbar, mLineThicknesSeekbar;

    RadioGroup mAnimScaleModeRadioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        initView();
        initListener();
    }

    private void initView() {
        mGestureLockView = (GestureLockView) findViewById(R.id.glv);
        mPassword = (TextView) findViewById(R.id.tv_current_passord);
        mGuidesCheckBox = (CheckBox) findViewById(R.id.cb_guide);
        mColorCheckBox = (CheckBox) findViewById(R.id.cb_color);
        mAnimCheckBox = (CheckBox) findViewById(R.id.cb_anim);
        mAnimDurationSeekbar = (SeekBar) findViewById(R.id.sb_anim_duration);
        mAnimScaleModeRadioGroup = (RadioGroup) findViewById(R.id.rg);
        mAnimScaleRateSeekbar = (SeekBar) findViewById(R.id.sb_anim_rate);
        mVibrateCheckBox = (CheckBox) findViewById(R.id.cb_vibrate);
        mVibrateDurationSeekbar = (SeekBar) findViewById(R.id.sb_vibrate_duration);
        mRadiusSeekbar = (SeekBar) findViewById(R.id.sb_radius);
        mLineThicknesSeekbar = (SeekBar) findViewById(R.id.sb_line_thickness);
    }

    private void initListener() {

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
                    Toast.makeText(StandardGestureLockActivity.this, "密码正确O(∩_∩)O~", Toast.LENGTH_SHORT).show();
                    mGestureLockView.clearView();
                } else {
                    Toast.makeText(StandardGestureLockActivity.this, "密码错误o(╯□╰)o~", Toast.LENGTH_SHORT).show();
                    mGestureLockView.showErrorStatus(1000);
                }
            }
        });

        // 设置是否显示辅助线
        mGuidesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mGestureLockView.setShowGuides(isChecked);
            }
        });

        // 设置是否修改颜色值
        mColorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mGestureLockView.setNormalColor(Color.GREEN);
                    mGestureLockView.setPressColor(Color.BLUE);
                    mGestureLockView.setErrorColor(Color.MAGENTA);
                } else {
                    mGestureLockView.setNormalColor(Painter.NORMAL_COLOR);
                    mGestureLockView.setPressColor(Painter.PRESS_COLOR);
                    mGestureLockView.setErrorColor(Painter.ERROR_COLOR);
                }
            }
        });

        // 设置是否使用动画
        mAnimCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mGestureLockView.setUseAnim(isChecked);
            }
        });

        // 修改动画持续时间
        mAnimDurationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGestureLockView.setAnimationDuration(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // 修改动画缩放模式
        mAnimScaleModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_reverse) {
                    mGestureLockView.setAnimationScaleMode(GestureLockView.REVERSE);
                } else {
                    mGestureLockView.setAnimationScaleMode(GestureLockView.NORMAL);
                }
            }
        });

        // 修改动画缩放比例
        mAnimScaleRateSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGestureLockView.setAnimationScaleRate(progress / 10.0F);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // 设置是否使用震动
        mVibrateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mGestureLockView.setUseVibrate(isChecked);
            }
        });

        // 修改震动时长
        mVibrateDurationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGestureLockView.setVibrateDuration(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // 设置半径改变监听器
        mRadiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGestureLockView.setRadiusRatio(progress / 100.0F);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        /**
         * 修改线的粗细值
         */
        mLineThicknesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGestureLockView.setLineThickness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
