package org.hyx.lib_base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by MrHuang on 2020/12/23 10:11.
 */

class FunShare {
    companion object {
        fun getFileCache(context: Context): String {
            return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                    || !Environment.isExternalStorageRemovable()) {
                context.externalCacheDir!!.path
            } else {
                context.cacheDir.path
            }
        }

        fun stampToDate(time: Long): String {
            val date = Date(time)
            @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return simpleDateFormat.format(date)
        }

        fun save(data: FloatArray, context: Context) {
            var fw: RandomAccessFile? = null
            var file: File = File(getFileCache(context) + "/record")
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw RuntimeException("创建文件夹失败")
                }
            }

            val fileName: String = getFileCache(context) + "/record/record.txt"
            file = File(fileName)
            try {
                if (file.exists()) {
                    if (file.delete()) if (file.createNewFile()) fw = RandomAccessFile(file, "rw")
                } else if (file.createNewFile()) fw = RandomAccessFile(file, "rw")
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var writeBuf = data.contentToString()
            writeBuf.replace("[", "");
            writeBuf.replace("]", "");
            writeBuf.replace(" ", "");
            fw?.writeBytes(writeBuf)
            println("写入结束")
            try {
                fw?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}