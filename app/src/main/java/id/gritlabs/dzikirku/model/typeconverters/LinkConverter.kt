package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import id.gritlabs.dzikirku.model.Link
import id.gritlabs.dzikirku.model.LinkType

class LinkConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): Link? {
        if (data == null) {
            return Link(arrayListOf(),arrayListOf(),"",LinkType.Unidentified, "")
        }

        return AppJson.decodeFromString<Link?>(data)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: Link?): String {
        return AppJson.encodeToString(someObjects)
    }
}