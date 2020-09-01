package com.lionzxy.trex_library

import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lionzxy.trex_library.impl.UrlTRexProvider
import com.lionzxy.trex_library.internal.DownloadAndUnpackHelper
import com.lionzxy.trex_library.internal.TRexDownloadListener
import java.io.File
import java.lang.ref.WeakReference

private const val TAG = "TRexView"

interface ITRexGameView {
    fun addProgressListener(listener: IProgressListener)
    fun setErrorListener(listener: IErrorListener)
    fun setUrl(url: String)
    fun setProvider(provider: ITRexZipProvider)
    fun refresh()
}

open class TRexGameView : WebView,
    TRexDownloadListener, ITRexGameView {
    private val downloadAndUnpackHelper = DownloadAndUnpackHelper(
        UrlTRexProvider(
            "https://github.com/LionZXY/t-rex-runner/archive/minify.zip",
            WeakReference(context)
        ),
        WeakReference(context)
    )
    private var progressListeners = ArrayList<IProgressListener>()
    private var errorListener: IErrorListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressListeners.forEach { it.showProgress(false) }
            }
        }
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.javaScriptEnabled = true
    }

    override fun addProgressListener(listener: IProgressListener) {
        this.progressListeners.add(listener)
    }

    override fun setErrorListener(listener: IErrorListener) {
        this.errorListener = listener
    }

    override fun setUrl(url: String) {
        setProvider(UrlTRexProvider(url, WeakReference(context)))
    }

    override fun setProvider(provider: ITRexZipProvider) {
        downloadAndUnpackHelper.tRexZipProvider = provider
    }

    override fun refresh() {
        progressListeners.forEach { it.showProgress(true) }
        downloadAndUnpackHelper.getFolderWithTRex(this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        progressListeners.forEach { it.showProgress(true) }
        downloadAndUnpackHelper.getFolderWithTRex(this)
    }

    override fun onFile(file: File?) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Log.v(TAG, "Execute processFolder in background thread. Fallback with handler")
            handler.post { onFile(file) }
            return
        }
        if (file == null) {
            errorListener?.onError()
            return
        }
        Log.v(TAG, "Download and unpack dino successful in path ${file.absolutePath}")
        val fileUrl = "file:///${file.absolutePath}/index.html"
        loadUrl(fileUrl)
    }
}