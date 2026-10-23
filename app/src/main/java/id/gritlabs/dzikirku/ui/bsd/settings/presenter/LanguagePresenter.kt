package id.gritlabs.dzikirku.ui.bsd.settings.presenter

import android.app.AlertDialog
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.ui.SplashActivity
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsActivity
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsPresenter
import id.gritlabs.dzikirku.ui.main.MainActivity

fun SettingsPresenter.language(){
    viewDataBinding.textLanguage.setOnClickListener {
        showLanguage()
    }
}

fun SettingsPresenter.showLanguage() {
    val prefs = Prefs
    val languages = arrayOf("english", "bahasa")
    val languageTitle = arrayOf("English", "Indonesia")
    AlertDialog.Builder(mContext)
        .setTitle(LocaleConstants.LANGUAGE.locale())
        .setItems(languageTitle) { dialog, which ->
            prefs.language = languages[which]
            TaskStackBuilder.create(mContext)
                .addNextIntent(Intent(mContext, SplashActivity::class.java))
                .startActivities()
        }.show()
}
