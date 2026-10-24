package id.gritlabs.dzikirku.ui.read.quran.listed

import id.gritlabs.dzikirku.util.fromJson


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.data.room.dao.AyahDao
import id.gritlabs.dzikirku.data.room.dao.SurahDao
import id.gritlabs.dzikirku.model.Ayah
import id.gritlabs.dzikirku.model.AyahWbw
import id.gritlabs.dzikirku.model.identifiers.BismillahObject
import id.gritlabs.dzikirku.model.identifiers.JuzObject
import id.gritlabs.dzikirku.model.identifiers.SurahObject
import id.gritlabs.dzikirku.ui.read.quran.AyahQuery
import id.gritlabs.dzikirku.ui.read.quran.AyahQueryType
import id.gritlabs.dzikirku.util.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuranListViewModel @Inject constructor(
    dataManager: AppDataManager,
    val ayahDao: AyahDao,
    val surahDao: SurahDao
) : BaseViewModel<BaseNavigator>(dataManager) {


    suspend fun getSurah(surahId:Int):List<Ayah> {
        return ayahDao.getAyahBySurahId(surahId)
    }

    fun getAyah(query: AyahQuery):LiveData<ArrayList<Any>>{
        return liveData {
            emit(getAyahData(query))
        }
    }

    private suspend fun getAyahData(query: AyahQuery): ArrayList<Any> {
        val wbw = FileUtils.getJsonStringFromAssets(
                dataManager.mContext,
                "json/quran/wbw/${query.id.toInt()}.json"
            ).fromJson<ArrayList<AyahWbw>>()
        val data = when (query.type) {
            AyahQueryType.Surah -> ayahDao.getAyahBySurahId(query.id.toInt())
            AyahQueryType.Juz -> ayahDao.getAyahByJuz(query.id.toInt(), 9999)
            AyahQueryType.Hizb -> ayahDao.getAyahByHizb(query.id, 9999)
        }
        return arrayListOf<Any>().apply {
            var currentJuz = -1
            data.forEach {
                if (currentJuz != it.juz){
                    currentJuz = it.juz
                    if (dataManager.isFirstAyahOfJuz(it.juz, it.chapterId, it.verse_number)){
                        add(JuzObject(it.juz))
                    }
                }
                if (it.verse_number == 1) {
                    add(SurahObject(surahDao.getSurahById(it.chapterId).first()))
                }
                if (it.use_bismillah == true) {
                    add(BismillahObject())
                }
                add(it.apply {
                    this.wbw.addAll(wbw.filter { it.ayah == this.verse_number })
                })
            }
        }
    }

    override fun onEvent(obj: Any) {
    }

}