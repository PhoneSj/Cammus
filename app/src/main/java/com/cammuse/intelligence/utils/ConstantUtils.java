package com.cammuse.intelligence.utils;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.device.BluetoothLeService;
import com.cammuse.intelligence.entity.BleDevice;

public class ConstantUtils {

    // 蓝牙的俩接状态
    public static boolean mConnected;
    public static BluetoothLeService mBluetoothLeService;
    //	public static FunctionActivity mFunctionActivity;
    public static int EQUIT_TYPE = 0;
    public static int EQUIT_ID = 0;
    public static String BLE_CHECK = "SR CHECK ACCEL ID EN";
    // UserActivity
    public static final String SP_FILENAME = "sp_file";
    public static final String SP_KEY_USERNAME = "username";
    public static final String SP_KEY_PASSWORD = "password";
    public static final String SP_KEY_REGISTER_TIME = "register_time";
    public static final String SP_KEY_NICKNAME = "nickname";
    public static final String SP_KEY_LAST_UPLOAD_GAS_DAY = "last_upload_gas_day";
    public static final String SP_KEY_LOCK_CHOOSE = "lock_choose";
    // Intent的action值
    public static final String INTENT_PHONENUMBER = "phoneNumber";
    public static final String INTENT_PASSWORD = "password";
    public static final String INTENT_CHANGEUER = "changeUser";
    public static final String INTENT_CURRENT_MODE = "current_mode";
    public static final String INTENT_IS_VISTIOR = "is_visitor";// 是否是游客身份
    public static final String INTENT_AUTOMATIC_LEVE = "automatic_level";
    public static final String INTENT_AUTOMATIC_POINTS = "automatic_points";
    // settingActivity
    public static final String SP_KEY_ENABLE_LOCK = "enable_lock";
    public static final String SP_KEY_ENABLE_SOUND = "enable_sound";
    public static final String SP_KEY_ENABLE_VIBRATOR = "enable_vibrator";

    public static boolean lock_state = false;// 锁的开启状态（true表示手势锁已经验证通过，false表示手势锁没有验证）
    /**
     * 手动控制模式
     */
    public static String manual_mode_name[] = {"原车模式", "省油模式", "运动模式",
            "超级运动模式", "赛车模式", "智能模式"};
    // 六种驾驶模式
    public static final int DRIVE_MODE_ORIGINAL = 0;
    public static final int DRIVE_MODE_SAVE = DRIVE_MODE_ORIGINAL + 1;
    public static final int DRIVE_MODE_SPORT = DRIVE_MODE_SAVE + 1;
    public static final int DRIVE_MODE_SUPER = DRIVE_MODE_SPORT + 1;
    public static final int DRIVE_MODE_RACE = DRIVE_MODE_SUPER + 1;
    public static final int DRIVE_MODE_SMART = DRIVE_MODE_RACE + 1;
    // 默认值
    public static final String SP_KEY_MANAUAL_MODE = "manaual_current_mode";
    public static final String SP_KEY_MANAUAL_BAR = "manaual_current_bar";
    public static final int DEFAULT_MANAUAL_CURRENT_MODE = DRIVE_MODE_ORIGINAL;
    public static final int DEFAULT_MANAUAL_CURRENT_BAR = 1;
    // 驾驶模式bar写入sp中的常亮
    public static final String ORIGINAL_MODE_BAR_NUMBER = "original_mode_bar_number";
    public static final String SAVE_MODE_BAR_NUMBER = "save_mode_bar_number";
    public static final String SPORT_MODE_BAR_NUMBER = "sport_mode_bar_number";
    public static final String SUPER_MODE_BAR_NUMBER = "super_mode_bar_number";
    public static final String RACE_MODE_BAR_NUMBER = "race_mode_bar_number";
    public static final String SMART_MODE_BAR_NUMBER = "smart_mode_bar_number";
    public static final String SEND_CODE_COUNT = "send_code_count";// 当日该手机发送验证码的次数
    public static final String RECORD_DATE = "current_date";
    public static final String BIND_DEVICE_NAME = "bind_device_name";
    public static final String BIND_DEVICE_ADDRESS = "bind_device_address";
    public static final String BIND_DEVICES = "bind_devices";
    public static final String JSON_KEY_DEVICE = "device";


