package com.cammuse.intelligence.tool.accelerator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.common.MyProgressBar;
import com.cammuse.intelligence.device.BLEUtils;
import com.cammuse.intelligence.device.BluetoothLeService;
import com.cammuse.intelligence.entity.BLEData;
import com.cammuse.intelligence.entity.UserLevel;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;

public class AutomaticFragment extends Fragment implements OnClickListener {

    private static final String TAG = "AutomaticFragment";

    private TextView textView_titleName_controlAct;
    private RelativeLayout relativeLayout_automaticFrag_root;
    private MyProgressBar myProgressBar_automaticFrag_levelProgress;
    private LinearLayout linearLayout_AutomaticFrag_smallLevel;

    private ImageView imageView_automaticFrag_dial;
    private ImageView imageView_automaticFrag_pointer;
    private RelativeLayout relativeLayout_automaticFrag_gas;

    // 机器人的状态
    private static final int ROBOT_STATE_FIRST_ENTER = 0;// 第一次进入
    private static final int ROBOT_STATE_ENTER = ROBOT_STATE_FIRST_ENTER + 1;// 从别的模式切换到该模式
    private static final int ROBOT_STATE_DISAPPPEAR = ROBOT_STATE_ENTER + 1;// 机器人消失
    private static final int ROBOT_STATE_UPDATE_SMALL_LEVEL = ROBOT_STATE_DISAPPPEAR + 1;// 大格改变
    private static final int ROBOT_STATE_UPDATE_LARGE_LEVEL = ROBOT_STATE_UPDATE_SMALL_LEVEL + 1;// 小格改变
    private static final int ROBOT_STATE_NO_GAS = ROBOT_STATE_UPDATE_LARGE_LEVEL + 1;// 没有油门数据
    private static final int ROBOT_STATE_HAS_GAS = ROBOT_STATE_NO_GAS + 1;
    private static final int ROBOT_STATE_WAIT = ROBOT_STATE_HAS_GAS + 1;
    private static int robot_current_state = ROBOT_STATE_FIRST_ENTER;// 机器人的当前状态，默认为第一次进入
    // 机器人说话的状态：正在说/已经说完了
    private static final int ROBOT_SAY = 100;
    private static final int ROBOT_SAY_OVER = ROBOT_SAY + 1;

    private enum RobotSayState {
        ROBOT_SAY, ROBOT_SAY_OVER
    }

    ;

    private static RobotSayState robot_current_say_state = RobotSayState.ROBOT_SAY_OVER;// 默认为已经说完

    private static boolean gas_state = false;// 记录当前油门状态：有/否

    private int bg_resId[] = {R.mipmap.automatic_bg0,
            R.mipmap.automatic_bg1, R.mipmap.automatic_bg2,
            R.mipmap.automatic_bg3, R.mipmap.automatic_bg4,
            R.mipmap.automatic_bg5, R.mipmap.automatic_bg6,
            R.mipmap.automatic_bg7};

//	private int robot_imgs[] = { R.mipmap.robot_sayhello,
//			R.mipmap.robot_thinking, R.mipmap.robot_tell };

    private int last_level;

    private int history_gas = 1;
    private boolean isLoop;

    private String content[];

    public AutomaticFragment(TextView textView_titleName_controlAct) {
        this.textView_titleName_controlAct = textView_titleName_controlAct;
    }

