package com.bear.screenshot.model.config

/**
 * description:适配高版本Android系统静态变量，以及相关方法
 * author: bear .
 * Created date:  2019-06-25.
 * mail:2280885690@qq.com
 */
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class PermissionConst {
    companion object {
        val REQUEST_PERMISSION_STORY_WRITE = 1101//请求读权限

        val REQUEST_WRITE_CONSTACTS_PERMISSIONS = 1102//请求获取写入通讯录的权限
        val REQUEST_READ_PHONE_STATE_PERMISSIONS = 1103//请求获取写入通讯录的权限

        fun canUseThisPermission(
            mActivity: Activity,
            permissionName: String,
            permission: String,
            request: Int
        ): Boolean {
            val permissionCheck = ContextCompat.checkSelfPermission(mActivity, permission)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                    if (!mActivity.isFinishing) {
                        val builder = AlertDialog.Builder(mActivity)
                        builder.setTitle("权限设置")
                            .setMessage(
                                "请开通" + permissionName +
                                        "权限再使用该功能！"
                            )
                            .setPositiveButton("开启") { dialog, which ->
                                ActivityCompat.requestPermissions(mActivity!!, arrayOf(permission), request)
                                dialog.dismiss()
                            }
                            .setNegativeButton("取消", null).create().show()
                    }
                } else {
                    ActivityCompat.requestPermissions(mActivity!!, arrayOf(permission), request)
                }
                return false
            } else {
                return true
            }
        }

    }
}
