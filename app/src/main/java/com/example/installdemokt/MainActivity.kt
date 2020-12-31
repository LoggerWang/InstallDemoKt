package com.example.installdemokt

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.medkey.android.getAppFolder
import java.io.File
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    /** 更新apk存储路径 */
    var UPDATE_APK_URL:String = getAppFolder()?.path.toString() + File.separator + "apk"
    var UPDATE_APK_NAME:String = "medkey.apk"

    /** apk文件路径 */
    var apkfile = UPDATE_APK_URL + File.separator + UPDATE_APK_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkExtrnalStorage()
        findViewById<TextView>(R.id.tvInstall).setOnClickListener {
            installApk()
        }
    }

    /**
     * 检查外部scard读写权限
     */
    private fun checkExtrnalStorage() {
        //首先判断用户手机的版本号 如果版本大于6.0就需要动态申请权限
        //如果版本小于6.0就直接去扫描二维码
        if (Build.VERSION.SDK_INT > 23) {
            //说明是android6.0之前的
            //1.定义一个数组，用来装载申请的权限
            val permissons = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            //2.判断这些权限有没有申请，没有申请的话，就把没有申请的权限放到一个数组里面
            val deniedPermissions = ArrayList<String>()
            var authSum = 0
            for (permission in permissons) {
                val i: Int? = this?.let { ContextCompat.checkSelfPermission(it, permission) }
                if (PackageManager.PERMISSION_DENIED == i) {
                    //说明权限没有被申请
                    deniedPermissions.add(permission)
                }else{
                    authSum += 1
                }
            }
            if (deniedPermissions.size == 0) {
                //说明是android6.0之前的
//                currentClickItem?.let { checkFileExist(clickPos, it) }
                return
            }
            //当你不知道数组多大的时候，就可以先创建一个集合，然后调用集合的toArray方法需要传递一个数组参数，这个数组参数的长度
            //设置成跟集合一样的长度
            val strings = deniedPermissions.toTypedArray()
            //3.去申请权限
            this?.let { ActivityCompat.requestPermissions(it, strings, 1) }
        }else{
//            currentClickItem?.let { checkFileExist(clickPos, it) }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){

                }
            }
            else -> {
            }
        }
    }


    /**
     * 安装应用
     */
    fun installApk() {
        var file =  File(apkfile)
        if (file.exists()) {
            var intent =  Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                var authority = getString(R.string.authorityOfAppPackageProvider)
                Log.e("installApk", "authority===" + authority);
                var apkUri = FileProvider.getUriForFile(this, authority, file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            startActivity(intent)
        }else{

        }
    }
}