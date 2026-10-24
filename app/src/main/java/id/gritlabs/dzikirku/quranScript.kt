package id.gritlabs.dzikirku

import id.gritlabs.dzikirku.util.toJsonString

import id.gritlabs.dzikirku.util.fromJson

import id.gritlabs.dzikirku.model.Ayah
import id.gritlabs.dzikirku.model.LanguageString
import java.io.*

fun main() {
    saveQuran("")
}

fun saveQuran(name: String) {
    val quran = File("quran.txt").bufferedReader().readLines().map {
        AyahData(it.split("|")[2], it.split("|")[1].toInt(), it.split("|").first().toInt())
    }
    for (i in 1..6){
        getLegacyQuran(i).writeQuran(quran, i)
    }
}

fun getLegacyQuran(number:Int):ArrayList<Ayah>{
    return getJsonString("quran$number.json").fromJson<ArrayList<Ayah>>()
}

fun ArrayList<Ayah>.writeQuran(quran: List<AyahData>, part: Int) {
    val result = map {
        it.apply {
            text.removeAll { it.language == "arabic" }
            text.add(LanguageString(text = quran.filter { it.ayah == this.verse_number && it.surah == this.chapterId }
                .first().text
                .replace(",", "")
                .replace(
                    if (it.verse_number == 1 && it.chapterId != 1) "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ" else "",
                    ""
                )
                .replace("و۟","و")
                .replace("ا۟", "ا")
                .replace("ي۟", "ي"), language = "arabic"))
        }
    }
    writeToFile(result.toJsonString(), "quran$part")
}

data class AyahData(val text: String, val ayah: Int, val surah: Int)

