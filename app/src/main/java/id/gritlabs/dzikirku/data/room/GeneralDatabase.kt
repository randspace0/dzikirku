package id.gritlabs.dzikirku.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.gritlabs.dzikirku.data.room.dao.DailyReminderDao
import id.gritlabs.dzikirku.data.room.dao.DailyReminderParentDao
import id.gritlabs.dzikirku.model.DailyReminder
import id.gritlabs.dzikirku.model.DailyReminderParent
import id.gritlabs.dzikirku.model.typeconverters.StringArrayConverter

@Database(
    entities = [DailyReminder::class, DailyReminderParent::class],
    version = 4
)
@TypeConverters(StringArrayConverter::class)
abstract class GeneralDatabase : RoomDatabase() {

    abstract fun dailyReminderDao(): DailyReminderDao
    abstract fun dailyReminderParentDao(): DailyReminderParentDao

    companion object {
        @Volatile
        private var instance: GeneralDatabase? = null

        fun getDatabase(context: Context): GeneralDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, GeneralDatabase::class.java, "general")
                .fallbackToDestructiveMigration()
                .build()
    }
}