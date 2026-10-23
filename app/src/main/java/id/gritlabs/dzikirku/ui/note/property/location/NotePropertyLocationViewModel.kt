package id.gritlabs.dzikirku.ui.note.property.location

import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotePropertyLocationViewModel @Inject constructor(dataManager: AppDataManager): BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {

    }

    val locations = dataManager.mNoteDatabase.notePropertyDao().getNoteProperties()

}