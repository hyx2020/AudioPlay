package org.hyx.lib_base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by MrHuang on 2020/12/23 10:11.
 */

class FunShare {
    companion object {
        val schedule = ScheduledThreadPoolExecutor(10, ShareThreadFactory())

        fun getFileCache(context: Context): String {
            return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
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

        fun save(data: ArrayList<FloatArray>, context: Context) {
            var fw: RandomAccessFile? = null
            var file = File(getFileCache(context) + "/record")
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
            var size = 0;
            for (i in data.indices) {
                size += data[i].size
                val writeBuf = data[i].contentToString().replace("[", "").replace("]", "").replace(" ", "") + ","
                fw?.writeBytes(writeBuf)
            }
            println("写入结束, 共计$size")
            try {
                fw?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun getAndPlay(context: Context): FloatArray {
            var buffer: FloatArray? = null
            try {
                val inputStreamReader = InputStreamReader(context.assets.open("send.txt"))
                val bf = BufferedReader(inputStreamReader)
                var str: String
                if (bf.readLine().also { str = it } != null) {
                    val node = str.split(",".toRegex()).toTypedArray()
                    buffer = FloatArray(node.size)
                    for (i in node.indices) {
                        buffer[i] = node[i].toFloat()
                    }
                }
            } catch (e: Exception) {
                // TODO: handle exception
            }

            return buffer!!
        }
    }

    class ShareThreadFactory : ThreadFactory {
        private val atomicInteger = AtomicInteger()
        override fun newThread(r: Runnable): Thread {
            atomicInteger.incrementAndGet()
            return ShareThread(r, atomicInteger)
        }
    }

    class ShareThread internal constructor(private val target: Runnable, private val integer: AtomicInteger) : Thread() {
        private val tag = this.name
        override fun run() {
            target.run()
            Log.d(tag, integer.getAndDecrement().toString() + "run over")
        }
    }
}