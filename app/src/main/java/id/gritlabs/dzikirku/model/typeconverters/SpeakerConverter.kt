package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import id.gritlabs.dzikirku.model.Speaker

class SpeakerConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): Speaker? {
        if (data == null) {
            return null
        }

        val speaker = try{
            AppJson.decodeFromString<Speaker>(data)
        } catch (e:Exception){
            Speaker(name = data)
        }
        return speaker
    }

    @TypeConverter
    fun someObjectListToString(someObjects: Speaker?): String {
        return AppJson.encodeToString(someObjects)
    }
}