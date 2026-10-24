package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class StringArrayConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<String>? {
        if (data == null) {
            return arrayListOf()
        }

        return AppJson.decodeFromString<ArrayList<String>?>(data)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: ArrayList<String>?): String {
        return AppJson.encodeToString(someObjects)
    }
}