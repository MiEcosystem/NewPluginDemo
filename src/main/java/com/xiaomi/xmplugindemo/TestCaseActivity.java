
package com.xiaomi.xmplugindemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.common.ui.widget.XmRadioGroup;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.SceneInfo;
import com.xiaomi.smarthome.device.api.UserInfo;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class TestCaseActivity extends XmPluginBaseActivity {

    LinearLayout mListContainer;
    LayoutInflater mInflater;

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

        mListContainer = (LinearLayout) findViewById(R.id.container);
        mInflater = LayoutInflater.from(this);

        addTestCase("intent parcelable", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ParcelData pData = new ParcelData();
                pData.mData = 2;
                intent.putExtra("pData", pData);
                startActivity(intent, FragmentActivity.class.getName());
            }
        });

        addTestCase("openShareActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openShareActivity();
            }
        });

        addTestCase("goUpdateActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.goUpdateActivity();
            }
        });

//        addTestCase("startLoadScene", new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHostActivity.startLoadScene();
//            }
//        });

        addTestCase("startCreateSceneByDid", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startCreateSceneByDid(mDeviceStat.did);
            }
        });

        addTestCase("startEditScene", new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SceneInfo> sceneInfos = mHostActivity.getSceneByDid(mDeviceStat.did);
                if (sceneInfos != null && sceneInfos.size() > 0)
                    mHostActivity.startEditScene(sceneInfos.get(0).mSceneId);
            }
        });

        addTestCase("startSetTimerList", new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray onParams = new JSONArray();
                onParams.put(0xffffff);
                JSONArray offParams = new JSONArray();
                offParams.put(0);
                mHostActivity.startSetTimerList(mDeviceStat.did, "set_rgb", onParams.toString(),
                        "set_rgb", offParams.toString(), mDeviceStat.did, "开关",
                        "开关灯");
            }
        });

        addTestCase("openShareMediaActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getWindow().getDecorView();
                if (view == null) {
                    return;
                }
                Bitmap bitmap = null;//= view.getDrawingCache();
                if (bitmap == null) {
                    if (view.getWidth() > 0 && view.getHeight() > 0) {
                        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
                        Canvas canvas = new Canvas(bitmap);
                        view.draw(canvas);
                    }
                }

                if (bitmap == null) {
                    return;
                }
                File dir = getExternalCacheDir();
                if (dir == null) {
                    Toast.makeText(activity(), "没有存储空间", Toast.LENGTH_SHORT).show();
                    return;
                }
                File shareFile = new File(dir, "share.jpg");
                boolean savesuccess = false;
                try {
                    OutputStream os = new FileOutputStream(shareFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
                    os.close();
                    savesuccess = true;
                    bitmap.recycle();
                    bitmap = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (savesuccess) {
                    mHostActivity.openShareMediaActivity("智能家庭",
                            "测试分享",
                            shareFile.getAbsolutePath()
                    );
                }
            }
        });


        addTestCase("startEditCustomScene", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startEditCustomScene();
            }
        });

        addTestCase("goUpdateActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.goUpdateActivity(mDeviceStat.did);
            }
        });

        addTestCase("startEditCustomScene", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startEditCustomScene();
            }
        });

    }

    void addTestCase(String name, OnClickListener listener) {
        View view = mInflater.inflate(R.layout.list_item, null);
        ((TextView) view.findViewById(R.id.name)).setText(name);
        view.setOnClickListener(listener);
        LinearLayout.LayoutParams lp = new XmRadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mListContainer.addView(view, lp);
    }

}
