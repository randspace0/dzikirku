package id.gritlabs.dzikirku.data.repository

import id.gritlabs.dzikirku.data.ApiService
import id.gritlabs.dzikirku.data.room.dao.PrayerDao
import id.gritlabs.dzikirku.data.room.dao.PrayerDataDao
import javax.inject.Inject


class PrayerRepository @Inject constructor(private val api:ApiService, private val dao:PrayerDao, private val dataDao: PrayerDataDao){

    // Prayer
    fun getPrayer(bookId:String) = dao.getPrayerByBookId(bookId)

    suspend fun getPrayerById(prayerId:String) = dao.getPrayerByIdSuspend(prayerId)
    // PrayerData
    fun getPrayerData(prayerId:String) = dataDao.getPrayerData(prayerId)

}