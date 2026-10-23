package id.gritlabs.dzikirku.ui.bsd.settings.presenter

import android.app.AlertDialog
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.constants.ReadModeConstants
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.model.events.SettingsEvent
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsPresenter
import id.gritlabs.dzikirku.util.RxBus
import id.gritlabs.dzikirku.util.TitleSubtitleView

fun SettingsPresenter.hadith(){
    viewDataBinding.textHadithReadMode.setOnClickListener {
        val modes = arrayOf(
            LocaleConstants.VERTICAL_MODE.locale(),
            LocaleConstants.PAGED_MODE.locale()
        )
        AlertDialog.Builder(mContext)
            .setTitle(LocaleConstants.DEFAULT_READ_MODE.locale())
            .setItems(modes) { dialog, which ->
                when (which) {
                    0-> {
                        Prefs.defaultHadithReadMode = ReadModeConstants.VERTICAL
                    }
                    1 -> {
                        Prefs.defaultHadithReadMode = ReadModeConstants.PAGED
                    }
                }
                viewDataBinding.textHadithReadMode.updateReadModeText()
                RxBus.getDefault().send(SettingsEvent())
            }.show()
    }
    viewDataBinding.textHadithReadMode.updateReadModeText()
}


private fun TitleSubtitleView.updateReadModeText(){
    when (Prefs.defaultHadithReadMode) {
        ReadModeConstants.VERTICAL -> setSubtitle(LocaleConstants.VERTICAL_MODE.locale())
        ReadModeConstants.PAGED -> setSubtitle(LocaleConstants.PAGED_MODE.locale())
    }
}