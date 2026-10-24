package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import id.gritlabs.dzikirku.model.QuranLastRead

class ListQuranLastReadConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<QuranLastRead> {
        if (data == null) {
            return arrayListOf()
        }

        return AppJson.decodeFromString<List<QuranLastRead>>(data)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<QuranLastRead>): String {
        return AppJson.encodeToString(someObjects)
    }
}