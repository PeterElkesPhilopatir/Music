package com.peter.music.service.image_load

import android.content.Context
import android.os.Environment
import java.io.File

class FileCache(context: Context) {
    private var cacheDir: File? = null
    fun getFile(url: String): File {
        val filename = url.hashCode().toString()
        return File(cacheDir, filename)
    }

    fun clear() {
        val files: Array<File> = cacheDir!!.listFiles() ?: return
        for (f in files) f.delete()
    }

    init {
        //Find the dir to save cached images
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) cacheDir = File(
            Environment.getExternalStorageDirectory(), "TempImages"
        ) else cacheDir = context.getCacheDir()
        if (!cacheDir!!.exists()) cacheDir!!.mkdirs()
    }
}