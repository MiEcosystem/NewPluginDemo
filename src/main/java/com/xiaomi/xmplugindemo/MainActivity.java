
package com.xiaomi.xmplugindemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.common.ui.dialog.MenuDialog;
import com.xiaomi.smarthome.common.ui.widget.SwitchButton;
import com.xiaomi.smarthome.device.api.BaseDevice;
import com.xiaomi.smarthome.device.api.BaseDevice.StateChangedListener;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends XmPluginBaseActivity implements StateChangedListener {
    static final int REQUEST_MENUS = 1;
    static final int MSG_SUB_PROPERTIES = 1;
    DemoDevice mDevice;

    TextView mInfoView;
    boolean mIsResume;
    LayoutInflater mLayoutInflater;
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_SUB_PROPERTIES:
                    if (mIsResume) {
                        mDevice.subscribeProperty(DemoDevice.PROPERTIES, null);
                        sendEmptyMessageDelayed(MSG_SUB_PROPERTIES, 3 * 60000);
                    }
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView version = (TextView) findViewById(R.id.version);
        version.setText("version:" + mPluginPackage.packageVersion);
        TextView subTitleView = ((TextView) findViewById(R.id.sub_title_bar_title));
        subTitleView.setText("子设备");
        subTitleView.setVisibility(View.VISIBLE);

        mInfoView = (TextView) findViewById(R.id.info);

        // 初始化device
        mDevice = DemoDevice.getDevice(mDeviceStat);

        mLayoutInflater = LayoutInflater.from(activity());
        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        ((TextView) findViewById(R.id.title_bar_title)).setText(mDevice.getName());
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
//                moreMenuDefault();
//                moreMenuUseDefine();
                moreMenuNew();
            }
        });

        // 打开分享
        View shareView = findViewById(R.id.title_bar_share);
        if (mDevice.isBinded()) {
            shareView.setVisibility(View.VISIBLE);
            shareView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHostActivity.openShareActivity();
                }
            });
        } else {
            shareView.setVisibility(View.GONE);
        }

        findViewById(R.id.control).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // ParcelData parcelData = new ParcelData();
                // parcelData.mData = 10;
                // intent.putExtra("parcelData", parcelData);
                startActivity(intent, ControlActivity.class.getName());
                // if(XmPluginHostApi.instance().getApiLevel()>=3)
                // mHostActivity.overridePendingTransition(IXmPluginHostActivity.ANIM_SLIDE_IN_TOP,
                // null);
            }
        });

        findViewById(R.id.cloud_debug).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHostActivity.loadWebView("http://home.mi.com/demo/cloud.html", null);
            }
        });

        findViewById(R.id.create_product).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHostActivity.loadWebView("http://home.mi.com/demo/product.html", null);
            }
        });

        mHostActivity.enableVerifyPincode();
        mHostActivity.enableAd();
    }

    void openMoreActivity(){
        ArrayList<IXmPluginHostActivity.MenuItemBase> menus = new
                ArrayList<IXmPluginHostActivity.MenuItemBase>();

        //
        IXmPluginHostActivity.StringMenuItem stringMenuItem = new
                IXmPluginHostActivity.StringMenuItem();
        stringMenuItem.name = "test string menu";
        menus.add(stringMenuItem);

        //
        IXmPluginHostActivity.IntentMenuItem intentMenuItem = new
                IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "test intent menu";
        intentMenuItem.intent =
                mHostActivity.getActivityIntent(null,
                        ApiDemosActivity.class.getName());
        menus.add(intentMenuItem);

        //
        //
        IXmPluginHostActivity.InfoMenuItem infoMenuItem = new
                IXmPluginHostActivity.InfoMenuItem();
        infoMenuItem.name = "test info menu";
        menus.add(infoMenuItem);

        //
        IXmPluginHostActivity.SlideBtnMenuItem slideBtnMenuItem = new
                IXmPluginHostActivity.SlideBtnMenuItem();
        slideBtnMenuItem.name = "test slide menu";
        slideBtnMenuItem.isOn = mDevice.getRgb() > 0;
        slideBtnMenuItem.onMethod = "set_rgb";
        JSONArray onparams = new JSONArray();
        onparams.put(0xffffff);
        slideBtnMenuItem.onParams =
                onparams.toString();
        slideBtnMenuItem.offMethod = "set_rgb";
        JSONArray offparams = new JSONArray();
        offparams.put(0);
        slideBtnMenuItem.offParams =
                offparams.toString();
        menus.add(slideBtnMenuItem);

        mHostActivity.openMoreMenu(menus, true,
                REQUEST_MENUS);
    }
    void moreMenuNew(){
        ArrayList<IXmPluginHostActivity.MenuItemBase> menus = new
                ArrayList<>();

        ////插件自定义菜单，可以在public void onActivityResult(int requestCode, int resultCode, Intent data) 中接收用户点击的菜单项，String result = data.getStringExtra("menu");
        IXmPluginHostActivity.StringMenuItem stringMenuItem = new
                IXmPluginHostActivity.StringMenuItem();
        stringMenuItem.name = "test string menu";
        menus.add(stringMenuItem);

        //带开关按钮的菜单，可以自动调用设备rpc
        IXmPluginHostActivity.SlideBtnMenuItem slideBtnMenuItem = new
                IXmPluginHostActivity.SlideBtnMenuItem();
        slideBtnMenuItem.name = "开关灯";
        slideBtnMenuItem.isOn = mDevice.getRgb() > 0;
        slideBtnMenuItem.onMethod = "set_rgb";
        JSONArray onparams = new JSONArray();
        onparams.put(0xffffff);
        slideBtnMenuItem.onParams = onparams.toString();
        slideBtnMenuItem.offMethod = "set_rgb";
        JSONArray offparams = new JSONArray();
        offparams.put(0);
        slideBtnMenuItem.offParams =
                offparams.toString();
        menus.add(slideBtnMenuItem);

        //跳转到插件下一个activity的菜单
        IXmPluginHostActivity.IntentMenuItem intentMenuItem = null;
        intentMenuItem = new
                IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "透明titlebar";
        intentMenuItem.intent =
                mHostActivity.getActivityIntent(null,
                        TransparentActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new
                IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "Dialog";
        intentMenuItem.intent =
                mHostActivity.getActivityIntent(null,
                        DiaglogActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new
                IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "分享";
        intentMenuItem.intent =
                mHostActivity.getActivityIntent(null,
                        ShareActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new
                IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "ApiDemo";
        intentMenuItem.intent =
                mHostActivity.getActivityIntent(null,
                        ApiDemosActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new
                IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "测试用例";
        intentMenuItem.intent =
                mHostActivity.getActivityIntent(null,
                        TestCaseActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new
                IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "Api自动测试";
        intentMenuItem.intent =
                mHostActivity.getActivityIntent(null,
                        ApiTestActivity.class.getName());
        menus.add(intentMenuItem);



        Intent intent = new Intent();
        intent.putExtra("security_setting_enable",true);
        mHostActivity.openMoreMenu2(menus, true, REQUEST_MENUS, intent);
    }
    void moreMenuDefault() {
        // 下拉菜单
        MenuDialog menuDialog = new MenuDialog(activity());
        menuDialog.setBackGroundColor(0xff16ccec);
        menuDialog.setItems(new String[] {
                "通用设置", "透明titlebar", "Dialog", "分享", "ApiDemo", "测试用例","Api自动测试"
        }, new MenuDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                    {
                        openMoreActivity();
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
                    case 6:
                        startActivity(null, ApiTestActivity.class.getName());
                        break;
                    default:
                        break;
                }
            }
        });
        menuDialog.show();
    }

    void moreMenuUseDefine() {
        // 下拉菜单
        final MenuDialog menuDialog = new MenuDialog(activity());
        menuDialog.setMenuAdapter(new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                switch (position) {
                    case 0:
                    {
                        if (convertView == null) {
                            convertView = mLayoutInflater.inflate(R.layout.menu_dialog_item, null);
                        }
                        TextView textView = (TextView) convertView.findViewById(R.id.text1);
                        textView.setText("通用设置");
                        convertView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openMoreActivity();
                                menuDialog.dismiss();
                            }
                        });
                        break;
                    }
                    case 1:
                    {
                        if (convertView == null) {
                            convertView = mLayoutInflater.inflate(R.layout.menu_dialog_slidebtn_item, null);
                        }
                        TextView textView = (TextView) convertView.findViewById(R.id.text1);
                        textView.setText("开关");
                        SwitchButton switchButton = (SwitchButton)convertView.findViewById(R.id.slide_btn);
                        if(mDevice.isReadOnlyShared()){
                            switchButton.setEnabled(false);
                        }else {
                            switchButton.setEnabled(true);
                            switchButton.setOnCheckedChangeListener(null);
                            switchButton.setChecked(mDevice.getRgb() != 0);
                            switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    JSONArray params = new JSONArray();
                                    if (isChecked) {
                                        params.put(0xffffff);
                                    } else {
                                        params.put(0);
                                    }
                                    mDevice.callMethod("set_rgb", params, null, null);
                                }
                            });
                        }
                        break;
                    }
                    case 2: {
                        if (convertView == null) {
                            convertView = mLayoutInflater.inflate(R.layout.menu_dialog_item, null);
                        }
                        TextView textView = (TextView) convertView.findViewById(R.id.text1);
                        textView.setText("透明titlebar");
                        convertView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(null, TransparentActivity.class.getName());
                                menuDialog.dismiss();
                            }
                        });
                        break;
                    }
                    case 3: {
                        if (convertView == null) {
                            convertView = mLayoutInflater.inflate(R.layout.menu_dialog_item, null);
                        }
                        TextView textView = (TextView) convertView.findViewById(R.id.text1);
                        textView.setText("Dialog");
                        convertView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(null, DiaglogActivity.class.getName());
                                menuDialog.dismiss();
                            }
                        });
                        break;
                    }
                    case 4: {
                        if (convertView == null) {
                            convertView = mLayoutInflater.inflate(R.layout.menu_dialog_item, null);
                        }
                        TextView textView = (TextView) convertView.findViewById(R.id.text1);
                        textView.setText("分享");
                        convertView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(null, ShareActivity.class.getName());
                                menuDialog.dismiss();
                            }
                        });
                        break;
                    }
                    case 5: {
                        if (convertView == null) {
                            convertView = mLayoutInflater.inflate(R.layout.menu_dialog_item, null);
                        }
                        TextView textView = (TextView) convertView.findViewById(R.id.text1);
                        textView.setText("API Demo");
                        convertView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(null, ApiDemosActivity.class.getName());
                                menuDialog.dismiss();
                            }
                        });
                        break;
                    }
                    case 6: {
                        if (convertView == null) {
                            convertView = mLayoutInflater.inflate(R.layout.menu_dialog_item, null);
                        }
                        TextView textView = (TextView) convertView.findViewById(R.id.text1);
                        textView.setText("测试用例");
                        convertView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(null, TestCaseActivity.class.getName());
                                menuDialog.dismiss();
                            }
                        });
                        break;
                    }
                    case 7: {
                        if (convertView == null) {
                            convertView = mLayoutInflater.inflate(R.layout.menu_dialog_item, null);
                        }
                        TextView textView = (TextView) convertView.findViewById(R.id.text1);
                        textView.setText("自动测试");
                        convertView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(null, ApiTestActivity.class.getName());
                                menuDialog.dismiss();
                            }
                        });
                        break;
                    }
                    default:
                        break;
                }
                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public int getCount() {
                return 8;
            }

            @Override
            public int getViewTypeCount() {
                return 8;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        });
        menuDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 自定义菜单返回
            if (requestCode == REQUEST_MENUS && data != null) {
                String selectMenu = data.getStringExtra("menu");
                if (TextUtils.isEmpty(selectMenu)) {
                    return;
                }
                if (selectMenu.equals("test string menu")) {
                    // 分享微博，微信，米聊
                    Toast.makeText(activity(), selectMenu, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsResume = true;
        mHandler.sendEmptyMessage(MSG_SUB_PROPERTIES);
        mDevice.addStateChangedListener(this);
        mDevice.updateDeviceStatus();
        mDevice.updateProperty(DemoDevice.PROPERTIES);
        ((TextView) findViewById(R.id.title_bar_title)).setText(mDevice.getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsResume = false;
        mDevice.removeStateChangedListener(this);
        mHandler.removeMessages(MSG_SUB_PROPERTIES);
    }

    @Override
    public void onStateChanged(BaseDevice device) {
        if (!mIsResume)
            return;
        String info = "温度:" + mDevice.getTemperature() + " 湿度:" + mDevice.getHumidity();
        mInfoView.setText(info);
    }
}
