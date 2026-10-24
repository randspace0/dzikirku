package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import id.gritlabs.dzikirku.model.AyahLineWord

class AyahLineWordConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<AyahLineWord>? {
        if (data == null) {
            return arrayListOf()
        }

        return AppJson.decodeFromString<List<AyahLineWord>?>(data)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<AyahLineWord>?): String {
        return AppJson.encodeToString(someObjects)
    }
}