    /**
     * 读取sp中的level
     */
    protected UserLevel getLevelInfo() {
        int level = SPUtils.getInt(getActivity(),
                ConstantUtils.SP_KEY_AUTOMATIC_CURRENT_LEVEL,
                ConstantUtils.DEFAULT_AUTOMATIC_CURRENT_LEVEL);
        return UserLevel.level2UserLevel(level);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_automatic, container,
                false);
        isLoop = true;
        // 是否是第一次使用
        boolean isFirstEnterAutomatic = SPUtils.getBoolean(getActivity(),
                ConstantUtils.SP_KEY_IS_FIRST_USE_AUTOMATIC,
                ConstantUtils.DEFAULT_IS_FIRST_ENTER_AUTOMATIC_MODE);
        if (isFirstEnterAutomatic) {
            robot_current_state = ROBOT_STATE_FIRST_ENTER;
        } else {
            robot_current_state = ROBOT_STATE_ENTER;
            SPUtils.putBoolean(getActivity(),
                    ConstantUtils.SP_KEY_IS_FIRST_USE_AUTOMATIC, false);
        }
        initView(view);
        initData();
//		initPlaySound(R.raw.yinxiao);
        initVibrator();

        return view;
    }

    /**
     * 切换机器人说话的状态
     */
    protected void setRobotSayState(RobotSayState state) {
        robot_current_say_state = state;
    }

    /**
     * 切换机器人的当前状态到Wait，只有当前状态为wait时才可切换
     */
    protected void setStateToWait() {
        LogUtils.d(TAG, "----setStateToWait-----");
        robot_current_state = ROBOT_STATE_WAIT;
    }

    /**
     * 切换机器人的当前状态:只有当状态恢复到wait时才可以切换
     */
    protected void setState(int state) {
        LogUtils.d(TAG, "---setState---");
        if (robot_current_state == ROBOT_STATE_WAIT) {
            robot_current_state = state;
        } else {
            LogUtils.d(TAG, "robot_current_state:" + robot_current_state);
            LogUtils.d(TAG, "机器人状态没有切换到Wait状态");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "采样间隔：" + ConstantUtils.gas_sample_interval);
        LogUtils.d(TAG, "采样梯度：" + ConstantUtils.gas_gradient[0]);
        LogUtils.d(TAG, "采样基准值：" + ConstantUtils.gas_benchmark[0]);
        LogUtils.d(TAG, "上限：" + ConstantUtils.level_threshold[0][0]);
        LogUtils.d(TAG, "下限：" + ConstantUtils.level_threshold[0][1]);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoop = false;
//		mMediaPlayer.release();
//		mMediaPlayer = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册广播接受者接收蓝牙数据
        //TODO
//        getActivity().registerReceiver(mGattUpdateReceiver,
//                makeGattUpdateIntentFilter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO
//        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    /**
     * 接受蓝牙数据
     */
    public void setData() {

    }

    private void initView(View view) {
        relativeLayout_automaticFrag_root = (RelativeLayout) view
                .findViewById(R.id.relativeLayout_automaticFrag_root);
        myProgressBar_automaticFrag_levelProgress = (MyProgressBar) view
                .findViewById(R.id.myProgressBar_automaticFrag_levelProgress);
        linearLayout_AutomaticFrag_smallLevel = (LinearLayout) view
                .findViewById(R.id.linearLayout_AutomaticFrag_smallLevel);
        imageView_automaticFrag_dial = (ImageView) view
                .findViewById(R.id.imageView_automaticFrag_dial);
        imageView_automaticFrag_pointer = (ImageView) view
                .findViewById(R.id.imageView_automaticFrag_pointer);
        relativeLayout_automaticFrag_gas = (RelativeLayout) view
                .findViewById(R.id.relativeLayout_automaticFrag_gas);
    }

    private void initData() {
        last_level = SPUtils.getInt(getActivity(),
                ConstantUtils.SP_KEY_AUTOMATIC_CURRENT_LEVEL,
                ConstantUtils.DEFAULT_AUTOMATIC_CURRENT_LEVEL);
        int level = SPUtils.getInt(getActivity(),
                ConstantUtils.SP_KEY_AUTOMATIC_CURRENT_LEVEL,
                ConstantUtils.DEFAULT_AUTOMATIC_CURRENT_LEVEL);
        BLEData mBleData = BLEUtils.level2BleData(level);
        String data = BLEUtils.constructData(mBleData);
        // TODO
        // BLEUtils.sendBleDate(getActivity(), data);
        // updateView(level);
        relativeLayout_automaticFrag_root
                .setBackgroundResource(bg_resId[UserLevel
                        .level2UserLevel(level).getLevel_large()]);
        ImageView child = null;
        for (int i = 0; i < linearLayout_AutomaticFrag_smallLevel
                .getChildCount(); i++) {
            child = (ImageView) linearLayout_AutomaticFrag_smallLevel
                    .getChildAt(i);
            if (i <= UserLevel.level2UserLevel(level).getLevel_small()) {
                child.setImageResource(R.mipmap.star_on);
            } else {
                child.setImageResource(R.mipmap.star_off);
            }
        }

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 更新ui
     *
     * @param
     */
    protected void updateView(int level) {
        LogUtils.d(TAG, "---last_level---" + last_level);
        LogUtils.d(TAG, "---level---" + level);
        textView_titleName_controlAct
                .setText(ConstantUtils.automatic_level_name[UserLevel
                        .level2UserLevel(level).getLevel_large()]);
        startLevelAnimation(last_level, level);
        startVibratorAndSound(last_level, level);
        last_level = level;
    }

    protected void updateProgress(int points) {
        int max = ConstantUtils.level_threshold[(last_level - 1) / 10][ConstantUtils.UP_THRESHOLD]
                + ConstantUtils.level_threshold[(last_level - 1) / 10][ConstantUtils.DOWM_THRESHOLD];
        int current = ConstantUtils.level_threshold[(last_level - 1) / 10][ConstantUtils.DOWM_THRESHOLD]
                + points;
        LogUtils.d(TAG, "max:" + max);
        LogUtils.d(TAG, "current:" + current);
        myProgressBar_automaticFrag_levelProgress.setMax(max);
        myProgressBar_automaticFrag_levelProgress.setProgress(current);

    }

    /**
     * 音效、震动
     *
     * @param last_level
     * @param current_level
     */
    private void startVibratorAndSound(int last_level, int current_level) {
        if (UserLevel.level2UserLevel(last_level).getLevel_large() != UserLevel
                .level2UserLevel(current_level).getLevel_large()) {
            // 大格发生变化
            startVibrate();
//			initPlaySound(R.raw.yinxiao2);
//			startPlaySound();
        } else if (UserLevel.level2UserLevel(last_level).getLevel_small() != UserLevel
                .level2UserLevel(current_level).getLevel_small()) {
            // 小格发生变化
            startVibrate();
//			initPlaySound(R.raw.yinxiao1);
//			startPlaySound();

        }
    }

    /**
     * 级别发生变化时的动画
     *
     * @param last_level
     * @param current_level
     */
    private void startLevelAnimation(int last_level, int current_level) {

        int last_small_level = UserLevel.level2UserLevel(last_level)
                .getLevel_small();
        int last_large_level = UserLevel.level2UserLevel(last_level)
                .getLevel_large();
        int current_small_level = UserLevel.level2UserLevel(current_level)
                .getLevel_small();
        int current_large_level = UserLevel.level2UserLevel(current_level)
                .getLevel_large();

        int count = linearLayout_AutomaticFrag_smallLevel.getChildCount();
        ImageView child = null;
        for (int i = 0; i < count; i++) {
            child = (ImageView) linearLayout_AutomaticFrag_smallLevel
                    .getChildAt(i);
            if (i <= current_small_level) {
                child.setImageResource(R.mipmap.star_on);
            } else {
                child.setImageResource(R.mipmap.star_off);
            }
        }

        if (last_large_level != current_large_level) {
            // 大格发生变化
            relativeLayout_automaticFrag_root
                    .setBackgroundResource(bg_resId[current_large_level]);
            setDialBackground(current_large_level);
            startLargeLevelAnimation(last_large_level, current_large_level);
            setState(ROBOT_STATE_UPDATE_LARGE_LEVEL);
        } else if (last_level != current_level) {
            // 小格发生变化
            startSamllLevleAnimation(last_level, current_level);
            setState(ROBOT_STATE_UPDATE_SMALL_LEVEL);
        }

    }

    /**
     * 设置油门仪表的背景
     *
     * @param current_large_level
     */
    private void setDialBackground(int current_large_level) {
        int color = 0;
        switch (current_large_level) {
            case 0:
                color = getResources().getColor(R.color.color0);
                break;
            case 1:
                color = getResources().getColor(R.color.color1);
                break;
            case 2:
                color = getResources().getColor(R.color.color2);
                break;
            case 3:
                color = getResources().getColor(R.color.color3);
                break;
            case 4:
                color = getResources().getColor(R.color.color4);
                break;
            case 5:
                color = getResources().getColor(R.color.color5);
                break;
            case 6:
                color = getResources().getColor(R.color.color6);
                break;
            case 7:
                color = getResources().getColor(R.color.color7);
                break;

            default:
                color = getResources().getColor(R.color.color0);
                break;
        }
        relativeLayout_automaticFrag_gas.setBackgroundColor(color);

    }

    private Animation anim;

    /**
     * 大格变化的动画
     */
    private void startLargeLevelAnimation(final int last_large_level,
                                          final int current_large_level) {
        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_large);
        for (int i = 0; i < linearLayout_AutomaticFrag_smallLevel
                .getChildCount(); i++) {
            imageView = (ImageView) linearLayout_AutomaticFrag_smallLevel
                    .getChildAt(i);
            imageView.startAnimation(anim);
        }

    }

    private ImageView imageView;
    private Animation anim0;
    private Animation anim1;
    private Animation anim2;
    private boolean isUpLevel;

    private void startSamllLevleAnimation(int last_level, int current_level) {
        Log.i(TAG, "startSamllLevleAnimation");
        if (last_level > current_level) {
            imageView = (ImageView) linearLayout_AutomaticFrag_smallLevel
                    .getChildAt(UserLevel.level2UserLevel(last_level)
                            .getLevel_small());
            isUpLevel = false;
        } else {
            imageView = (ImageView) linearLayout_AutomaticFrag_smallLevel
                    .getChildAt(UserLevel.level2UserLevel(current_level)
                            .getLevel_small());
            isUpLevel = true;
        }
        anim0 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_first);
        anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_second);
        anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_third);
        anim0.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (isUpLevel) {
                    imageView.setImageResource(R.mipmap.star_off);
                } else {
                    imageView.setImageResource(R.mipmap.star_on);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(anim1);
            }
        });
        anim1.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(anim2);
            }
        });
        anim2.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (isUpLevel) {
                    imageView.setImageResource(R.mipmap.star_on);
                } else {
                    imageView.setImageResource(R.mipmap.star_off);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        imageView.startAnimation(anim0);
    }

    /**
     * 接受蓝牙服务发出的广播信息
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 不处理手动操作的广播
            if (!ConstantUtils.ENABLE_AUTOMATIC) {
                return;
            }
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //
                String data = intent
                        .getStringExtra(BluetoothLeService.EXTRA_DATA);// 获得发送过来的数据
                BLEData mBleData = BLEUtils.parseData(data);
                if (mBleData.getMode_id() != 6) {
                    // updateGasView(mBleData);
                    UpdateDial(mBleData.getGas_degree());
                    // 切换机器人的状态:油门状态发生变化时，才切换状态，否则子线程重复发送消息
                    if (mBleData.getGas_degree() != 1) {
                        if (!gas_state) {
                            setState(ROBOT_STATE_HAS_GAS);
                        }
                        gas_state = true;
                    } else {
                        if (gas_state) {
                            setState(ROBOT_STATE_NO_GAS);
                        }
                        gas_state = false;
                    }
                }
            }
//			if (GasService.ACTION_LEVEL_CHANGE.equals(action)) {
//				int level = intent.getIntExtra(
//						ConstantUtils.INTENT_AUTOMATIC_LEVE,
//						ConstantUtils.DEFAULT_AUTOMATIC_CURRENT_LEVEL);
//				saveLevel(level);
//				updateView(level);
//			}
//			if (GasService.ACTION_POINTS.equals(action)) {
//				LogUtils.d(TAG, "--收到积分广播--");
//				int points = intent.getIntExtra(
//						ConstantUtils.INTENT_AUTOMATIC_POINTS, 0);
//				updateProgress(points);
//			}
        }
    };

    /**
     * 设置监听的动作
     *
     * @return
     */
    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
