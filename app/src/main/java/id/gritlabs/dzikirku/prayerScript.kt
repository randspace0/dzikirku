package id.gritlabs.dzikirku

import id.gritlabs.dzikirku.util.toJsonString

import id.gritlabs.dzikirku.util.fromJson

import id.gritlabs.dzikirku.model.Prayer
import id.gritlabs.dzikirku.model.PrayerBook
import id.gritlabs.dzikirku.model.PrayerData

fun main() {
    changeIds()
}

fun changeIds(){
    val books = getJsonString("backup/book.json").fromJson<ArrayList<PrayerBook>>()
    val prayer = getJsonString("backup/prayer.json").fromJson<ArrayList<Prayer>>()
    val prayerData = getJsonString("backup/prayerData.json").fromJson<ArrayList<PrayerData>>()

    books.mapIndexed { index, book ->
        prayer.filter { it.book_id == book.id }.map {
            it.book_id = index.toString()
        }
        prayerData.filter { it.book_id == book.id }.map {
            it.book_id = index.toString()
        }
        book.id = index.toString()
    }
    prayer.mapIndexed { index, prayer ->
        prayerData.filter { it.prayer_id == prayer.id }.map {
            it.prayer_id = index.toString()
        }
        prayer.id = index.toString()
    }
    prayerData.mapIndexed { index, prayerData ->
        prayerData.id = index.toString()
    }

    createNewFile("prayerv2")
    writeToFile(prayer.toJsonString(), "prayerv2")
    createNewFile("bookv2")
    writeToFile(books.toJsonString(), "bookv2")
    createNewFile("prayerDatav2")
    writeToFile(prayerData.toJsonString(), "prayerDatav2")
}