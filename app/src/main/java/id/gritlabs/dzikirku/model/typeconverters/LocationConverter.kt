package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import id.gritlabs.dzikirku.model.Location

class LocationConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): Location? {
        if (data == null) {
            return null
        }

        val speaker = try{
            AppJson.decodeFromString<Location>(data)
        } catch (e:Exception){
            Location(name = data)
        }
        return speaker
    }

    @TypeConverter
    fun someObjectListToString(someObjects: Location?): String {
        return AppJson.encodeToString(someObjects)
    }
}