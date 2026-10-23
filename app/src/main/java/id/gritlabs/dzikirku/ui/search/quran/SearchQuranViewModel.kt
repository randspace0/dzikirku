package id.gritlabs.dzikirku.ui.search.quran

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.events.SearchEvent
import id.gritlabs.dzikirku.model.search.SearchQuranModel
import id.gritlabs.dzikirku.util.StringExt.getText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchQuranViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is SearchEvent->{
                query.value = obj.query
            }
        }
    }

    val query = MutableLiveData("")
    val result = query.switchMap { query->
        liveData<List<SearchQuranModel>>{
            if (query.isNullOrBlank().not()){
                val result = dataManager.mPersistenceDatabase.ayahDao().searchAyah("%$query%").filter {
                    it.text.getText().lowercase().contains(query.lowercase())
                }
                val chapterId = result.map {
                    it.chapterId
                }.toSet()
                emit(chapterId.map { id->
                    SearchQuranModel(id, result.filter { id == it.chapterId })
                })
            } else{
                emit(arrayListOf())
            }
        }
    }

}