package id.gritlabs.dzikirku.ui.jump

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.data.room.dao.SurahDao
import id.gritlabs.dzikirku.model.Surah
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JumpQuranViewModel @Inject constructor(dataManager: AppDataManager, surahDao: SurahDao) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }

    val query = MutableLiveData<String>()
    val surah = query.switchMap { query->
        liveData {
            val surah = surahDao.getSurah()
            if (query.isNotBlank()){
                val firstQuery = query.split(" ")[0].lowercase()
                emit(arrayListOf<Surah>().apply {
                    addAll(surah.filter { it.name.filter(Char::isLetter).lowercase().contains(query.split(" ")[0].lowercase()) })
                    addAll(surah.filter { it.name.lowercase().contains(query.split(" ")[0].lowercase()) })
                    firstQuery.toIntOrNull()?.let { queryId->
                        addAll(surah.filter { it.id == queryId })
                    }
                }.distinctBy { it.id })
            }
        }
    }

    fun isHasSecondValue():Boolean{
        return query.value?.split(" ")?.count() ?: 0 > 1
    }

    fun isSecondValueNumber():Boolean{
        return query.value?.split(" ")?.get(1)?.filter(Char::isDigit).isNullOrEmpty().not()  && query.value?.split(" ")?.get(1)?.filter(Char::isLetter).isNullOrEmpty()
    }

    fun getSecondNumber():Int{
        return query.value?.split(" ")?.get(1)?.filter(Char::isDigit).toString().toInt()
    }

}