package com.minfo.carrepairseller.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.util.EasyUtils;
import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.query.ProductPurchaseVINActivity;
import com.minfo.carrepairseller.activity.shop.ProductDetailActivity;
import com.minfo.carrepairseller.utils.Utils;

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {
    //purchase : 求购功能使用的常量

    private static final int MESSAGE_TYPE_SEND_PURCHASE = 1;
    private static final int MESSAGE_TYPE_RECV_PURCHASE = 2;
    private static final int MESSAGE_TYPE_SELECT_PURCHASE = 3003;


    //开单 功能使用的常量

    private static final int MESSAGE_TYPE_SEND_ORDER = 3;
    private static final int MESSAGE_TYPE_RECV_ORDER = 4;
    private static final int MESSAGE_TYPE_SELECT_ORDER = 3006;

    //报价 功能使用的常量

    private static final int MESSAGE_TYPE_SEND_PRICE = 5;
    private static final int MESSAGE_TYPE_RECV_PRICE = 6;
    private static final int MESSAGE_TYPE_SELECT_PRICE = 3009;

    //商品 功能使用的常量

    private static final int MESSAGE_TYPE_SEND_SHOP = 7;
    private static final int MESSAGE_TYPE_RECV_SHOP = 8;
    private static final int MESSAGE_TYPE_SELECT_SHOP = 3012;

    private static final int PURCHASE = 3013;
    private static final int ORDER = 3014;
    private static final int PRICE = 3015;
    private static final int SHOP = 3016;

    private Utils utils;
    private String userNickName;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        utils = new Utils(getActivity());
        Bundle bundle = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);

        super.setUpView();
        // set click listener
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (EasyUtils.isSingleActivity(getActivity())) {
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
                }
                onBackPressed();
            }
        });
        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());

    }


    @Override
    public void onEnterToChatDetails() {

    }

    //点击跳向个人名片界面
    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
//        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case PURCHASE://求购
                Intent intent_purchase = new Intent(getActivity(), ProductPurchaseVINActivity.class);
                intent_purchase.putExtra("fromchat", "fromchat");
                startActivityForResult(intent_purchase, MESSAGE_TYPE_SELECT_PURCHASE);
                break;
            case ORDER://开单
                Intent intent_order = new Intent(getActivity(), ProductDetailActivity.class);
                startActivityForResult(intent_order, MESSAGE_TYPE_SELECT_ORDER);
//                Intent intent_purchase = new Intent(getActivity(), ProductDetailActivity.class);
//                startActivityForResult(intent_purchase, MESSAGE_TYPE_SELECT_PURCHASE);
                break;
            case PRICE://报价
                Intent intent_price = new Intent(getActivity(), ProductDetailActivity.class);
                startActivityForResult(intent_price, MESSAGE_TYPE_SELECT_PRICE);
                break;

        }
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {

        return new CustomChatRowProvider();
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        inputMenu.registerExtendMenuItem(R.string.car_purchase, R.drawable.car_purchase_selector, PURCHASE, extendMenuItemClickListener);
        //inputMenu.registerExtendMenuItem(R.string.car_order, R.drawable.car_order_selector, ORDER, extendMenuItemClickListener);
        //inputMenu.registerExtendMenuItem(R.string.car_price, R.drawable.car_price_selector, PRICE, extendMenuItemClickListener);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MESSAGE_TYPE_SELECT_PURCHASE:
                    Log.e("发送求购消息", "PURCHASE");
                    String nameOfPei = data.getStringExtra("name");
                    String idOfPei = data.getStringExtra("id");
                    String nameOfCar = data.getStringExtra("nameofcar");

                    EMMessage mes_purchase = EMMessage.createTxtSendMessage("[求购信息]", toChatUsername);
                    mes_purchase.setAttribute(HuanUtils.MESSAGE_TYPE, getActivity().getResources().getString(R.string.purchase_string));
                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.car_name), nameOfPei);
                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.car_icon), "wwwjhnjjjjj");
                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.ca_price), "555");
                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.car_infoId), idOfPei);
                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.car_carTpye), nameOfCar);
                    String userPic = utils.getImg();
                    if (!TextUtils.isEmpty(userPic)) {
                        mes_purchase.setAttribute("userHeadImage", userPic);
                    }
                    String userName = utils.getName();
                    if (!TextUtils.isEmpty(userName)) {
                        mes_purchase.setAttribute("userNickName", userName);
                    }
                    EMClient.getInstance().chatManager().sendMessage(mes_purchase);
                    break;
                case MESSAGE_TYPE_SELECT_ORDER:
                    Log.e("发送开单消息", "ORDER");
                    EMMessage mes_order = EMMessage.createTxtSendMessage("66", toChatUsername);
                    mes_order.setAttribute(HuanUtils.MESSAGE_TYPE, getActivity().getResources().getString(R.string.order_string));
                    mes_order.setAttribute(getActivity().getResources().getString(R.string.car_name), "大灯");
                    mes_order.setAttribute(getActivity().getResources().getString(R.string.car_icon), "wwwjhnjjjjj");
                    mes_order.setAttribute(getActivity().getResources().getString(R.string.ca_price), "66666");
                    mes_order.setAttribute(getActivity().getResources().getString(R.string.car_infoId), "555");
                    mes_order.setAttribute(getActivity().getResources().getString(R.string.car_carTpye), "555");
