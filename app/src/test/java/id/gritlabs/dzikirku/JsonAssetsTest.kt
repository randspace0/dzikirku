package id.gritlabs.dzikirku

import id.gritlabs.dzikirku.model.Ayah
import id.gritlabs.dzikirku.model.AyahLine
import id.gritlabs.dzikirku.model.AyahWbw
import id.gritlabs.dzikirku.model.Hizb
import id.gritlabs.dzikirku.model.HadithChapter
import id.gritlabs.dzikirku.model.HadithData
import id.gritlabs.dzikirku.model.Juz
import id.gritlabs.dzikirku.model.Note
import id.gritlabs.dzikirku.model.Prayer
import id.gritlabs.dzikirku.model.PrayerBook
import id.gritlabs.dzikirku.model.PrayerData
import id.gritlabs.dzikirku.model.PrayerTime
import id.gritlabs.dzikirku.model.QuranLastRead
import id.gritlabs.dzikirku.model.Surah
import id.gritlabs.dzikirku.model.legacy.NoteLegacy
import id.gritlabs.dzikirku.SeparatorItem
import id.gritlabs.dzikirku.VersesItem
import id.gritlabs.dzikirku.util.fromJson
import id.gritlabs.dzikirku.util.toJsonString
import kotlinx.serialization.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Decodes every bundled JSON asset with the real model types, so a model/JSON mismatch
 * fails here instead of crashing the app on first launch.
 */
class JsonAssetsTest {

    private val assets = File("src/main/assets/json")

    private fun text(path: String) = File(assets, path).readText(Charsets.UTF_8)

    private fun files(dir: String, filter: (File) -> Boolean = { true }) =
        File(assets, dir).listFiles { f -> f.extension == "json" && filter(f) }!!.sortedBy { it.name }

    @Test
    fun prayerBooksPrayersAndData() {
        assertTrue(text("prayer/book.json").fromJson<ArrayList<PrayerBook>>().isNotEmpty())
        assertTrue(text("prayer/prayer.json").fromJson<ArrayList<Prayer>>().isNotEmpty())
        assertTrue(text("prayer/prayerData.json").fromJson<ArrayList<PrayerData>>().isNotEmpty())
    }

    @Test
    fun quranStaticData() {
        assertEquals(114, text("quran/surah.json").fromJson<ArrayList<Surah>>().size)
        assertTrue(text("quran/hizb.json").fromJson<ArrayList<Hizb>>().isNotEmpty())
        assertTrue(text("quran/juz.json").fromJson<ArrayList<Juz>>().isNotEmpty())
        assertTrue(text("quran/paged/separator.json").fromJson<ArrayList<SeparatorItem>>().isNotEmpty())
    }

    @Test
    fun quranTextParts() {
        var verses = 0
        for (i in 1..6) verses += text("quran/quran$i.json").fromJson<ArrayList<Ayah>>().size
        assertEquals(6236, verses)
    }

    @Test
    fun quranPagesDecodeAsLinesAndVerses() {
        val pages = files("quran/paged") { it.name.startsWith("page") }
        assertEquals(604, pages.size)
        for (f in pages) {
            val body = f.readText(Charsets.UTF_8)
            body.fromJson<ArrayList<AyahLine>>()
            body.fromJson<ArrayList<VersesItem>>()
        }
    }

    @Test
    fun wordByWord() {
        val parts = files("quran/wbw")
        assertTrue(parts.isNotEmpty())
        for (f in parts) f.readText(Charsets.UTF_8).fromJson<ArrayList<AyahWbw>>()
    }

    @Test
    fun hadithBooks() {
        val data = files("hadith") { !it.name.contains("books") }
        assertTrue(data.isNotEmpty())
        for (f in data) {
            val body = f.readText(Charsets.UTF_8)
            // chapter indexes and hadith rows share the folder; each file is one of the two
            val ok = runCatching { body.fromJson<ArrayList<HadithData>>() }.isSuccess ||
                runCatching { body.fromJson<ArrayList<HadithChapter>>() }.isSuccess
            assertTrue("undecodable: ${f.name}", ok)
        }
    }

    @Test
    fun localeFilesAreFlatStringMaps() {
        for (name in listOf("locale_bahasa", "locale_english")) {
            val o = File("src/main/res/raw/$name.json").readText(Charsets.UTF_8).fromJson<JsonObject>()
            assertTrue(name, o.isNotEmpty())
        }
    }

    @Test
    fun roundTripsPersistedShapes() {
        val last = QuranLastRead(2, 255, page = 42, isSavedFromPage = true, timestamp = 7L)
        assertEquals(last, last.toJsonString().fromJson<QuranLastRead>())
        val pt = PrayerTime(address = "x", fajr = "04:30")
        assertEquals("04:30", pt.toJsonString().fromJson<PrayerTime>().fajr)
    }

    @Test
    fun noteAcceptsLegacyPresenterKeyAndUnknownFields() {
        val note = """{"id":1,"title":"t","presenter":{"id":3,"name":"Ustadz"},"is_public":true,"extra":"ignored"}"""
            .fromJson<Note>()
        assertEquals("Ustadz", note.speaker?.name)
        assertEquals(true, note.isPublic)
        val legacy = """{"id":1,"presenter":"Ustadz","tags":["a"]}""".fromJson<NoteLegacy>()
        assertEquals("Ustadz", legacy.speaker)
    }

    @Test
    fun missingOrNullFieldsFallBackToDefaults() {
        val l = """{"id":1,"verse_number":null}""".fromJson<AyahLine>()
        assertEquals(1, l.id)
        assertEquals(null, l.verse_number)
    }
}
