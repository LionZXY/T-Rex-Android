package com.lionzxy.trex_offline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class TRexOfflineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TRexOfflineView(this))
    }

    companion object {
        fun open(ctx: Context) {
            ctx.startActivity(Intent(ctx, TRexOfflineActivity::class.java))
        }
    }
}