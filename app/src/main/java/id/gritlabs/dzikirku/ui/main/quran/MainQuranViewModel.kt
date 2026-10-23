package id.gritlabs.dzikirku.ui.main.quran

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.gritlabs.dzikirku.SeparatorItem
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.data.room.dao.SurahDao
import id.gritlabs.dzikirku.model.events.ApplyWindowEvent
import id.gritlabs.dzikirku.model.events.NoteInsertEvent
import id.gritlabs.dzikirku.model.events.SettingsEvent
import id.gritlabs.dzikirku.util.FileUtils
import id.gritlabs.dzikirku.util.LocaleProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainQuranViewModel @Inject constructor(
    dataManager: AppDataManager,
    val surahDao: SurahDao
) :
    BaseViewModel<MainQuranNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is SettingsEvent->{
                navigator?.onSettingsEvent()
            }
            is NoteInsertEvent->{
                navigator?.onNotePropertySelectedEvent()
            }
            is ApplyWindowEvent->{
                navigator?.onApplyWindowEvent(obj.insets)
            }
        }
    }

    val lastReadString = MutableLiveData(LocaleProvider.getString(LocaleConstants.NOT_SET))

    val surahId: MutableLiveData<Int?> = MutableLiveData()
    val verseId: MutableLiveData<Int?> = MutableLiveData()

    fun getArabicCalligraphy(mContext: Context, surah: Int): String {
        return Gson().fromJson<ArrayList<SeparatorItem>>(
            FileUtils.getJsonStringFromAssets(
                mContext,
                "json/quran/paged/separator.json"
            ), object :
                TypeToken<ArrayList<SeparatorItem>>() {}.type
        ).filter { it.surah == surah }.firstOrNull()?.unicode ?: ""
    }

}