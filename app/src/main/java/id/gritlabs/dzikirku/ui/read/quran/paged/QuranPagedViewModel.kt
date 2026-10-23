package id.gritlabs.dzikirku.ui.read.quran.paged

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.gritlabs.dzikirku.SeparatorItem
import id.gritlabs.dzikirku.WordItem
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.data.room.dao.AyahLineDao
import id.gritlabs.dzikirku.data.room.dao.SurahDao
import id.gritlabs.dzikirku.model.AyahLineWord
import id.gritlabs.dzikirku.model.events.MenuEvent


import id.gritlabs.dzikirku.util.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranPagedViewModel @Inject constructor(
    dataManager: AppDataManager,
    val surahDao: SurahDao,
    val ayahLineDao: AyahLineDao
) : BaseViewModel<QuranPagedNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
        when(obj){
            is MenuEvent->{
                navigator?.onMenuEvent()
            }
        }
    }

    private val separator = ArrayList<SeparatorItem>()
    val words = MutableLiveData<ArrayList<WordItem>>()

    val surahName = MutableLiveData<String>()
    val surahId = MutableLiveData<Int>()
    val juzNumber = MutableLiveData<String>()

    init {
        separator.addAll(Gson().fromJson(FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/quran/paged/separator.json"), object:TypeToken<ArrayList<SeparatorItem>>(){}.type))
    }

    fun getSurahName(id:Int){
        viewModelScope.launch(IO){
            surahName.postValue(surahDao.getSurahById(id).firstOrNull()?.name)
            surahId.postValue(id)
        }
    }

    fun getJuz(page:Int){
        dataManager.getJuz().forEach {
            if (page in it.start_page..it.end_page){
                juzNumber.value = "Juz ${it.juz}"
            }
        }
    }

    fun getSeparator(page:Int, line:Int):SeparatorItem? {
        return separator.filter { it.page == page && it.line == line }.firstOrNull()
    }

    fun getVerse(line:Int, words:List<AyahLineWord>):String{
        val lastRead = Prefs.quranLastRead
        return words.filter { it.line_number == line }.sortedBy { it.id }.map {
            if (it.verse_key == "${lastRead.surah}:${lastRead.ayah}"){
                "<font color=\"#048383\">${it.code_v1}</font>"
            } else{
                it.code_v1
            }
        }.joinToString("")
    }

    suspend fun getWords(page:Int):List<AyahLineWord>{
        return ayahLineDao.getAyahLineByPage(page).flatMap { ayah->
            ayah.words.apply {
                forEach {
                    it.verse_key = ayah.verse_key
                    it.page = ayah.page
                }
            }
        }
    }

}