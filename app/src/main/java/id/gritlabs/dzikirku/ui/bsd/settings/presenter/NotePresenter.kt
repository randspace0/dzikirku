package id.gritlabs.dzikirku.ui.bsd.settings.presenter

import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.model.events.SettingsEvent
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsPresenter
import id.gritlabs.dzikirku.util.RxBus

fun SettingsPresenter.note() {
    viewDataBinding.notesWysiwyg.isChecked = Prefs.noteWYSIWYGEnabled
    viewDataBinding.notesWysiwyg.setOnCheckedChangeListener { buttonView, isChecked ->
        Prefs.noteWYSIWYGEnabled = isChecked
        RxBus.getDefault().send(SettingsEvent())
    }
}