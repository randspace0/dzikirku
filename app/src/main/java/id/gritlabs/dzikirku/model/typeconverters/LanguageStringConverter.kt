package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import id.gritlabs.dzikirku.model.LanguageString

class LanguageStringConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<LanguageString>? {
        if (data == null) {
            return arrayListOf()
        }

        return AppJson.decodeFromString<ArrayList<LanguageString>?>(data)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: ArrayList<LanguageString>?): String {
        return AppJson.encodeToString(someObjects)
    }
}