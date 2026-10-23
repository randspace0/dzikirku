package id.gritlabs.dzikirku.ui.search.prayer


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.Prayer
import id.gritlabs.dzikirku.model.events.SearchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchPrayerViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is SearchEvent ->{
                query.value = obj.query
                isNoteDeeplink = obj.isNoteDeeplinkEvent == true
            }
        }
    }

    var isNoteDeeplink = false
    val query = MutableLiveData("")
    val result = query.switchMap { query->
        liveData<List<Prayer>>{
            if (query.isNullOrBlank().not()){
                val result = dataManager.mPersistenceDatabase.prayerDao().searchPrayer("%$query%")
                emit(result)
            } else{
                emit(arrayListOf())
            }
        }
    }


}