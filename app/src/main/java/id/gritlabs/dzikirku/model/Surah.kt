package id.gritlabs.dzikirku.model

import kotlinx.serialization.Serializable

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surah")
@Serializable
data class Surah(
    @PrimaryKey
    val id: Int,
    val revelation: String,
    val verses: Int,
    val name: String,
    val page:Int,
    val translation: ArrayList<LanguageString>
)