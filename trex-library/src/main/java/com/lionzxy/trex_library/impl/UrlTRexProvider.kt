package com.lionzxy.trex_library.impl

import android.content.Context
import android.util.Log
import com.lionzxy.trex_library.ITRexZipProvider
import java.io.File
import java.lang.ref.WeakReference
import java.net.URL

private const val TAG = "UrlTRexProvider"

class UrlTRexProvider(private val url: String,
                      context: WeakReference<Context>
): ITRexZipProvider {
    private val tRexZipFile = context.get()?.let { File(it.cacheDir, "tRexFile.zip") }

    override fun getTRexZip(): File {
        downloadZip()

        return tRexZipFile!!
    }

    override fun dropCache() {
        tRexZipFile?.deleteRecursively()
    }

    private fun downloadZip() {
        Log.d(TAG, "Start download zip")
        if (tRexZipFile!!.exists()) {
            Log.d(TAG, "Zip already download")
            return
        }
        tRexZipFile.createNewFile()

        URL(url).openStream().use { downloadStream ->
            tRexZipFile.outputStream().use { fileStream ->
                downloadStream.copyTo(fileStream)
            }
        }
    }

    override fun toString(): String {
        return "TRex provider with url $url"
    }
}
