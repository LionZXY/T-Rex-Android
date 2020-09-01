package com.lionzxy.trex_progress

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.lionzxy.trex_library.IErrorListener
import com.lionzxy.trex_library.IProgressListener
import com.lionzxy.trex_library.ITRexGameView
import com.lionzxy.trex_library.TRexGameView

class TRexProgressView(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int,
    private val gameView: TRexGameView,
    private val progressView: ProgressBar,
    private val errorText: TextView
) : FrameLayout(context, attrs, defStyleAttr), IProgressListener, IErrorListener,
    ITRexGameView by gameView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        TRexGameView(context, attrs, defStyleAttr),
        ProgressBar(context),
        TextView(context)
    ) {
        gameView.visibility = View.GONE
        gameView.addProgressListener(this)
        addView(gameView)
        progressView.layoutParams = LayoutParams(150.px, 150.px, CENTER)
        addView(progressView)
        errorText.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, CENTER)
        errorText.setText(R.string.game_error_progress)
        errorText.visibility = View.GONE
        addView(errorText)
    }

    override fun showProgress(shown: Boolean) {
        if (shown) {
            progressView.visibility = View.VISIBLE
            gameView.visibility = View.GONE
            errorText.visibility = View.GONE
        } else {
            progressView.visibility = View.GONE
            gameView.visibility = View.VISIBLE
            errorText.visibility = View.GONE
        }
    }

    override fun onError() {
        progressView.visibility = View.GONE
        gameView.visibility = View.GONE
        errorText.visibility = View.VISIBLE
    }

    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}