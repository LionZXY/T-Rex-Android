package com.lionzxy.trex_progress

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class TRexPlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TRexProgressView(this))
    }

    companion object {
        fun open(ctx: Context) {
            ctx.startActivity(Intent(ctx, TRexPlayActivity::class.java))
        }
    }
}