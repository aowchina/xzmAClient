package com.minfo.carrepairseller.chat;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

/**
 * Created by min-fo-012 on 17/6/5.
 */
public class ChatRowOrder extends EaseChatRow {
    private TextView tv_title, tv_price, tv_name, tv_kind;
    private ImageView iv_icon;
    private String priceStr, kindStr, nameStr, iconStr, idStr;
    private boolean recIsTrue;

    public ChatRowOrder(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);

    }

    /**
     * 填充布局
     */
    @Override
    protected void onInflateView() {
        if (Purchase.isOrderMessage(message)) {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? com.hyphenate.easeui.R.layout.item_row_receive_purchase : com.hyphenate.easeui.R.layout.item_row_sent_purchase, this);

        }
        recIsTrue = (message.direct() == EMMessage.Direct.RECEIVE ? true : false);

    }

    /**
     * 查找控件
     */
    @Override
    protected void onFindViewById() {
        tv_title = ((TextView) findViewById(com.hyphenate.easeui.R.id.tv_title));
        tv_price = ((TextView) findViewById(com.hyphenate.easeui.R.id.tv_price));
        tv_name = ((TextView) findViewById(com.hyphenate.easeui.R.id.tv_name));
        tv_kind = ((TextView) findViewById(com.hyphenate.easeui.R.id.tv_kind));
        iv_icon = ((ImageView) findViewById(com.hyphenate.easeui.R.id.iv_icon));


    }

    /**
     * 消息状态改变，刷新listview
     */
    @Override
    protected void onUpdateView() {

    }


    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        priceStr = message.getStringAttribute("price", null);
        nameStr = message.getStringAttribute("name", null);
        idStr = message.getStringAttribute("infoId", null);
        iconStr = message.getStringAttribute("icon", null);
        kindStr = message.getStringAttribute("carTpye", null);

        if (Purchase.isPurchaseMessage(message)) {
            //求购消息

            tv_price.setText(priceStr);
            tv_name.setText(nameStr);
            tv_kind.setText(getResources().getString(com.minfo.carrepairseller.R.string.purchase_kind));
            if (recIsTrue) {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.purchase_name_send));
            } else {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.purchase_name_receive));

            }
        } else if (Purchase.isPriceMessage(message)) {
            //报价消息
            tv_price.setText(priceStr);
            tv_name.setText(nameStr);
            tv_kind.setText(getResources().getString(com.minfo.carrepairseller.R.string.price_namekind));
            if (recIsTrue) {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.price_name_send));
            } else {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.price_name_receive));
            }
        } else if (Purchase.isOrderMessage(message)) {
            //开单消息
            tv_price.setText(priceStr);
            tv_name.setText(nameStr);
            tv_kind.setText(getResources().getString(com.minfo.carrepairseller.R.string.order_name_kind));
            if (recIsTrue) {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.order_name_send));
            } else {
                tv_title.setText(getResources().getString(com.minfo.carrepairseller.R.string.order_name_rceive));

            }
        }
//        else if (Purchase.isShopMessage(message)) {
//            //商品消息
//            tv_price.setText(priceStr);
//            tv_name.setText(kindStr);
//            tv_title.setText(nameStr);
//
//            tv_kind.setText(getResources().getString(com.minfo.carrepair.R.string.product_name_kind));
//
//        }


    }

    @Override
    protected void onBubbleClick() {

    }
}
