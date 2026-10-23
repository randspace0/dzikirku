package id.gritlabs.dzikirku.ui.dailyreminder

import androidx.lifecycle.MutableLiveData
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.model.QuranLastRead
import id.gritlabs.dzikirku.model.events.MainTabEvent
import id.gritlabs.dzikirku.model.events.MainTabType
import id.gritlabs.dzikirku.model.events.MenuEvent
import id.gritlabs.dzikirku.util.PraytimeWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyReminderViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<DailyReminderNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is MainTabEvent-> tab.value = obj.type
            is QuranLastRead-> {
                CoroutineScope(IO).launch {
                    if (obj.surah == 0) return@launch
                    if (obj.isSavedFromPage == true){
                        dataManager.mPersistenceDatabase.quranLastReadDao().deleteSavedFromPage()
                    }
                    dataManager.mPersistenceDatabase.quranLastReadDao().putQuranLastRead(obj)
                    PraytimeWidget.update(dataManager.mContext)
                }
            }
            is MenuEvent->{
                CoroutineScope(IO).launch {
                    dataManager.mPersistenceDatabase.quranLastReadDao().putQuranLastRead(Prefs.quranLastRead)
                }
            }
        }
    }

    val tab = MutableLiveData(MainTabType.HOME)


}