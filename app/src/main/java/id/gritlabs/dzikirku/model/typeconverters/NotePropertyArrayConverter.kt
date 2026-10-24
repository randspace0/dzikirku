package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import id.gritlabs.dzikirku.model.NoteProperty

class NotePropertyArrayConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<NoteProperty>? {
        if (data == null) {
            return arrayListOf()
        }

        return AppJson.decodeFromString<ArrayList<NoteProperty>?>(data)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: ArrayList<NoteProperty>?): String {
        return AppJson.encodeToString(someObjects)
    }
}