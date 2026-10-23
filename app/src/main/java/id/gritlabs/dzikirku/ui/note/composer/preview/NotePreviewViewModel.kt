package id.gritlabs.dzikirku.ui.note.composer.preview

import androidx.lifecycle.MutableLiveData
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.events.NoteEditorEvent
import id.gritlabs.dzikirku.model.events.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotePreviewViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
        when(obj){
            is NoteEditorEvent->{
                content.value = obj.content
            }
            is SettingsEvent ->{
                navigator?.onSettingsEvent()
            }
        }
    }

    val content = MutableLiveData("")

}