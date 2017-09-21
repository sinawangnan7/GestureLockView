package com.wangnan.gesturelockview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wangnan.gesturelockview.activity.AliPayGestureLockActivity;
import com.wangnan.gesturelockview.activity.ImageGestureLockActivity;
import com.wangnan.gesturelockview.activity.JDFinanceGestureLockActivity;
import com.wangnan.gesturelockview.activity.LUcomGestureLockActivity;
import com.wangnan.gesturelockview.activity.StandardGestureLockActivity;
import com.wangnan.gesturelockview.activity.System360GestureLockActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void standard(View view){
        startActivity(new Intent(this, StandardGestureLockActivity.class));
    }

    public void image(View view){
        startActivity(new Intent(this, ImageGestureLockActivity.class));
    }

    public void system360(View view){
        startActivity(new Intent(this, System360GestureLockActivity.class));
    }

    public void jdFinance(View view){
        startActivity(new Intent(this, JDFinanceGestureLockActivity.class));
    }

    public void alipay(View view){
        startActivity(new Intent(this, AliPayGestureLockActivity.class));
    }

    public void lucom(View view){
        startActivity(new Intent(this, LUcomGestureLockActivity.class));
    }
}
