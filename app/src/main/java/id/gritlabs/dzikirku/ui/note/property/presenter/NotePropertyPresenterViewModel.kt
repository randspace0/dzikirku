package id.gritlabs.dzikirku.ui.note.property.presenter

import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotePropertyPresenterViewModel @Inject constructor(dataManager: AppDataManager): BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {

    }

    val presenters = dataManager.mNoteDatabase.notePropertyDao().getNoteProperties()
}