//		intentFilter.addAction(GasService.ACTION_LEVEL_CHANGE);
//		intentFilter.addAction(GasService.ACTION_POINTS);
        return intentFilter;
    }

    /**
     * 更新仪表盘的油门数据
     *
     * @param gas_degree
     */

    private ObjectAnimator animator;
    private float begin;// 指针旋转的起始角度
    private float end;// 指针旋转的结束角度

    protected void UpdateDial(int gas_degree) {
        LogUtils.d(TAG, "--UpdateDial--");
        end = (gas_degree) * 180 / 10;
        startAnim(end);
        history_gas = gas_degree;
    }

    private void startAnim(Float end) {
        if (animator != null) {
            animator.cancel();
        }
        animator = ObjectAnimator.ofFloat(imageView_automaticFrag_pointer,
                "rotation", begin, end).setDuration(1000);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                begin = imageView_automaticFrag_pointer.getRotation();
                Log.d("Test", "rotation:" + begin);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                begin = imageView_automaticFrag_pointer.getRotation();
                Log.d("Test", "rotation:" + begin);
            }

        });
    }

    /**
     * 保存当前level到sp中
     *
     * @param
     * @param
     */
    protected void saveLevel(int level) {
        if (ConstantUtils.ENABLE_AUTOMATIC) {
            if (level > 0 & level <= 41) {
                SPUtils.putInt(getActivity(),
                        ConstantUtils.SP_KEY_AUTOMATIC_CURRENT_LEVEL, level);
            }
        }
    }

    //	private Vibrator mVibrator;
