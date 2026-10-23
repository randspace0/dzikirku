package id.gritlabs.dzikirku.ui.bsd.settings.presenter

import android.app.AlertDialog
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsPresenter
import id.gritlabs.dzikirku.util.praytimes.PrayTimeScript


fun SettingsPresenter.asrJuristic(){
    viewDataBinding.textAsrJuristic.setOnClickListener {
        showAsrJuristic()
    }
    viewDataBinding.textAsrJuristic.setSubtitle(asrJuristicTitle[Prefs.asrJuristic])
}

val asrJuristicTitle = arrayOf(
    "Shafi'i, Maliki, Hanbali (Standard)",
    "Hanafi"
)

fun SettingsPresenter.showAsrJuristic() {
    val prefs = Prefs
    val praytime = PrayTimeScript()
    val asrJuristic = arrayOf(
        praytime.Shafii,
        praytime.Hanafi,
    )
    AlertDialog.Builder(mContext)
        .setTitle(LocaleConstants.ASR_JURISTIC.locale())
        .setItems(asrJuristicTitle) { dialog, which ->
            prefs.asrJuristic = asrJuristic[which]
            configureViews()
        }.show()
}