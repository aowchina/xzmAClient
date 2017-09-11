package com.minfo.carrepair.CommonInterface;

import java.util.List;

/**
 * Created by MinFo021 on 17/7/10.
 */

public interface PermissionListener {
    //授权成功
    void onGranted();

    //授权部分
    void onGranted(List<String> grantedPermission);

    //拒绝授权
    void onDenied(List<String> deniedPermission);
}
