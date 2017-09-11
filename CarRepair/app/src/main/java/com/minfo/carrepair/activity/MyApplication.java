package com.minfo.carrepair.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.minfo.carrepair.chat.ChatActivity;
import com.minfo.carrepair.chat.EmojiconExampleGroupData;
import com.minfo.carrepair.utils.CrashHandler;
import com.mob.MobApplication;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by liujing on 15/8/25.
 */
public class MyApplication extends MobApplication {
    private static MyApplication mInstance;
    private ImageLoader mImgLoader;


    public String registrationId = "";
    public static long start;
    private EaseUI easeUI;
    public final String PREF_USERNAME = "username";

    static {
        System.loadLibrary("CARREPAIR");
    }

    @Override
    public void onCreate() {
        start = System.currentTimeMillis();
        super.onCreate();
        mInstance = this;
        getImgLoader();
        init(mInstance);

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        Log.e("11", "JPushInterface.init(this) ");

        JPushInterface.init(this);
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public ImageLoader getImgLoader() {
        if (mImgLoader == null) {
            mImgLoader = ImageLoader.getInstance();
            mImgLoader.init(initImgloadConf());
        }
        return mImgLoader;
    }

    private ImageLoaderConfiguration initImgloadConf() {

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "img/cache");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(1024, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                        // .diskCacheExtraOptions(480, 800,null)  // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量,最好是1-5
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()

                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(20)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)//缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                        // 将缓存下来的文件以什么方式命名
                        // 里面可以调用的方法有
                        // 1.new Md5FileNameGenerator() //使用MD5对UIL进行加密命名
                        // 2.new HashCodeFileNameGenerator()//使用HASHCODE对UIL进行加密命名
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)

                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 20 * 1000, 30 * 1000)) // connectTimeout (20 s), readTimeout (30 s)超时时间
                .imageDecoder(new BaseImageDecoder(false)) // default
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        return config;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context) {
        EMOptions options = initChatOptions();
//        options.setRestServer("103.241.230.122:31111");
//        options.setIMServer("103.241.230.122");
//        options.setImPort(31097);

        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {

            //debug mode, you'd better set it to false, if you want release your App officially.
            EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //to set user's profile and avatar
            setEaseUIProviders();
            //initialize preference manager
            //PreferenceManager.init(context);
            //initialize profile manager
            // getUserProfileManager().init(context);
            //set Call options
            //setCallOptions();

            // TODO: set Call options
//            // min video kbps
//            int minBitRate = PreferenceManager.getInstance().getCallMinVideoKbps();
//            if (minBitRate != -1) {
//                EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(minBitRate);
//            }
//
//            // max video kbps
//            int maxBitRate = PreferenceManager.getInstance().getCallMaxVideoKbps();
//            if (maxBitRate != -1) {
//                EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(maxBitRate);
//            }
//
//            // max frame rate
//            int maxFrameRate = PreferenceManager.getInstance().getCallMaxFrameRate();
//            if (maxFrameRate != -1) {
//                EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(maxFrameRate);
//            }
//
//            // audio sample rate
//            int audioSampleRate = PreferenceManager.getInstance().getCallAudioSampleRate();
//            if (audioSampleRate != -1) {
//                EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(audioSampleRate);
//            }

            /**
             * This function is only meaningful when your app need recording
             * If not, remove it.
             * This function need be called before the video stream started, so we set it in onCreate function.
             * This method will set the preferred video record encoding codec.
             * Using default encoding format, recorded file may not be played by mobile player.
             */
            //EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);

            // resolution
//            String resolution = PreferenceManager.getInstance().getCallBackCameraResolution();
//            if (resolution.equals("")) {
//                resolution = PreferenceManager.getInstance().getCallFrontCameraResolution();
//            }
//            String[] wh = resolution.split("x");
//            if (wh.length == 2) {
//                try {
//                    EMClient.getInstance().callManager().getCallOptions().setVideoResolution(new Integer(wh[0]).intValue(), new Integer(wh[1]).intValue());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            // enabled fixed sample rate
//            boolean enableFixSampleRate = PreferenceManager.getInstance().isCallFixedVideoResolution();
//            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(enableFixSampleRate);
//
//            // Offline call push
//            EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(getModel().isPushCall());
//
//            setGlobalListeners();
//            broadcastManager = LocalBroadcastManager.getInstance(appContext);
//            initDbDao();
        }
    }

    //初始化环信
    private EMOptions initChatOptions() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
