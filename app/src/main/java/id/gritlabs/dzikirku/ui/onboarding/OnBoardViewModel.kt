package id.gritlabs.dzikirku.ui.onboarding

import androidx.lifecycle.MutableLiveData
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.data.room.dao.*
import id.gritlabs.dzikirku.model.events.OnBoardEvent
import id.gritlabs.dzikirku.model.events.OnBoardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OnBoardViewModel @Inject constructor(
    dataManager: AppDataManager,
    val bookDao: PrayerBookDao,
    val ayahDao: AyahDao,
    val surahDao: SurahDao,
    val prayerDao: PrayerDao,
    val prayerDataDao: PrayerDataDao
) :
    BaseViewModel<OnBoardNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        if(obj is OnBoardEvent){
            when(obj.type){
                OnBoardType.LANGUAGE -> page.value = 0
                OnBoardType.LOCATION -> page.value = 1
                OnBoardType.PRAYTIME -> page.value = 2
            }
        }
    }

    val page = MutableLiveData(0)

}

















