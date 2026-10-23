package id.gritlabs.dzikirku.ui.bsd.quran.deeplink

import androidx.lifecycle.liveData
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.events.NoteInsertEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuranDeeplinkViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<QuranDeeplinkNavigator>(dataManager) {

    val surah = liveData {
        emit(dataManager.mPersistenceDatabase.surahDao().getSurah())
    }

    override fun onEvent(obj: Any) {
        if (obj is NoteInsertEvent){
            navigator?.onDeeplinkEvent()
        }
    }

}