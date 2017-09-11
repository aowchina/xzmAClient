package com.minfo.carrepairseller.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.minfo.carrepairseller.activity.publish.ShowPriceActivity;
import com.minfo.carrepairseller.activity.purchase.MyPurchaseActivity;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;
import com.minfo.carrepairseller.utils.Utils;

/**
 * Created by min-fo-012 on 17/6/3.
 */
public class ChatRowPurchaseCall extends EaseChatRow {
    private TextView tv_title, tv_price, tv_name, tv_kind;
    private ImageView iv_icon;
    private String priceStr, kindStr, nameStr, iconStr, idStr;
    private boolean recIsTrue;
    private ImageView ivIcon;
    private Utils utils;
    private String img;

    public ChatRowPurchaseCall(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        utils = new Utils(context);


    }

    /**
     * 填充布局
     */
    @Override
    protected void onInflateView() {

        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.item_row_receive_purchase : R.layout.item_row_sent_purchase, this);
        recIsTrue = (message.direct() == EMMessage.Direct.RECEIVE ? false :true );

    }

    /**
     * 查找控件
     */
    @Override
    protected void onFindViewById() {
        tv_title = ((TextView) findViewById(R.id.tv_title));
        tv_price = ((TextView) findViewById(R.id.tv_price));
        tv_name = ((TextView) findViewById(R.id.tv_name));
        tv_kind = ((TextView) findViewById(R.id.tv_kind));
        iv_icon = ((ImageView) findViewById(R.id.iv_icon));
        ivIcon = ((ImageView) findViewById(R.id.iv_userhead));


    }

    /**
     * 消息状态改变，刷新listview
     */
    @Override
    protected void onUpdateView() {

    }


    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        priceStr = message.getStringAttribute("price", null);
        nameStr = message.getStringAttribute("name", null);
        idStr = message.getStringAttribute("infoId", null);
        iconStr = message.getStringAttribute("icon", null);
        kindStr = message.getStringAttribute("carType", null);
        img = message.getStringAttribute("userHeadImage", null);

        tv_price.setText(nameStr);
        tv_name.setText(kindStr);
        if (recIsTrue) {

            UniversalImageUtils.displayImageCircle(utils.getImg(), ivIcon, 120);
            Log.e("img", "getImg  " + utils.getImg());

        } else {
            UniversalImageUtils.displayImageCircle(img, ivIcon, 120);
            Log.e("img", "img  " + img);

        }
        if (Purchase.isPurchaseMessage(message)) {
            //求购消息

            iv_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.qixiu_gou));
            tv_kind.setText(getResources().getString(com.minfo.carrepairseller.R.string.purchase_kind));
            if (recIsTrue) {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.purchase_name_send));


            } else {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.purchase_name_receive));

            }
        } else if (Purchase.isPriceMessage(message)) {
            //报价消息
            iv_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.qixiu_fu));

            tv_kind.setText(getResources().getString(com.minfo.carrepairseller.R.string.price_namekind));
            if (recIsTrue) {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.price_name_send));
            } else {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.price_name_receive));
            }
        }
//        else if (Purchase.isOrderMessage(message)) {
//            //开单消息
//            tv_price.setText(priceStr);
//            tv_name.setText(nameStr);
//            tv_kind.setText(getResources().getString(com.minfo.carrepairseller.R.string.order_name_kind));
//            if (recIsTrue) {
//                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.order_name_send));
//            } else {
//                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.order_name_rceive));
//
//            }
//        }
//        else if (Purchase.isShopMessage(message)) {
//            //商品消息
//            tv_price.setText(priceStr);
//            tv_name.setText(kindStr);
//            tv_title.setText(nameStr);
//
//            tv_kind.setText(getResources().getString(com.minfo.carrepairseller.R.string.product_name_kind));
//
//        }


    }

    @Override
    protected void onBubbleClick() {
//        Toast.makeText(context, "ChatRowPurchaseCall", Toast.LENGTH_SHORT).show();

        if (Purchase.isPurchaseMessage(message)) {


            if (recIsTrue) {//点击查看求购消息
                Intent intentPurChase = new Intent(context, MyPurchaseActivity.class);
                intentPurChase.putExtra("id", idStr);
                context.startActivity(intentPurChase);

            }
//            else {//点击查看报价消息
//                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.purchase_name_receive));
//
//            }

//        } else if (Purchase.isShopMessage(message)) {
//
//            if (recIsTrue) {//点击查看商品消息
////                Intent intentPurChase = new Intent(context, ProductDetailActivity.class);
////                intentPurChase.putExtra("id", idStr);
////                context.startActivity(intentPurChase);
//
//            }
        }else if (Purchase.isPriceMessage(message)) {
            //报价消息

            if (!recIsTrue) {//点击查看报价消息
//                ToastUtils.show(context, "查看报价");
                Intent intentPurChase = new Intent(context, ShowPriceActivity.class);
                intentPurChase.putExtra("id", idStr);
                context.startActivity(intentPurChase);
            }
        }

    }
}
