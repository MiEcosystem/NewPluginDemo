
package com.xiaomi.xmplugindemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.smarthome.common.ui.dialog.MLAlertDialog;
import com.xiaomi.smarthome.device.api.DeviceStat;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONArray;

import java.util.List;

public class TestCaseActivity extends XmPluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_case);

        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.title_bar_title)).setText("插件测试用例");

        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        findViewById(R.id.intent_parcelable).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // SerializableData sData = new SerializableData();
                // sData.id = 5;
                // intent.putExtra("sData", sData);
                ParcelData pData = new ParcelData();
                pData.mData = 2;
                intent.putExtra("pData", pData);
                startActivity(intent, FragmentActivity.class.getName());
            }
        });
    }

}
