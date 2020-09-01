package com.lionzxy.t_rexgamelibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lionzxy.trex_offline.TRexOfflineActivity
import com.lionzxy.trex_progress.TRexPlayActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*trex.addProgressListener {
            swipeToRefresh.isRefreshing = it
        }
        swipeToRefresh.setOnRefreshListener {
            trex.refresh()
        }
        open_activity.setOnClickListener {
            TRexOfflineActivity.open(this)
        }*/
    }
}