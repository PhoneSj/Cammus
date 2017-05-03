package com.cammuse.intelligence.tool.accelerator;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.common.CenterButton;
import com.cammuse.intelligence.common.CircleLayout;
import com.cammuse.intelligence.common.SqureViewGroup;
import com.cammuse.intelligence.device.BLEUtils;
import com.cammuse.intelligence.device.BluetoothLeService;
import com.cammuse.intelligence.entity.BLEData;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.DimensUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;
import com.cammuse.intelligence.utils.ToastUtils;

public class ManualFragment extends Fragment implements OnClickListener {

    protected static final String TAG = "ManualFragment";
    // View
    private TextView textView_titleName_controlAct;
    private RelativeLayout relativeLayout_root;
    private Button button_manualFrag_at;
    private Button button_manualFrag_mt;

    private CenterButton centerButton_manualFrag_currentMode;
    private CircleLayout circleLayout_manualFrag_controlArea;
    private ImageView imageView_manualFrag_sport;
    private ImageView imageView_manualFrag_super;
    private ImageView imageView_manualFrag_original;
    private ImageView imageView_manualFrag_save;
    private ImageView imageView_manualFrag_smart;
    private ImageView imageView_manualFrag_race;
    private SqureViewGroup squreViewGroup_manualFrag_controlArea;
    private ImageView imageView_manualFrag_modeBar;
    private Button button_manualFrag_sub;
    private Button button_manualFrag_add;
    private FrameLayout frameLayout_mainAct;

    private RelativeLayout relativeLayout_manualFrag_triangle;

    private LayoutParams mParams;

    private final int UPDATE_ANIMATION = 0;
    private ObjectAnimator animator;
    private boolean isLoop = false;

    boolean isEnterWaitUI = false;// waitUI界面是否已经开启
    private int current_mode;
    private int current_bar;

