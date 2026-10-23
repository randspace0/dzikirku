package id.gritlabs.dzikirku.data.repository

import android.content.Context
import id.gritlabs.dzikirku.data.room.PersistenceDatabase
import id.gritlabs.dzikirku.data.room.dao.SurahDao
import id.gritlabs.dzikirku.model.Surah
import id.gritlabs.dzikirku.util.getPath1FromUrl
import javax.inject.Inject


class SurahRepository @Inject constructor(private val dao:SurahDao){

    companion object{
        fun getInstance(context:Context):SurahRepository{
            return SurahRepository(PersistenceDatabase.getDatabase(context).surahDao())
        }
    }

    suspend fun getSurahByUrl(url:String):Surah?{
        return dao.getSurahById(url.getPath1FromUrl()).firstOrNull()
    }

    suspend fun getSurahById(id:Int):Surah?{
        return dao.getSurahById(id).firstOrNull()
    }
}