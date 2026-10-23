package id.gritlabs.dzikirku.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import id.gritlabs.dzikirku.util.praytimes.Praytime


class AutoStartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Praytime.schedule(context)
    }
}
