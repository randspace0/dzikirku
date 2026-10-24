package id.gritlabs.dzikirku.util.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import id.gritlabs.dzikirku.util.BooleanUtil.isLocationGranted
import id.gritlabs.dzikirku.util.PraytimeWidget
import id.gritlabs.dzikirku.util.praytimes.Praytime

class TickReceiver:BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Praytime.schedule(p0)
        PraytimeWidget.update(p0)
    }
}