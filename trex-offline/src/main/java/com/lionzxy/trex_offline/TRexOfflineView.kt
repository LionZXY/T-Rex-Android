package com.lionzxy.trex_offline

import android.content.Context
import android.util.AttributeSet
import com.lionzxy.trex_library.TRexGameView
import java.lang.ref.WeakReference


class TRexOfflineView : TRexGameView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setProvider(OfflineTRexProvider(WeakReference(context)))
    }
}