package com.lionzxy.trex_library.internal

import android.content.Context
import android.util.Log
import com.lionzxy.trex_library.ITRexZipProvider
import java.io.File
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

private const val TAG = "TRexDownloadHelper"

class DownloadAndUnpackHelper(
    var tRexZipProvider: ITRexZipProvider,
    context: WeakReference<Context>
) : Runnable {
    private var downloadListener: WeakReference<TRexDownloadListener> = WeakReference(null)
    private var thread: Thread? = null
    private val tRexFolder = context.get()?.let { File(it.cacheDir, "tRexFolder") }

    fun getFolderWithTRex(downloadListener: TRexDownloadListener, forceUpdate: Boolean = false, ) {
        this.downloadListener = WeakReference(downloadListener)
        if (forceUpdate) {
            tRexZipProvider.dropCache()
            tRexFolder?.deleteRecursively()
        }
        if (thread?.isAlive == true) {
            Log.e(TAG, "Already run thread with zip file request")
            thread?.interrupt()
        }
        thread = Thread(this)
        thread?.start()
    }

    override fun run() {
        if (tRexFolder?.exists() == true) {
            Log.d(TAG, "Folder alredy exist")
            report(tRexFolder)
            return
        }
        if (tRexFolder == null) {
            Log.e(TAG, "Folder is null! Internal error")
            report(null)
            return
        }

        if (Thread.interrupted()) {
            report(null)
            return
        }

        val zipFile = try {
            tRexZipProvider.getTRexZip()
        } catch (ex: Exception) {
            tRexZipProvider.dropCache()
            Log.e(TAG, "Error while download zip archive from provider: $tRexZipProvider", ex)
            report(null, ex)
            return
        }

        if (Thread.interrupted()) {
            report(null)
            return
        }

        try {
            unpackZip(zipFile)
        } catch (ex: Exception) {
            Log.e(
                TAG,
                "Error while extract $zipFile in ${tRexFolder.absoluteFile}",
                ex
            )
            report(null, ex)
            return
        }

        report(tRexFolder)
    }

    private fun report(file: File?, ex: Exception? = null) {
        downloadListener.get()?.onFile(file?.findIndexHtmlFolder() ?: file)
    }



    private fun unpackZip(zipFile: File) {
        Log.d(TAG, "Start unpack zip")
        if (!zipFile.exists()) {
            error("Not found zip archive for unpack")
        }

        tRexFolder!!.mkdirs()

        zipFile.inputStream().use { fileInputStream ->
            val zis = ZipInputStream(fileInputStream)
            var entry = zis.nextEntry
            while (entry != null) {
                zis.unpackZipEntry(tRexFolder, entry)
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
        Log.v(TAG, "Done extract zip")
    }

    private fun ZipInputStream.unpackZipEntry(folder: File, entry: ZipEntry) {
        if (Thread.interrupted()) {
            throw InterruptedException()
        }

        val file = File(folder, entry.name)
        if (entry.isDirectory) {
            file.mkdirs()
            return
        }

        file.createNewFile()
        file.outputStream().use { fos ->
            copyTo(fos)
        }
    }

    private fun File.findIndexHtmlFolder(): File? {
        if (!isDirectory) {
            return null
        }
        if (listFiles()?.find { it.name == "index.html" } != null) {
            return this
        }
        listFiles()?.forEach {
            val file = it.findIndexHtmlFolder()
            if (file != null) {
                return file
            }
        }
        return null
    }
}