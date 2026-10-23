package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import id.gritlabs.dzikirku.model.Speaker

class SpeakerConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): Speaker? {
        val gson = Gson()
        if (data == null) {
            return null
        }

        val speaker = try{
            gson.fromJson(data, Speaker::class.java)
        } catch (e:Exception){
            Speaker(name = data)
        }
        return speaker
    }

    @TypeConverter
    fun someObjectListToString(someObjects: Speaker?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}