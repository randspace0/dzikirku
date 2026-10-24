package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import id.gritlabs.dzikirku.model.QuranLastRead

class QuranLastReadConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): QuranLastRead {
        if (data == null) {
            return QuranLastRead(
                surah = 0,
                ayah = 0,
                page = null,
                isSavedFromPage = null,
                timestamp = null
            )
        }

        return AppJson.decodeFromString<QuranLastRead>(data)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: QuranLastRead): String {
        return AppJson.encodeToString(someObjects)
    }
}