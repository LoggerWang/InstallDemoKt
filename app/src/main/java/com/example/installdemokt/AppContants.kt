package com.medkey.android

import android.os.Environment
import com.medkey.android.AppContacts.APP_FOLDER_NAME
import java.io.File

object AppContacts {
    const val APP_FOLDER_NAME = "medkey"
}


fun getAppFolder(): File? {
    return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        val appFolder = File(Environment.getExternalStorageDirectory(),APP_FOLDER_NAME)
        if (fileIsExists(appFolder.path)) {
            return appFolder
        }else{
            return createOnNotFound(appFolder)
        }
    } else {
        return Environment.getExternalStorageDirectory()
    }
}


  fun createOnNotFound(folder: File?): File? {
    if (folder == null) {
        return null
    }
    if (!folder.exists()) {
        folder.mkdirs()
    }
    return if (folder.exists()) {
        folder
    } else {
        null
    }
}

/**
 * 判断文件是否存在
 *
 * @param url 文件url
 * @return
 */
fun fileIsExists(url: String?): Boolean {
    try {
        val f = File(url)
        if (!f.exists()) {
            return false
        }
    } catch (e: Exception) {
        return false
    }
    return true
}