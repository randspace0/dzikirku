package id.gritlabs.dzikirku.data

import id.gritlabs.dzikirku.util.fromJson

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import id.gritlabs.dzikirku.VersesItem
import id.gritlabs.dzikirku.data.room.BookmarkDatabase
import id.gritlabs.dzikirku.data.room.NotesDatabase
import id.gritlabs.dzikirku.data.room.PersistenceDatabase
import id.gritlabs.dzikirku.model.*
import id.gritlabs.dzikirku.util.FileUtils
import id.gritlabs.dzikirku.util.praytimes.PrayerTimeHelper
import kotlinx.coroutines.Dispatchers.Main
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(
    val mContext: Context,
    val mApiService: ApiService,
    val mPersistenceDatabase: PersistenceDatabase,
    val mBookmarkDatabase: BookmarkDatabase,
    val mNoteDatabase: NotesDatabase,
) {

    fun getBooks(): LiveData<List<PrayerBook>>? {
        return mPersistenceDatabase.prayerBookDao().getBooks()
    }

    suspend fun getBookById(id: String): PrayerBook? {
        return mPersistenceDatabase.prayerBookDao().getBookById(id).firstOrNull()
    }

    fun getJuz(): ArrayList<Juz> {
        return FileUtils.getJsonStringFromAssets(mContext, "json/quran/juz.json").fromJson<ArrayList<Juz>>()
    }

    fun getQuranByPage(page: Int): ArrayList<VersesItem> {
        return FileUtils.getJsonStringFromAssets(
                mContext,
                "json/quran/paged/page$page.json"
            ).fromJson<ArrayList<VersesItem>>()
    }

    fun isFirstAyahOfJuz(juz: Int, chapterId: Int, verseNumber: Int): Boolean {
        getJuz().filter { it.juz == juz }.firstOrNull()?.let {
            return it.surah_id == chapterId && it.verse == verseNumber
        }
        return false
    }

    fun getHizb(): List<Hizb> {
        return FileUtils.getJsonStringFromAssets(mContext, "json/quran/hizb.json").fromJson<ArrayList<Hizb>>()
    }

    fun getHadithById(id: String): List<HadithData> {
        return FileUtils.getJsonStringFromAssets(mContext, "json/hadith/$id.json").fromJson<ArrayList<HadithData>>()
    }

    fun getJuzWithHizb(juzNumber: Int): List<Any> {
        val juz = getJuz()
        val hizb = FileUtils.getJsonStringFromAssets(mContext, "json/quran/hizb.json").fromJson<ArrayList<Hizb>>()
        return arrayListOf<Any>().apply {
            juz.forEach {
                this.add(it)
                if (it.juz == juzNumber) {
                    it.isHizbShown = true
                    this.addAll(hizb.filter { hizb ->
                        hizb.juz == it.juz
                    })
                }
            }
        }
    }


    fun getPrayer(bookId: String): LiveData<List<Prayer>> {
        return mPersistenceDatabase.prayerDao().getPrayerByBookId(bookId)
    }

    suspend fun getPrayerById(prayerId: String): Prayer? {
        return mPersistenceDatabase.prayerDao().getPrayerByIdSuspend(prayerId).firstOrNull()
    }

    fun getPrayerData(prayerId: String): LiveData<List<PrayerData>> {
        return mPersistenceDatabase.prayerDataDao().getPrayerData(prayerId)
    }

    private suspend fun getPrayerTime(): PrayerTime {
        return PrayerTimeHelper.getPrayerTime(mContext)
    }

    val prayerTime = liveData(Main) {
        try {
            val prefs = PrayerTimeHelper.getPrayerTimeFromPrefs(mContext)
            emit(prefs)
            val new = getPrayerTime()
            emit(new)
        } catch (e: java.lang.Exception) {
        }
    }

    fun getGregorianDate(): String {
        return SimpleDateFormat("EEE, MMM d, yyyy").format(Calendar.getInstance().time)
    }

    fun getHijriDate(): String {
        val cal = UmmalquraCalendar()
        cal.time = Calendar.getInstance().time
        return "${cal.get(Calendar.DAY_OF_MONTH)} ${
            cal.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH)}," +
                " ${cal.get(Calendar.YEAR)}"
    }


}