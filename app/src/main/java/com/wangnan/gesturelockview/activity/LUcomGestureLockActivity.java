package com.wangnan.gesturelockview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.wangnan.gesturelockview.R;
import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;
import com.wangnan.library.painter.LUcomPainter;

/**
 * @ClassName: AliPayGestureLockActivity
 * @Description: 手势解锁视图（仿陆金所）
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public class LUcomGestureLockActivity extends AppCompatActivity implements OnGestureLockListener {

    GestureLockView mGestureLockView;

    TextView mCurrentPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucom);
        mCurrentPassword = (TextView) findViewById(R.id.tv_current_passord);
        mGestureLockView = (GestureLockView) findViewById(R.id.glv);
        mGestureLockView.setPainter(new LUcomPainter());
        mGestureLockView.setGestureLockListener(this);
    }


    /**
     * 解锁开始监听方法
     */
    @Override
    public void onStarted() {

    }

    /**
     * 解锁密码改变监听方法
     */
    @Override
    public void onProgress(String progress) {
        mCurrentPassword.setText("当前密码: " + progress);
    }

    /**
     * 解锁完成监听方法
     */
    @Override
    public void onComplete(String result) {
        if (TextUtils.isEmpty(result)) {
            return;
        } else if ("012345678".equals(result)) {
            Toast.makeText(LUcomGestureLockActivity.this, "密码正确O(∩_∩)O~", Toast.LENGTH_SHORT).show();
            mGestureLockView.clearView();
        } else {
            Toast.makeText(LUcomGestureLockActivity.this, "密码错误o(╯□╰)o~", Toast.LENGTH_SHORT).show();
            mGestureLockView.clearView();
        }
    }
}
