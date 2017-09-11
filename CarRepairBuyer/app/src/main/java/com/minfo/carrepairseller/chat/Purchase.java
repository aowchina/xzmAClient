package com.minfo.carrepairseller.chat;

import com.hyphenate.chat.EMMessage;

/**
 * Created by min-fo-012 on 17/6/3.
 */
public class Purchase {
    public static boolean isPurchaseMessage(EMMessage message) {
        String TYPE = message.getStringAttribute(HuanUtils.MESSAGE_TYPE, null);
        if (TYPE == null) {
            return false;

        }
        if (TYPE.equals("purchaseInfo")) {
            message.getStringAttribute("icon", null);
            message.getStringAttribute("price", null);
            message.getStringAttribute("name", null);
            message.getStringAttribute("infoId", null);
            message.getStringAttribute("carTpye", null);


            return true;
        }
        return false;
    }
    public static boolean isOrderMessage(EMMessage message) {
        String TYPE = message.getStringAttribute(HuanUtils.MESSAGE_TYPE, null);
        if (TYPE == null) {
            return false;

        }
        if (TYPE.equals("orderInfo")) {
            message.getStringAttribute("icon", null);
            message.getStringAttribute("price", null);
            message.getStringAttribute("name", null);
            message.getStringAttribute("infoId", null);
            message.getStringAttribute("carTpye", null);


            return true;
        }
        return false;
    }

    public static boolean isPriceMessage(EMMessage message) {
        String TYPE = message.getStringAttribute(HuanUtils.MESSAGE_TYPE, null);
        if (TYPE == null) {
            return false;

        }
        if (TYPE.equals("priceInfo")) {
            message.getStringAttribute("icon", null);
            message.getStringAttribute("price", null);
            message.getStringAttribute("name", null);
            message.getStringAttribute("infoId", null);
            message.getStringAttribute("carTpye", null);


            return true;
        }
        return false;
    }


    public static boolean isShopMessage(EMMessage message) {
        String TYPE = message.getStringAttribute(HuanUtils.MESSAGE_TYPE, null);
        if (TYPE == null) {
            return false;

        }
        if (TYPE.equals("shopInfo")) {
            message.getStringAttribute("icon", null);
            message.getStringAttribute("price", null);
            message.getStringAttribute("name", null);
            message.getStringAttribute("infoId", null);
            message.getStringAttribute("carTpye", null);


            return true;
        }
        return false;
    }
}
