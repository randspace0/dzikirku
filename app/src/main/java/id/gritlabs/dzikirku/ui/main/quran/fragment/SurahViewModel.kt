package id.gritlabs.dzikirku.ui.main.quran.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.data.room.dao.SurahDao


import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SurahViewModel @Inject constructor(
    dataManager: AppDataManager,
    val surahDao: SurahDao
) :
    BaseViewModel<BaseNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    val isLoading = MutableLiveData(true)

    val surah = liveData {
        emit(dataManager.mPersistenceDatabase.surahDao().getSurah())
    }


}