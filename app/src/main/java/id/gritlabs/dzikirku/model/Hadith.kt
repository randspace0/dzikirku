package id.gritlabs.dzikirku.model

import kotlinx.serialization.Serializable

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Serializable
data class Hadith(
    val id: String,
    val name: String,
    val author:String,
    val description:ArrayList<LanguageString>,
    val count: Int,
    val metadata:String?=null
) : Parcelable

@Parcelize
@Serializable
data class HadithChapter(val title:ArrayList<LanguageString>, val startIndex: Int, val endIndex: Int, var chapters:ArrayList<HadithSubChapter> = arrayListOf()):Parcelable
@Parcelize
@Serializable
data class HadithSubChapter(val title:ArrayList<LanguageString>, val chapterIndex:Int?=null, val startIndex:Int?, val endIndex:Int?):Parcelable

@Entity(tableName = "hadithData")
@Parcelize
@Serializable
data class HadithData(
    @ColumnInfo(name = "index")
    val index: String,
    @ColumnInfo(name = "hadith_id")
    val hadith_id: String,
    @ColumnInfo(name = "text")
    val text: ArrayList<LanguageString>,
    @ColumnInfo(name = "narrated")
    val narrated: ArrayList<LanguageString>,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
) : Parcelable