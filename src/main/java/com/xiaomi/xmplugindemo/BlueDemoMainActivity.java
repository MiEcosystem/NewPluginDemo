
package com.xiaomi.xmplugindemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.smarthome.common.ui.dialog.MenuDialog;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

public class BlueDemoMainActivity extends XmPluginBaseActivity {

    boolean mIsResume;
    LayoutInflater mLayoutInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_main);

        mLayoutInflater = LayoutInflater.from(activity());
        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        ((TextView) findViewById(R.id.title_bar_title)).setText(mDeviceStat.name);
        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.title_bar_redpoint).setVisibility(View.VISIBLE);

        findViewById(R.id.title_bar_more).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // // 新版更多菜单规则，第一页插件自定义，然后跳转到公共页面
                // Intent intent = new Intent();
                // startActivity(intent, MoreActivity.class.getName());
                moreMenuDefault();
            }
        });

        // 打开分享
        View shareView = findViewById(R.id.title_bar_share);
        shareView.setVisibility(View.VISIBLE);
        shareView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openShareActivity();
            }
        });
    }

    void moreMenuDefault() {
        // 下拉菜单
        MenuDialog menuDialog = new MenuDialog(activity());
        menuDialog.setBackGroundColor(0xff16ccec);
        menuDialog.setItems(new String[] {
                "通用设置", "透明titlebar", "Dialog", "分享", "ApiDemo", "测试用例"
        }, new MenuDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                    {
                        // ArrayList<MenuItemBase> menus = new
                        // ArrayList<IXmPluginHostActivity.MenuItemBase>();
                        //
                        // //
                        // StringMenuItem stringMenuItem = new
                        // StringMenuItem();
                        // stringMenuItem.name = "test string menu";
                        // menus.add(stringMenuItem);
                        //
                        // //
                        // IntentMenuItem intentMenuItem = new
                        // IntentMenuItem();
                        // intentMenuItem.name = "test intent menu";
                        // intentMenuItem.intent =
                        // mHostActivity.getActivityIntent(null,
                        // ApiDemosActivity.class.getName());
                        // menus.add(intentMenuItem);
                        //
                        // //
                        // SlideBtnMenuItem slideBtnMenuItem = new
                        // SlideBtnMenuItem();
                        // slideBtnMenuItem.name = "test slide menu";
                        // slideBtnMenuItem.isOn = mDevice.getRgb() > 0;
                        // slideBtnMenuItem.onMethod = "set_rgb";
                        // JSONArray onparams = new JSONArray();
                        // onparams.put(0xffffff);
                        // slideBtnMenuItem.onParams =
                        // onparams.toString();
                        // slideBtnMenuItem.offMethod = "set_rgb";
                        // JSONArray offparams = new JSONArray();
                        // offparams.put(0);
                        // slideBtnMenuItem.offParams =
                        // offparams.toString();
                        // menus.add(slideBtnMenuItem);

                        mHostActivity.openMoreMenu(null, true,
                                -1);
                    }
                        break;
                    case 1:
                        startActivity(null, TransparentActivity.class.getName());
                        break;
                    case 2:
                        startActivity(null, DiaglogActivity.class.getName());
                        break;
                    case 3:
                        startActivity(null, ShareActivity.class.getName());
                        break;
                    case 4:
                        startActivity(null, ApiDemosActivity.class.getName());
                        break;
                    case 5:
                        startActivity(null, TestCaseActivity.class.getName());
                        break;
                    default:
                        break;
                }
            }
        });
        menuDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsResume = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsResume = false;
    }

}
