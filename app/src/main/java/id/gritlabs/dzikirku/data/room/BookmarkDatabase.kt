package id.gritlabs.dzikirku.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.gritlabs.dzikirku.data.room.dao.BookmarkDao
import id.gritlabs.dzikirku.data.room.dao.BookmarkGroupDao
import id.gritlabs.dzikirku.model.Bookmark
import id.gritlabs.dzikirku.model.BookmarkGroup
import id.gritlabs.dzikirku.model.typeconverters.StringArrayConverter

@Database(
    entities = [Bookmark::class, BookmarkGroup::class],
    version = 6
)
@TypeConverters(StringArrayConverter::class)
abstract class BookmarkDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao
    abstract fun bookmarkGroupDao(): BookmarkGroupDao

    companion object {
        @Volatile
        private var instance: BookmarkDatabase? = null

        fun getDatabase(context: Context): BookmarkDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, BookmarkDatabase::class.java, "bookmarks")
                .fallbackToDestructiveMigration()
                .build()
    }
}