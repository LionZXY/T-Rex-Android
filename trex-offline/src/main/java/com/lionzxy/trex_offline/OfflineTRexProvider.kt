package com.lionzxy.trex_offline

import android.content.Context
import android.util.Log
import com.lionzxy.trex_library.ITRexZipProvider
import java.io.File
import java.lang.ref.WeakReference

private const val TAG = "OfflineTRexProvider"

class OfflineTRexProvider(private val context: WeakReference<Context>): ITRexZipProvider {
    private val tRexZipFile = context.get()?.let { File(it.cacheDir, "tRexFile.zip") }

    override fun getTRexZip(): File {
        if (tRexZipFile!!.exists()) {
            Log.v(TAG, "Zip already exist")
            return tRexZipFile
        }

        context.get()!!.resources.openRawResource(R.raw.trex).use {resFile ->
            tRexZipFile.outputStream().use {zipFile ->
                resFile.copyTo(zipFile)
            }
        }
        return tRexZipFile
    }

    override fun dropCache() {
        tRexZipFile?.deleteRecursively()
    }
}