//	private MediaPlayer mMediaPlayer;
    private static final long VIBRATOR_DURATION = 200;
    private static final float BEEP_VOLUME = 0.10f;// 音效音量

    private void initPlaySound(int resId) {
        LogUtils.d(TAG, "initPlaySound");
//		mMediaPlayer = new MediaPlayer();
//		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//		AssetFileDescriptor file = getResources().openRawResourceFd(resId);
//		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
//
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				mp.seekTo(0);
//			}
//		});
//		try {
//			mMediaPlayer.setDataSource(file.getFileDescriptor(),
//					file.getStartOffset(), file.getLength());
//			file.close();
//			mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//			mMediaPlayer.prepare();
//		} catch (IOException e) {
//			e.printStackTrace();
//			mMediaPlayer = null;
//		}

    }

    private void initVibrator() {
        LogUtils.d(TAG, "initVibrator");
//		mVibrator = (Vibrator) getActivity().getSystemService(
//				Context.VIBRATOR_SERVICE);
    }

    /**
     * 开始震动
     */
    protected void startVibrate() {
        LogUtils.d(TAG, "bellRingAndPlaySound");

//		if (SPUtils.getBoolean(getActivity(),
//				ConstantUtils.SP_KEY_ENABLE_VIBRATOR, true)
//				&& mVibrator != null) {
//			mVibrator.vibrate(VIBRATOR_DURATION);
//		}
    }

    /**
     * 响起音效
     */
    protected void startPlaySound() {
//		if (SPUtils.getBoolean(getActivity(),
//				ConstantUtils.SP_KEY_ENABLE_SOUND, true)
//				&& mMediaPlayer != null) {
//			mMediaPlayer.start();
//		}
    }


}