    private boolean mConnected = false;
    private LayoutParams params;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            startAnimation();
        }

        ;
    };

    public ManualFragment(TextView textView_titleName_controlAct) {
        this.textView_titleName_controlAct = textView_titleName_controlAct;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // LogUtils.d(TAG, "onCreateView");
        View view = inflater
                .inflate(R.layout.fragment_manual, container, false);
        initView(view);
        initEvent(view);
        initModeIdAndBar();
        initControlAreaBg();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLoop = true;
        //TODO
//        getActivity().registerReceiver(mGattUpdateReceiver,
//                makeGattUpdateIntentFilter());// 注册广播接受者接收蓝牙数据

    }

    @Override
    public void onResume() {
        super.onResume();
        BLEUtils.sendBleDate(getActivity(), ConstantUtils.BLE_CHECK);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // LogUtils.d(TAG, "-----onDestroy----");
        isLoop = false;
        //TODO
//        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    /**
     * 接受蓝牙数据
     */
    public void setData(BLEData mBleData) {
        switch (mBleData.getMode_id() / 6) {
            case 0:
                saveModeIdAndBar(mBleData.getMode_id(),
                        mBleData.getMode_bar());
                updateViewAfterReceiver(mBleData.getMode_id(),
                        mBleData.getMode_bar());
                break;
            case 1:
                // 手/自动挡的切换
                temp = false;
                if (mBleData.isAT()) {
                    button_manualFrag_at
                            .setBackgroundResource(R.mipmap.mode_at_selected);
                    button_manualFrag_mt
                            .setBackgroundResource(R.drawable.selector_radiobutton_manual_mode_mt);
                } else {
                    button_manualFrag_at
                            .setBackgroundResource(R.drawable.selector_radiobutton_manual_mode_at);
                    button_manualFrag_mt
                            .setBackgroundResource(R.mipmap.mode_mt_selected);
                }
                break;
        }
    }

    /**
     * 控制区域的背景动画
     */
    private void initControlAreaBg() {
        startAnimation();
        new Thread() {
            public void run() {
                try {
                    while (isLoop) {
                        Thread.sleep(3000);
                        mHandler.sendEmptyMessage(UPDATE_ANIMATION);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    private void startAnimation() {
        ImageView child = null;
        for (int i = 0; i < relativeLayout_manualFrag_triangle.getChildCount(); i++) {
            child = (ImageView) relativeLayout_manualFrag_triangle
                    .getChildAt(i);

            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(
                    "translationX", 0,
                    (float) (200 * Math.sin(Math.PI / 3 * i)));
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(
                    "translationY", 0,
                    (float) (200 * Math.cos(Math.PI / 3 * i)));
            PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat("alpha",
                    0, 1, 0);
            animator = ObjectAnimator.ofPropertyValuesHolder(child, pvhX, pvhY,
                    pvhA).setDuration(3000);
            animator.start();

        }

    }

    public void initView(View view) {
        button_manualFrag_at = (Button) view
                .findViewById(R.id.button_manualFrag_at);
        button_manualFrag_mt = (Button) view
                .findViewById(R.id.button_manualFrag_mt);
        relativeLayout_root = (RelativeLayout) view
                .findViewById(R.id.relativeLayout_root);
        centerButton_manualFrag_currentMode = (CenterButton) view
                .findViewById(R.id.centerButton_manualFrag_currentMode);
        circleLayout_manualFrag_controlArea = (CircleLayout) view
                .findViewById(R.id.circleLayout_manualFrag_controlArea);
        imageView_manualFrag_sport = (ImageView) view
                .findViewById(R.id.imageView_manualFrag_sport);
        imageView_manualFrag_super = (ImageView) view
                .findViewById(R.id.imageView_manualFrag_super);
        imageView_manualFrag_original = (ImageView) view
                .findViewById(R.id.imageView_manualFrag_original);
        imageView_manualFrag_save = (ImageView) view
                .findViewById(R.id.imageView_manualFrag_save);
        imageView_manualFrag_smart = (ImageView) view
                .findViewById(R.id.imageView_manualFrag_smart);
        imageView_manualFrag_race = (ImageView) view
                .findViewById(R.id.imageView_manualFrag_race);
        squreViewGroup_manualFrag_controlArea = (SqureViewGroup) view
                .findViewById(R.id.squreViewGroup_manualFrag_controlArea);
        imageView_manualFrag_modeBar = (ImageView) view
                .findViewById(R.id.imageView_manualFrag_modeBar);
        button_manualFrag_sub = (Button) view
                .findViewById(R.id.button_manualFrag_sub);
        button_manualFrag_add = (Button) view
                .findViewById(R.id.button_manualFrag_add);
        frameLayout_mainAct = (FrameLayout) view
                .findViewById(R.id.frameLayout_mainAct);

        relativeLayout_manualFrag_triangle = (RelativeLayout) view
                .findViewById(R.id.relativeLayout_manualFrag_triangle);

        centerButton_manualFrag_currentMode.bringToFront();// 将该控件前台显示，防止被其他控件遮住
        setModeTag();
        params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        initEquit(ConstantUtils.EQUIT_ID);
    }

    /**
     * 给每种模式对应的view打上标记
     */
    private void setModeTag() {
        imageView_manualFrag_original.setTag(ConstantUtils.DRIVE_MODE_ORIGINAL);
        imageView_manualFrag_save.setTag(ConstantUtils.DRIVE_MODE_SAVE);
        imageView_manualFrag_sport.setTag(ConstantUtils.DRIVE_MODE_SPORT);
        imageView_manualFrag_super.setTag(ConstantUtils.DRIVE_MODE_SUPER);
        imageView_manualFrag_race.setTag(ConstantUtils.DRIVE_MODE_RACE);
        imageView_manualFrag_smart.setTag(ConstantUtils.DRIVE_MODE_SMART);
    }

    /**
     * 初始化设备功能:根据对应的设备id显示所具有的功能ui
     *
     * @param equit_id
     */
    private void initEquit(int equit_id) {
        // 顺序：原车、省油、运动、超级、赛车、智能
        boolean modeVisiable[] = {true, true, true, true, true, true};// 默认六种模式
        switch (equit_id) {
            case 0x90:
                // 省油、原车、运动模式
                modeVisiable[3] = modeVisiable[4] = modeVisiable[5] = false;
                break;
            case 0x92:
                // 省油、原车、运动、超级运动
                modeVisiable[4] = modeVisiable[5] = false;
                break;
            case 0x94:
                // 省油、原车、运动模式
                modeVisiable[3] = modeVisiable[4] = modeVisiable[5] = false;
                break;
            case 0x96:
                // 原车、运动、超级运动、竞速
                modeVisiable[1] = modeVisiable[5] = false;
                break;
            case 0x98:
                // 省油、原车、运动、超级运动、竞速
                modeVisiable[5] = false;
                break;
            case 0x99:
                // 省油、原车、运动、超级运动、竞速、智能
                break;
            case 0x9a:
                // 省油、原车、智能、运动
                modeVisiable[3] = modeVisiable[4] = false;
                break;
            default:
                if (!getActivity().getIntent().getBooleanExtra(
                        ConstantUtils.INTENT_IS_VISTIOR, false)) {// 判断是否是游客身份
                    //TODO
//                    getActivity().finish();
                    ToastUtils.showShort(getActivity(), "该设备不具备此功能id:" + equit_id);
                    // LogUtils.i(TAG, "没有对应的设备id:"+equit_id);
                }

        }
        setModeVisiable(modeVisiable);
    }

    private void setModeVisiable(boolean[] modeVisiable) {
        imageView_manualFrag_original
                .setVisibility(modeVisiable[ConstantUtils.DRIVE_MODE_ORIGINAL] ? View.VISIBLE
                        : View.INVISIBLE);
        imageView_manualFrag_save
                .setVisibility(modeVisiable[ConstantUtils.DRIVE_MODE_SAVE] ? View.VISIBLE
                        : View.INVISIBLE);
        imageView_manualFrag_sport
                .setVisibility(modeVisiable[ConstantUtils.DRIVE_MODE_SPORT] ? View.VISIBLE
                        : View.INVISIBLE);
        imageView_manualFrag_super
                .setVisibility(modeVisiable[ConstantUtils.DRIVE_MODE_SUPER] ? View.VISIBLE
                        : View.INVISIBLE);
        imageView_manualFrag_race
                .setVisibility(modeVisiable[ConstantUtils.DRIVE_MODE_RACE] ? View.VISIBLE
                        : View.INVISIBLE);
        imageView_manualFrag_smart
                .setVisibility(modeVisiable[ConstantUtils.DRIVE_MODE_SMART] ? View.VISIBLE
                        : View.INVISIBLE);
    }

    private void initEvent(final View view) {

        relativeLayout_root.setOnClickListener(this);
        button_manualFrag_at.setOnClickListener(this);
        button_manualFrag_mt.setOnClickListener(this);
        button_manualFrag_sub.setOnClickListener(this);
        button_manualFrag_add.setOnClickListener(this);
        imageView_manualFrag_original.setOnClickListener(this);
        imageView_manualFrag_save.setOnClickListener(this);
        imageView_manualFrag_sport.setOnClickListener(this);
        imageView_manualFrag_super.setOnClickListener(this);
        imageView_manualFrag_race.setOnClickListener(this);
        imageView_manualFrag_smart.setOnClickListener(this);

        /**
         * 拖拽事件
         */

        centerButton_manualFrag_currentMode
                .setOnTouchListener(new OnTouchListener() {

                    int lastX, lastY;
                    int dx, dy;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                lastX = (int) event.getRawX();
                                lastY = (int) event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                dx = (int) (event.getRawX()) - lastX;
                                dy = (int) (event.getRawY()) - lastY;
                                int left = v.getLeft() + dx;
                                int top = v.getTop() + dy;
                                int right = v.getRight() + dx;
                                int bottom = v.getBottom() + dy;
                                v.layout(left, top, right, bottom);
                                updateViewAfterMove();
                                lastX = (int) event.getRawX();
                                lastY = (int) event.getRawY();
                                break;
                            case MotionEvent.ACTION_UP:
                                updateViewAfterUp();
                                break;

                        }
                        return false;
                    }
                });

    }

    /**
     * 是否在控制区域内
     */
    protected boolean isInControlArea(int left, int top, int right, int bottom) {
        if (left < squreViewGroup_manualFrag_controlArea.getLeft()) {
            return false;
        }
        if (top < squreViewGroup_manualFrag_controlArea.getTop()) {
            return false;
        }
        if (right > squreViewGroup_manualFrag_controlArea.getRight()) {
            return false;
        }
        if (bottom > squreViewGroup_manualFrag_controlArea.getBottom()) {
            return false;
        }
        return true;
    }

    /**
     * 拖拽中
     */
    protected void updateViewAfterMove() {
        int count = circleLayout_manualFrag_controlArea.getChildCount();
        ImageView child = null;
        int distanceX = 0;
        int distanceY = 0;
        int distance = 0;
        int distance_selected = 0;
        for (int i = 0; i < count; i++) {
            child = (ImageView) circleLayout_manualFrag_controlArea
                    .getChildAt(i);
            distanceX = Math.abs(child.getLeft() + child.getWidth() / 2
                    - centerButton_manualFrag_currentMode.getLeft()
                    - centerButton_manualFrag_currentMode.getWidth() / 2);
            distanceY = Math.abs(child.getTop() + child.getHeight() / 2
                    - centerButton_manualFrag_currentMode.getTop()
                    - centerButton_manualFrag_currentMode.getHeight() / 2);
            distance = (int) Math.sqrt(distanceX * distanceX + distanceY
                    * distanceY);
            distance_selected = child.getWidth() / 2
                    + centerButton_manualFrag_currentMode.getWidth() / 2;
            if (distance < distance_selected) {
                centerButton_manualFrag_currentMode
                        .setVisibility(View.INVISIBLE);
                child.setImageResource(ConstantUtils.resModeImgSelectedId[i]);
            }
        }
    }

    /**
     * 拖拽松开
     */
    protected void updateViewAfterUp() {
        // LogUtils.d(TAG, "---updateViewAfterUp---");
        int count = circleLayout_manualFrag_controlArea.getChildCount();
        ImageView child = null;
        int distanceX = 0;
        int distanceY = 0;
        int distance = 0;
        int distance_selected = 0;
        for (int i = 0; i < count; i++) {
            child = (ImageView) circleLayout_manualFrag_controlArea
                    .getChildAt(i);
            distanceX = Math.abs(child.getLeft() + child.getWidth() / 2
                    - centerButton_manualFrag_currentMode.getLeft()
                    - centerButton_manualFrag_currentMode.getWidth() / 2);
            distanceY = Math.abs(child.getTop() + child.getHeight() / 2
                    - centerButton_manualFrag_currentMode.getTop()
                    - centerButton_manualFrag_currentMode.getHeight() / 2);
            distance = (int) Math.sqrt(distanceX * distanceX + distanceY
                    * distanceY);
            distance_selected = child.getWidth() / 2
                    + centerButton_manualFrag_currentMode.getWidth() / 2;
            if (distance < distance_selected) {
                int modeId = (Integer) child.getTag();// 获取标记
                centerButton_manualFrag_currentMode
                        .setBackgroundResource(ConstantUtils.resModeImgSelectedId[modeId]);
                textView_titleName_controlAct
                        .setText(ConstantUtils.manual_mode_name[modeId]);
                setSelected(modeId);
            }

        }
        centerButton_manualFrag_currentMode.setVisibility(View.VISIBLE);
        resetControlAreaView();
    }

    /**
     * 接受到蓝牙数据更新View
     *
     * @param mode_id
     * @param mode_bar
     */
    protected void updateViewAfterReceiver(int mode_id, int mode_bar) {
        // LogUtils.d(TAG, "-----updateViewAfterReceiver-----");
        centerButton_manualFrag_currentMode
                .setBackgroundResource(ConstantUtils.resModeImgSelectedId[mode_id]);
        textView_titleName_controlAct
                .setText(ConstantUtils.manual_mode_name[mode_id]);
        updateModeBarView(mode_id, mode_bar);
    }

    /**
     * 选中对应模式
     */
    private void setSelected(int modeId) {
        // LogUtils.d(TAG, "---setSelected---");
        int modeBar = SPUtils.getInt(getActivity(),
                ConstantUtils.sp_mode_keys[modeId],
                ConstantUtils.DEFAULT_MANAUAL_CURRENT_BAR);
        String data = BLEUtils.constructData(modeId, modeBar);
        BLEUtils.sendBleDate(getActivity(), data);
    }

    /**
     * 更新ModeBar进度
     */
    private void updateModeBarView(int modeId, int modeBar) {
        // LogUtils.d(TAG, "-----updateModeBarView-----");
        switch (modeId) {
            case ConstantUtils.DRIVE_MODE_ORIGINAL:
            case ConstantUtils.DRIVE_MODE_SMART:
                imageView_manualFrag_modeBar.setVisibility(View.INVISIBLE);
                break;
            case ConstantUtils.DRIVE_MODE_SAVE:
            case ConstantUtils.DRIVE_MODE_SPORT:
            case ConstantUtils.DRIVE_MODE_SUPER:
            case ConstantUtils.DRIVE_MODE_RACE:
                imageView_manualFrag_modeBar.setVisibility(View.VISIBLE);
                break;
        }
        imageView_manualFrag_modeBar
                .setImageResource(ConstantUtils.resbarId[modeBar]);
    }

    /**
     * 重置控制区域的view
     */
    private void resetControlAreaView() {
        // LogUtils.d(TAG, "---resetControlAreaView---");
        // 重置边缘的6的imageview
        int count = circleLayout_manualFrag_controlArea.getChildCount();
        ImageView child = null;
        for (int i = 0; i < count; i++) {
            child = (ImageView) circleLayout_manualFrag_controlArea
                    .getChildAt(i);
            int modeId = (Integer) child.getTag();
            child.setImageResource(ConstantUtils.resModeImgNormalId[modeId]);
        }
        params = new LayoutParams(DimensUtils.dipToPx(getActivity(), 100),
                DimensUtils.dipToPx(getActivity(), 100));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        centerButton_manualFrag_currentMode.setLayoutParams(params);
    }

    public void addModeBar() {
        int mode_id = SPUtils.getInt(getActivity(),
                ConstantUtils.SP_KEY_MANAUAL_MODE,
                ConstantUtils.DEFAULT_MANAUAL_CURRENT_MODE);
        int mode_bar = SPUtils.getInt(getActivity(),
                ConstantUtils.SP_KEY_MANAUAL_BAR,
                ConstantUtils.DEFAULT_MANAUAL_CURRENT_BAR);
        if (mode_bar < 10) {
            mode_bar++;
        }
        String data = BLEUtils.constructData(mode_id, mode_bar);
        BLEUtils.sendBleDate(getActivity(), data);
    }

    public void subModeBar() {
        int mode_id = SPUtils.getInt(getActivity(),
                ConstantUtils.SP_KEY_MANAUAL_MODE,
                ConstantUtils.DEFAULT_MANAUAL_CURRENT_MODE);
        int mode_bar = SPUtils.getInt(getActivity(),
                ConstantUtils.SP_KEY_MANAUAL_BAR,
                ConstantUtils.DEFAULT_MANAUAL_CURRENT_BAR);
        if (mode_bar > 1) {
            mode_bar--;
        }
        String data = BLEUtils.constructData(mode_id, mode_bar);
        BLEUtils.sendBleDate(getActivity(), data);
    }

    // 将中间的图片位置更新
    public void updateModeView() {
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        centerButton_manualFrag_currentMode.requestLayout();
        centerButton_manualFrag_currentMode.setLayoutParams(params);
    }

    private boolean temp = false;

    /**
     * 接受蓝牙服务发出的广播信息
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 不处理自动模式的广播
            if (ConstantUtils.ENABLE_AUTOMATIC) {
                return;
            }
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { // 接收
                String data = intent
                        .getStringExtra(BluetoothLeService.EXTRA_DATA);// 获得发送过来的数据
                LogUtils.d(TAG, "接受到蓝牙数据:" + data);
                BLEData mBleData = BLEUtils.parseData(data);
                LogUtils.d(TAG, "mode_id:" + mBleData.getMode_id());
                switch (mBleData.getMode_id() / 6) {
                    case 0:
                        saveModeIdAndBar(mBleData.getMode_id(),
                                mBleData.getMode_bar());
                        updateViewAfterReceiver(mBleData.getMode_id(),
                                mBleData.getMode_bar());
                        break;
                    case 1:
                        // 手/自动挡的切换
                        temp = false;
                        if (mBleData.isAT()) {
                            button_manualFrag_at
                                    .setBackgroundResource(R.mipmap.mode_at_selected);
                            button_manualFrag_mt
                                    .setBackgroundResource(R.drawable.selector_radiobutton_manual_mode_mt);
                        } else {
                            button_manualFrag_at
                                    .setBackgroundResource(R.drawable.selector_radiobutton_manual_mode_at);
                            button_manualFrag_mt
                                    .setBackgroundResource(R.mipmap.mode_mt_selected);
                        }
                        break;
                }
            }
        }
    };

    /**
     * 初始化当前模式、微调
     */
    private void initModeIdAndBar() {
        // LogUtils.d(TAG, "-----initModeIdAndBar-----");
        current_mode = SPUtils.getInt(getActivity(),
                ConstantUtils.SP_KEY_MANAUAL_MODE,
                ConstantUtils.DEFAULT_MANAUAL_CURRENT_MODE);
        current_bar = SPUtils.getInt(getActivity(),
                ConstantUtils.SP_KEY_MANAUAL_BAR,
                ConstantUtils.DEFAULT_MANAUAL_CURRENT_BAR);
        String data = BLEUtils.constructData(current_mode, current_bar);
        // TODO
        // BLEUtils.sendBleDate(getActivity(), data);
    }

    /**
     * 保存当前模式、微调
     *
     * @param mode_id
     * @param mode_bar
     */
    protected void saveModeIdAndBar(int mode_id, int mode_bar) {
        // LogUtils.d(TAG, "---saveState---");
        if (!ConstantUtils.ENABLE_AUTOMATIC) {
            SPUtils.putInt(getActivity(), ConstantUtils.SP_KEY_MANAUAL_MODE,
                    mode_id);
            current_mode = mode_id;
            SPUtils.putInt(getActivity(), ConstantUtils.SP_KEY_MANAUAL_BAR,
                    mode_bar);
            current_bar = mode_bar;
            // 记住对应模式的微调
            SPUtils.putInt(getActivity(), ConstantUtils.sp_mode_keys[mode_id],
                    mode_bar);
        }

    }

//    private static IntentFilter makeGattUpdateIntentFilter() {
//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
//        intentFilter
//                .addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
//        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
//        return intentFilter;
//    }

    @Override
    public void onClick(View v) {
        String data = null;
        switch (v.getId()) {
            case R.id.relativeLayout_root:
                break;
            case R.id.button_manualFrag_add:
                addModeBar();// 加号按钮的处理
                break;

            case R.id.button_manualFrag_sub:
                subModeBar();// 减号按钮的处理
                break;
            case R.id.imageView_manualFrag_original:
                updateSelectedView(0);
                setSelected(0);
                startAnima(imageView_manualFrag_original);
                break;
            case R.id.imageView_manualFrag_save:
                updateSelectedView(1);
                setSelected(1);
                startAnima(imageView_manualFrag_save);
                break;
            case R.id.imageView_manualFrag_sport:
                updateSelectedView(2);
                setSelected(2);
                startAnima(imageView_manualFrag_sport);
                break;
            case R.id.imageView_manualFrag_super:
                updateSelectedView(3);
                setSelected(3);
                startAnima(imageView_manualFrag_super);
                break;
            case R.id.imageView_manualFrag_race:
                updateSelectedView(4);
                setSelected(4);
                startAnima(imageView_manualFrag_race);
                break;
            case R.id.imageView_manualFrag_smart:
                updateSelectedView(5);
                setSelected(5);
                startAnima(imageView_manualFrag_smart);
                break;
            case R.id.button_manualFrag_at:
                data = BLEUtils.constructData(2,
                        Integer.toHexString(ConstantUtils.EQUIT_ID), 6, 1, 0);
                BLEUtils.sendBleDate(getActivity(), data);
                break;
            case R.id.button_manualFrag_mt:
                data = BLEUtils.constructData(2,
                        Integer.toHexString(ConstantUtils.EQUIT_ID), 6, 2, 0);
                BLEUtils.sendBleDate(getActivity(), data);
                break;
        }

    }

    private void updateSelectedView(int modeId) {
        centerButton_manualFrag_currentMode
                .setBackgroundResource(ConstantUtils.resModeImgSelectedId[modeId]);
        textView_titleName_controlAct
                .setText(ConstantUtils.manual_mode_name[modeId]);
    }

    private void startAnima(ImageView target) {

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX",
                1.0f, 1.2f, 1.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY",
                1.0f, 1.2f, 1.0f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target,
                pvhX, pvhY);
        animator.setDuration(2000);
        animator.start();
    }
}