//初始化
        EMClient.getInstance().init(mInstance, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

//        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
//        options.setDeleteMessagesAsExitGroup(getModel().isDeleteMessagesAsExitGroup());
//        options.setAutoAcceptGroupInvitation(getModel().isAutoAcceptGroupInvitation());
//
//        EMOptions options = new EMOptions();
//        // set if accept the invitation automatically
//        options.setAcceptInvitationAlways(false);
//        // set if you need read ack
//        options.setRequireAck(true);
//        // set if you need delivery ack
//        options.setRequireDeliveryAck(false);
//
//        //you need apply & set your own id if you want to use google cloud messaging.
//        options.setGCMNumber("324169311137");
//        //you need apply & set your own id if you want to use Mi push notification
//        options.setMipushConfig("2882303761517426801", "5381742660801");
//        //you need apply & set your own id if you want to use Huawei push notification
//        options.setHuaweiPushAppId("10492024");
//
//        //set custom servers, commonly used in private deployment
//        if(demoModel.isCustomServerEnable() && demoModel.getRestServer() != null && demoModel.getIMServer() != null) {
//            options.setRestServer(demoModel.getRestServer());
//            options.setIMServer(demoModel.getIMServer());
//            if(demoModel.getIMServer().contains(":")) {
//                options.setIMServer(demoModel.getIMServer().split(":")[0]);
//                options.setImPort(Integer.valueOf(demoModel.getIMServer().split(":")[1]));
//            }
//        }
//
//        if (demoModel.isCustomAppkeyEnabled() && demoModel.getCutomAppkey() != null && !demoModel.getCutomAppkey().isEmpty()) {
//            options.setAppKey(demoModel.getCutomAppkey());
//        }
//
//        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
//        options.setDeleteMessagesAsExitGroup(getModel().isDeleteMessagesAsExitGroup());
//        options.setAutoAcceptGroupInvitation(getModel().isAutoAcceptGroupInvitation());

        return options;
    }

    private EaseUser getUserInfo(String username) {
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        EaseUser user = null;
//        if(username.equals(EMClient.getInstance().getCurrentUser()))
//            return getUserProfileManager().getCurrentUserInfo();
//        user = getContactList().get(username);
//        if(user == null && getRobotList() != null){
//            user = getRobotList().get(username);
//        }

        // if user is not in your contacts, set inital letter for him/her
        if (user == null) {
            user = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(user);
        }
        return user;
    }

    protected void setEaseUIProviders() {
        //set user avatar to circle shape
        EaseAvatarOptions avatarOptions = new EaseAvatarOptions();
        avatarOptions.setAvatarShape(1);
        easeUI.setAvatarOptions(avatarOptions);

        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

//        //set options
//        easeUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {
//
//            @Override
//            public boolean isSpeakerOpened() {
//                return demoModel.getSettingMsgSpeaker();
//            }
//
//            @Override
//            public boolean isMsgVibrateAllowed(EMMessage message) {
//                return demoModel.getSettingMsgVibrate();
//            }
//
//            @Override
//            public boolean isMsgSoundAllowed(EMMessage message) {
//                return demoModel.getSettingMsgSound();
//            }
//
//            @Override
//            public boolean isMsgNotifyAllowed(EMMessage message) {
//                if(message == null){
//                    return demoModel.getSettingMsgNotification();
//                }
//                if(!demoModel.getSettingMsgNotification()){
//                    return false;
//                }else{
//                    String chatUsename = null;
//                    List<String> notNotifyIds = null;
//                    // get user or group id which was blocked to show message notifications
//                    if (message.getChatType() == EMMessage.ChatType.Chat) {
//                        chatUsename = message.getFrom();
//                        notNotifyIds = demoModel.getDisabledIds();
//                    } else {
//                        chatUsename = message.getTo();
//                        notNotifyIds = demoModel.getDisabledGroups();
//                    }
//
//                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            }
//        });
        //set emoji icon provider
        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
                for (EaseEmojicon emojicon : data.getEmojiconList()) {
                    if (emojicon.getIdentityCode().equals(emojiconIdentityCode)) {
                        return emojicon;
                    }
                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });

//        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, mInstance);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
//                EaseUser user = getUserInfo(message.getFrom());
//                if(user != null){
//                    if(EaseAtMessageHelper.get().isAtMeMsg(message)){
//                        return String.format(mInstance.getString(R.string.at_your_in_group), user.getNick());
//                    }
//                    return user.getNick() + ": " + ticker;
//                }else{
//                    if(EaseAtMessageHelper.get().isAtMeMsg(message)){
//                        return String.format(mInstance.getString(R.string.at_your_in_group), message.getFrom());
//                    }
//                    return message.getFrom() + ": " + ticker;
//                }
                return "";
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(mInstance, ChatActivity.class);
                // open calling activity if there is call

                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { // single chat message
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", 1);
                } else { // group chat message
                    // message.getTo() is the group id
//                        intent.putExtra("userId", message.getTo());
//                        if(chatType == EMMessage.ChatType.GroupChat){
//                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
//                        }else{
//                            intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
//                        }

                }

                return intent;
            }
        });
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

}
