package id.gritlabs.dzikirku.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.gritlabs.dzikirku.data.room.dao.KhatamDao
import id.gritlabs.dzikirku.model.Khatam
import id.gritlabs.dzikirku.model.typeconverters.DateConverter
import id.gritlabs.dzikirku.model.typeconverters.ListQuranLastReadConverter
import id.gritlabs.dzikirku.model.typeconverters.QuranLastReadConverter
import id.gritlabs.dzikirku.model.typeconverters.StringArrayConverter

@Database(
    entities = [Khatam::class],
    version = 2
)
@TypeConverters(StringArrayConverter::class, DateConverter::class, QuranLastReadConverter::class, ListQuranLastReadConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun khatamDao(): KhatamDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "database")
                .fallbackToDestructiveMigration()
                .build()
    }
}