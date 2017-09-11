package com.minfo.carrepairseller.utils;

/**
 * Created by MinFo021 on 17/5/15.
 */

public class ConstantUrl {

    public static String URL_HOME_PAGE = "goods/Homepage.php"; // 首页接口
    public static String URL_CATEGORY = "type/Gtype.php"; // 分类接口
    public static String URL_TYPE_COUNTRY = "type/Country.php"; // 国家分类接口

    public static String URL_PRODUCT_LIST = "goods/List.php"; // 产品列表接口
    public static String URL_SHOPPING_CART = "order/AddFromCart.php"; // 购物车列表接口
    public static String URL_PRODUCT_DETAIL = "goods/Detail.php"; // 产品详情接口

    public static String URL_ADD_CART = "cart/Add.php"; // 添加购物车
    public static String URL_CART_LIST = "cart/List.php"; // 购物车列表
    public static String URL_CART_CHANGE = "cart/Change.php"; // 购物车改变商品数

    public static String URL_DETAIL_BUY = "order/AddFromDetail.php"; // 详情购买接口

    public static String URL_USER_KEFU = "kefu/List.php"; // 客服
    public static String URL_USER_PERSONAL = "user/Main.php"; // 个人中心

    public static String URL_USER_FANDIAN = "user/BackInfo.php"; // 返点
    public static String URL_USER_FANDIAN_DETAIL = "user/BackInfo.php"; // 返点列表
    public static String URL_USER_FANDIAN_TO_WALLET = "user/BackSub.php"; // 返点到钱包
    public static String URL_USER_WALLET = "user/wallet.php"; // 钱包
    public static String URL_USER_WALLET_DETAIL = "user/TxList.php"; // 钱包明细
    public static String URL_USER_WALLET_TX_TOWX = "user/txToWx.php"; // 钱包提现到微信
    public static String URL_USER_WALLET_TX_ALIPAY = "user/txToZfb.php"; // 钱包提现到支付宝

    public static String URL_ORDER_LIST = "order/List.php"; // 订单列表
    public static String URL_ORDER_MAKE_SURE = "order/MakeSure.php"; // 确认订单接口
    public static String URL_ORDER_DATIAL = "order/Detail.php"; // 订单详情接口
    public static String URL_ORDER_CANCLE = "order/Delete.php"; // 取消订单接口
    public static String URL_ORDER_BEFORE_ALIPAY = "order/ZfbPay.php"; // 支付宝支付
    public static String URL_ORDER_YUE_PAY = "order/YePay.php"; // 余额支付

    // 求购流程
    public static String URL_QG_ORDER_ADD = "qgorder/AddToOrder.php"; // 下单接口
    public static String URL_QG_ORDER_MAKE_SURE = "qgorder/MakeSure.php"; // 确认订单接口
    public static String URL_QG_ORDER_DATIAL = "qgorder/Detail.php"; // 订单详情接口
    public static String URL_QG_ORDER_CANCLE = "qgorder/Delete.php"; // 取消订单接口
    public static String URL_QG_ORDER_BEFORE_ALIPAY = "qgorder/ZfbPay.php"; // 支付宝支付
    public static String URL_QG_ORDER_YUE_PAY = "qgorder/YePay.php"; // 余额支付
    public static String URL_QG_ORDER_SHOUHUO = "qgorder/GetGoods.php"; // 确认收货
    public static String URL_QG_ORDER_WL = "qgorder/SeeWl.php"; // 查看物流

    public static String URL_SETTING_CHANGE_NAME = "user/Setname.php"; // 修改昵称
    public static String URL_SETTING_CHANGE_PASSWORD = "user/EditPsw.php"; // 修改密码

    public static String URL_ADDRESS_LIST = "user/AddressList.php"; // 收货地址列表
    public static String URL_ADDRESS_DEFAULT = "user/AddressDefault.php"; // 设置默认收货地址
    public static String URL_ADDRESS_DELETE = "user/AddressDelete.php"; // 删除收货地址
    public static String URL_ADDRESS_EDIT = "user/AddressEdit.php"; // 编辑收货地址
    public static String URL_ADDRESS_ADD = "user/AddressAdd.php"; // 添加收货地址user/AddressAdd.php

    public static String URL_USER_LOGIN_OTHER = "user/LoginByDsf.php"; // 登录
    public static String URL_USER_LOGIN = "user/Login.php"; // 登录
    public static String URL_USER_INIT = "user/Init.php"; // 验证登录
    public static String URL_USER_REGISTER = "user/Apply.php"; // 注册
    public static String URL_GOODS_SEARCH = "goods/Screen.php"; // 查询
    public static String URL_DUANXIN_GETCODE = "duanxin/SendCode.php"; // 获取验证码
    public static String URL_USER_ZHANGDAN = "user/wRecord.php"; // 账单


//    public static String URL_ORDER_LIST = "order/List.php"; // 订单列表
//    public static String URL_ORDER_LIST = "order/List.php"; // 订单列表
    public static String URL_GOOD_MANAGER = ""; // 商品管理
    public static String URL_GOOD_MANAGER_DEL_GOOD = "DEL"; // 删除商品
    public static String URL_GOOD_MANAGER_SELLING= "SELLING"; // 上架商品
    public static String URL_GOOD_MANAGER_LAID = "LAID"; // 下架商品

    public static String URL_MY_MESSAGE_PJS = "pjs/pjsList.php"; // 配件商列表
    public static String URL_MY_MESSAGE_HY = "addfriend/friendsList.php"; // 好友列表
    public static String URL_MY_MESSAGE_ADD_FRIEND = "addfriend/add.php"; // 添加好友

    public static String URL_CAR_PINPAI_LIST = "car/Brand.php"; // 品牌列表
    public static String URL_CAR_CHEXI_LIST = "car/Serial.php"; // 车系列表
    public static String URL_CAR_CHEXING_LIST = "car/Car.php"; // 车型列表


    public static String URL_QUERY_PEIJIAN = "cha/Pjian.php"; // 配件查询
    public static String URL_QUERY_OEM= "cha/Oem.php"; // OEM号查询
    public static String URL_QUERY_CHEJIA_VIN = "cha/Vin.php"; // 车架号查询
    public static String URL_QUERY_EPC_LEFT = "cha/EpcA.php"; // EPC结构图标题
    public static String URL_QUERY_EPC_CONTETN = "cha/EpcB.php"; // EPC结构图
    public static String URL_QUERY_EPC_DETAIL = "cha/EpcDetail.php"; // EPC结构图详情
    public static String URL_QUERY_OEM_DETAIL = "cha/pjDetail.php"; // OEM配件详情

    public static String URL_CENTER_TIAOWEN = "user/law.php"; // 条纹中心

//    user/collect.php
    public static String URL_USER_COLLECT = "user/collect.php"; // 我的收藏

    public static String URL_GOODS_SHOP = "goods/Shop.php"; // 店铺

}
