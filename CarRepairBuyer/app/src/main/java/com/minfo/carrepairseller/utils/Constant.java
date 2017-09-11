package com.minfo.carrepairseller.utils;

import java.util.HashMap;

/**
 * Created by liujing on 15/8/20.
 */
public class Constant {

    //版本信息的HashMap
    public static HashMap<String, String> updateMap = null;

    //版本是否需要更新的标识
    public static boolean isUpdate = false;

    public static String userid;

    public static String providerId;//服务商ID

    public static int initFragment;
    public static String psw;

    // 订单状态
    public final static String ORDER_STATE = "order_state"; // 0全部，1待付款，2待发货，3待收货，4待评价
    public final static String GOOD_STATE = "good_state"; // 0出售中，1已下架，2审核中，3审核不通过
    public final static String MESSAGE_TYPE = "message_type"; // 0最近，1配件商，2我的好友
    // 查询类型
    public final static String QUERY_TYPE = "query_type"; // 0配件查询，1OEM号查询

    public final static String CAR_TYPE = "chengxing"; // 车型



}
