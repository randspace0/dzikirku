package id.gritlabs.dzikirku.data.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.gritlabs.dzikirku.data.room.BookmarkDatabase
import id.gritlabs.dzikirku.model.BookmarkGroup

@Dao
interface BookmarkGroupDao {

    @Query("SELECT * FROM bookmarkGroup WHERE type IS :type")
    fun getGroupByType(type:String): LiveData<List<BookmarkGroup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putGroup(bookmarkGroup: BookmarkGroup)
}

fun Context.getBookmarkGroupDao():BookmarkGroupDao{
    return BookmarkDatabase.getDatabase(this).bookmarkGroupDao()
}