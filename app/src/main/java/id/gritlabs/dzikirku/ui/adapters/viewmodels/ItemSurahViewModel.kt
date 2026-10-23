package id.gritlabs.dzikirku.ui.adapters.viewmodels

import androidx.databinding.ObservableField
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.model.Surah
import id.gritlabs.dzikirku.util.StringExt.getText
import id.gritlabs.dzikirku.util.LocaleProvider

class ItemSurahViewModel (val data: Surah){

    fun getRevelation():ObservableField<String>{
        return if(data.revelation == MAKKIYYAH){
            ObservableField("Makkah")
        } else{
            ObservableField("Madinah")
        }
    }

    val verses = ObservableField("${data.verses} ${LocaleProvider.getInstance().getString(LocaleConstants.AYAH)}")
//    val arabic = ObservableField("سورة${data.translation.getArabic()}")
    val translation = ObservableField(data.translation.getText())
    val index = ObservableField("${data.id}")

    companion object{
        val MAKKIYYAH = "makkah"
        val MADANIYYAH = "madinah"
    }
}