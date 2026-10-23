package id.gritlabs.dzikirku.ui.hadithchapter

import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.events.NoteInsertEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HadithChapterActivityViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<BaseNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
        when(obj){
            is NoteInsertEvent->{
                navigator?.finish()
            }
        }
    }
}