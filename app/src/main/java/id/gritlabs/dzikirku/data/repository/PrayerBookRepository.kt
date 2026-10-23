package id.gritlabs.dzikirku.data.repository

import id.gritlabs.dzikirku.data.ApiService
import id.gritlabs.dzikirku.data.room.dao.PrayerBookDao
import javax.inject.Inject


class PrayerBookRepository @Inject constructor(private val api:ApiService, private val dao:PrayerBookDao){

    val books = dao.getBooks()

    suspend fun getBookById(id:String) = dao.getBookById(id)
}