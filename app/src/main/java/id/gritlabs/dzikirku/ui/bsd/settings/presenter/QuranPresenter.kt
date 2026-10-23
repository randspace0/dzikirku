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

fun SettingsPresenter.quran() {
    val prefs = Prefs
    viewDataBinding.markLastReadAutoCheckbox.isChecked = prefs.isMarkQuranLastReadAutomatically
    viewDataBinding.markLastReadAutoCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
        prefs.isMarkQuranLastReadAutomatically = isChecked
        RxBus.getDefault().send(SettingsEvent())
    }
    viewDataBinding.wbwCheckbox.isChecked = prefs.quranWbwEnabled
    viewDataBinding.wbwCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
        prefs.quranWbwEnabled = isChecked
        RxBus.getDefault().send(SettingsEvent())
    }

    viewDataBinding.textQuranReadMode.setOnClickListener {
        val modes = arrayOf(
            LocaleConstants.VERTICAL_MODE.locale(),
            LocaleConstants.PAGED_MODE.locale()
        )
        AlertDialog.Builder(mContext)
            .setTitle(LocaleConstants.DEFAULT_READ_MODE.locale())
            .setItems(modes) { dialog, which ->
                when (which) {
                    0 -> {
                        Prefs.defaultQuranReadMode = ReadModeConstants.VERTICAL
                    }
                    1 -> {
                        Prefs.defaultQuranReadMode = ReadModeConstants.PAGED
                    }
                }
                viewDataBinding.textQuranReadMode.updateReadModeText()
                RxBus.getDefault().send(SettingsEvent())
            }.show()
    }

    val fontTitle = arrayOf("Uthman Taha (Madinah)", "Indopak", "MeQuran", "Al Qalam")
    val font =
        arrayOf("fonts/uthman.otf", "fonts/indopak.ttf", "fonts/me_quran.ttf", "fonts/al_qalam.ttf")
    viewDataBinding.textArabicFont.setOnClickListener {
        AlertDialog.Builder(mContext)
            .setTitle(LocaleConstants.ARABIC_FONT.locale())
            .setItems(fontTitle) { dialog, which ->
                Prefs.arabicFont = font[which]
                viewDataBinding.textArabicFont.updateArabicFont()
                RxBus.getDefault().send(SettingsEvent())
            }.show()
    }
    viewDataBinding.textArabicFont.updateArabicFont()
    viewDataBinding.textQuranReadMode.updateReadModeText()
}

private fun TitleSubtitleView.updateReadModeText() {
    when (Prefs.defaultQuranReadMode) {
        ReadModeConstants.VERTICAL -> setSubtitle(LocaleConstants.VERTICAL_MODE.locale())
        ReadModeConstants.PAGED -> setSubtitle(LocaleConstants.PAGED_MODE.locale())
    }
}

private fun TitleSubtitleView.updateArabicFont() {
    when (Prefs.arabicFont) {
        "fonts/uthman.otf" -> setSubtitle("Uthman Taha (Madani)")
        "fonts/indopak.ttf" -> setSubtitle("Indopak")
        "fonts/me_quran.ttf" -> setSubtitle("MeQuran")
        else -> setSubtitle("Al Qalam")
    }
}