    public static String sp_mode_keys[] = {ORIGINAL_MODE_BAR_NUMBER,
            SAVE_MODE_BAR_NUMBER, SPORT_MODE_BAR_NUMBER, SUPER_MODE_BAR_NUMBER,
            RACE_MODE_BAR_NUMBER, SMART_MODE_BAR_NUMBER};

    /**
     * 自动控制模式
     */
    public static boolean ENABLE_AUTOMATIC = false;
    public static final String SP_KEY_IS_FIRST_USE_AUTOMATIC = "is_first_use_automatic";
    public static final boolean DEFAULT_IS_FIRST_ENTER_AUTOMATIC_MODE = true;
    public static final String SP_KEY_AUTOMATIC_CURRENT_LEVEL = "automatic_current_level";// 范围【0，40】，共41个值
    public static final int DEFAULT_AUTOMATIC_CURRENT_LEVEL = 15;
    // level的阈值
    public static final int UP_THRESHOLD = 0;
    public static final int DOWM_THRESHOLD = UP_THRESHOLD + 1;
    public static int LEVEL_MAX = 40;
    public static int LEVEL_MIN = 1;

    public static String automatic_level_name[] = {"环保一族", "环保达人", "爱车一族",
            "爱车达人", "玩车一族", "玩车达人", "赛车一族", "赛车达人"};

    public static final int DEFAULT_GAS_SAMPLE_INTERVAL = 1000;// 默认采样时间
    public static final int DEFAULT_GAS_BENCHMARK[] = {4, 5, 6, 7};// 默认油门基准值
    public static final int DEFAULT_GAS_GRADIENT[] = {2, 2, 2, 2};// 默认油门梯度
    public static final int DEFAULT_LEVEL_THRESHOLD[][] = {{50, 50},
            {50, 50}, {50, 50}, {50, 50}};// 默认油门积分上下限

    public static int active_sample_time = 1000 * 30;// 有效采样时间
    public static int gas_sample_interval = DEFAULT_GAS_SAMPLE_INTERVAL;// 采样间隔
    public static int gas_benchmark[] = DEFAULT_GAS_BENCHMARK;
    public static int gas_gradient[] = DEFAULT_GAS_GRADIENT;
    public static int level_threshold[][] = DEFAULT_LEVEL_THRESHOLD;

