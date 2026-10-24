package id.gritlabs.dzikirku.model.typeconverters

import androidx.room.TypeConverter
import id.gritlabs.dzikirku.util.AppJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import id.gritlabs.dzikirku.model.Author

class AuthorTypeConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): Author? {
        if (data == null) {
            return Author(uid = "", name = "", profilePicture = "")
        }

        return AppJson.decodeFromString<Author?>(data)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: Author?): String {
        return AppJson.encodeToString(someObjects)
    }
}