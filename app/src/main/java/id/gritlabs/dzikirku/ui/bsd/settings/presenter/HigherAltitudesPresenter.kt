package id.gritlabs.dzikirku.ui.bsd.settings.presenter

import android.app.AlertDialog
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsPresenter
import id.gritlabs.dzikirku.util.praytimes.PrayTimeScript

val higherTitle = arrayOf(
    LocaleConstants.NONE,
    LocaleConstants.MIDNIGHT,
    LocaleConstants.ONE_SEVENTH,
    LocaleConstants.ANGLE_BASED,
)

fun SettingsPresenter.higherAltitudes(){
    viewDataBinding.textHigherAltitudes.setOnClickListener {
        showHigherLatitudes()
    }
    viewDataBinding.textHigherAltitudes.setSubtitle(higherTitle[Prefs.higherLatitudes].locale())
}

fun SettingsPresenter.showHigherLatitudes() {
    val prefs = Prefs
    val praytime = PrayTimeScript()
    val higher = arrayOf(
        praytime.None,
        praytime.MidNight,
        praytime.OneSeventh,
        praytime.AngleBased,
    )
    AlertDialog.Builder(mContext)
        .setTitle(LocaleConstants.ADJUSTING_METHODS_FOR_HIGHER_LATITUDES.locale())
        .setItems(higherTitle) { dialog, which ->
            prefs.higherLatitudes = higher[which]
            configureViews()
        }.show()
}