    /**
     * Handler
     */
    public static final int HANDLER_DIALOG_TIMEOUT = 100;// 对话框测超时
    public static final int HANDLER_WHAT_REQUEST_FAILURE = HANDLER_DIALOG_TIMEOUT + 1;// 请求失败
    public static final int HANDLER_WHAT_REQUEST_SUCCESS_CHECK_USERNAME = HANDLER_WHAT_REQUEST_FAILURE + 1;// 核对用户名请求成功
    public static final int HANDLER_WHAT_REQUEST_SUCCESS_LOGIN = HANDLER_WHAT_REQUEST_SUCCESS_CHECK_USERNAME + 1;// 登录请求成功
    public static final int HANDLER_WHAT_REQUEST_SUCCESS_SUBMIT = HANDLER_WHAT_REQUEST_SUCCESS_LOGIN + 1;// 提交用户信息（注册）请求成功
    public static final int HANDLER_WHAT_REQUEST_SUCCESS_SENDCODE = HANDLER_WHAT_REQUEST_SUCCESS_SUBMIT + 1;// 请求发送验证码成功
    public static final int HANDLER_WHAT_REQUEST_SUCCESS_CHECKCODE = HANDLER_WHAT_REQUEST_SUCCESS_SENDCODE + 1;// 请求核对验证码成功
    public static final int HANDLER_WHAT_REQUEST_SUCCESS_UPLOAD_GAS = HANDLER_WHAT_REQUEST_SUCCESS_CHECKCODE + 1;// 上传油门数据请求成功
    public static final int HANDLER_WHAT_REQUEST_SUCCESS_UPLOAD_EXCEPTION = HANDLER_WHAT_REQUEST_SUCCESS_UPLOAD_GAS + 1;// 上传异常文件请求成功
    public static final int HANDLER_WHAT_AUTOMATIC_LEVEL_CHANGED = HANDLER_WHAT_REQUEST_SUCCESS_UPLOAD_EXCEPTION + 1;
    public static final int HANDLER_WHAT_AUTOMATIC_POINTS = HANDLER_WHAT_AUTOMATIC_LEVEL_CHANGED + 1;
    public static final int HANDLER_WHAT_REQUEST_SUCCESS_DOWNLOAD_AUTODATA = HANDLER_WHAT_AUTOMATIC_POINTS + 1;
    /**
     * 短信验证
     */
    public static final String APPKEY = "e7ff665e94dc";
    public static final String APPSECRET = "687ac3646a408f2bf87ebac325b37302";
    public static final int MAX_SEND_NUMBER = 10;// 一天最多能发送的验证码数量
    /**
     * Url
     */
    public static String URL_REGISTER = "http://www.cammus.com/appcode/register/register.aspx";
    public static String URL_LOGIN = "http://www.cammus.com/appcode/login/login.aspx";
    public static String URL_REQUEST_CODE = "http://www.cammus.com/appcode/edituserinfo/sendcode.aspx";
    public static String URL_CHECK_CODE = "http://www.cammus.com/appcode/edituserinfo/checkcode.aspx";
    public static String URL_MODIFY_CODE = "http://www.cammus.com/appcode/edituserinfo/setpwd.aspx";
    public static String URL_OFFICIAL_WEBSITE = "<a href=\"http://www.cammus.com.cn/\"><u>http://www.cammus.com.cn/</u></a>";
    public static String URL_CHECK_USERNAME = "";// TODO
    public static String URL_UPLOAD_GAS = "http://www.cammus.com/appcode/carinfo/gasinfo.aspx";
    public static String URL_UPLOAD_EXCEPTION = "";// TODO
    public static String URL_DOWNLOAD_AUTODATA = "http://www.cammus.com/appcode/carinfo/config.aspx";

    /**
     * 资源id
     */
    public static int[] resModeImgNormalId = {R.mipmap.mode_original,
            R.mipmap.mode_save, R.mipmap.mode_sport, R.mipmap.mode_super,
            R.mipmap.mode_race, R.mipmap.mode_smart};
    public static int[] resModeImgSelectedId = {
            R.mipmap.mode_original_selected, R.mipmap.mode_save_selected,
            R.mipmap.mode_sport_selected, R.mipmap.mode_super_selected,
            R.mipmap.mode_race_selected, R.mipmap.mode_smart_selected};

    public static int[] resbarId = {R.mipmap.bar0, R.mipmap.bar1,
            R.mipmap.bar2, R.mipmap.bar3, R.mipmap.bar4, R.mipmap.bar5,
            R.mipmap.bar6, R.mipmap.bar7, R.mipmap.bar8, R.mipmap.bar9,
            R.mipmap.bar10};
    public static int mCircleImageViewIds[] = {
            R.id.imageView_manualFrag_original, R.id.imageView_manualFrag_save,
            R.id.imageView_manualFrag_sport, R.id.imageView_manualFrag_super,
            R.id.imageView_manualFrag_race, R.id.imageView_manualFrag_smart};


//    public static boolean USER_LOGIN_STATE = false;//用户当前状态

    //请求码
    public static final int REQUEST_CODE_LOGIN = 100;
    public static final int REQUEST_CODE_USERINFO = REQUEST_CODE_LOGIN + 1;
    //结果码
    public static final int RESULT_CODE_LOGIN = 200;
    public static final int RESULT_CODE_USERINFO = RESULT_CODE_LOGIN + 1;

    public static BleDevice connectedBleDevice;


    public static final int EQUIT_TYPE_METER = 1;
    public static final int EQUIT_TYPE_ACCELERATOR = 2;

}