//
                    EMClient.getInstance().chatManager().sendMessage(mes_order);

//                    EMMessage mes_purchase = EMMessage.createTxtSendMessage("55", HuanUtils.tochat);
//                    mes_purchase.setAttribute(HuanUtils.MESSAGE_TYPE, getActivity().getResources().getString(R.string.purchase_string));
//                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.car_name), "大灯");
//                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.car_icon), "wwwjhnjjjjj");
//                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.ca_price), "66666");
//                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.car_infoId), "555");
//                    mes_purchase.setAttribute(getActivity().getResources().getString(R.string.car_carTpye), "555");
//                    EMClient.getInstance().chatManager().sendMessage(mes_purchase);
                    break;
                case MESSAGE_TYPE_SELECT_PRICE:

                    EMMessage mes_price = EMMessage.createTxtSendMessage("77", toChatUsername);
                    mes_price.setAttribute(HuanUtils.MESSAGE_TYPE, getActivity().getResources().getString(R.string.price_string));
                    mes_price.setAttribute(getActivity().getResources().getString(R.string.car_name), "大灯");
                    mes_price.setAttribute(getActivity().getResources().getString(R.string.car_icon), "wwwjhn;n;kj");
                    mes_price.setAttribute(getActivity().getResources().getString(R.string.ca_price), "66666");
                    mes_price.setAttribute(getActivity().getResources().getString(R.string.car_infoId), "555");
                    mes_price.setAttribute(getActivity().getResources().getString(R.string.car_carTpye), "555");

                    EMClient.getInstance().chatManager().sendMessage(mes_price);
                    break;
//
                default:
                    break;
            }
        }
    }


    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {

        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            Log.e("发送求购消息", "7777");

            return 6;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {

            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
//
                if (Purchase.isPurchaseMessage(message)) {
                    //发送求购消息
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_PURCHASE : MESSAGE_TYPE_SEND_PURCHASE;
                } else if (Purchase.isOrderMessage(message)) {
                    //发送开单消息
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_ORDER : MESSAGE_TYPE_SEND_ORDER;
                } else if (Purchase.isPriceMessage(message)) {
                    //发送报价消息
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_PRICE : MESSAGE_TYPE_SEND_PRICE;
                }
//                else if (Purchase.isShopMessage(message)) {
//                    //发送商品消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_SHOP : MESSAGE_TYPE_SEND_SHOP;
//                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (Purchase.isPurchaseMessage(message)) {
                    //发送求购消息
                    return new ChatRowPurchaseCall(getActivity(), message, position, adapter);
                } else if (Purchase.isOrderMessage(message)) {
                    //发送开单消息
                    return new ChatRowPurchaseCall(getActivity(), message, position, adapter);
                } else if (Purchase.isPriceMessage(message)) {
                    //发送报价消息
                    return new ChatRowPurchaseCall(getActivity(), message, position, adapter);
                }
//                else if (Purchase.isShopMessage(message)) {
//                    //发送求购消息
//                    return new ChatRowPurchaseCall(getActivity(), message, position, adapter);
//                }
            }
            return null;
        }
    }

//    @Override
//    public void onCmdMessageReceived(List<EMMessage> messages) {
//        for (EMMessage message : messages) {
//            EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//            String action = cmdMsgBody.action();//获取自定义action
//            messageList.refresh();
//        }
//        super.onCmdMessageReceived(messages);
//    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {


        // 通过扩展属性，将自己的userPic和userName发送出去。
        String userPic = utils.getImg();
        if (!TextUtils.isEmpty(userPic)) {
            message.setAttribute("userHeadImage", userPic);
        }
        String userName = utils.getName();
        if (!TextUtils.isEmpty(userName)) {
            message.setAttribute("userNickName", userName);
        }


    }
}
