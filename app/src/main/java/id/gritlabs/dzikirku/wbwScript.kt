package id.gritlabs.dzikirku

import id.gritlabs.dzikirku.util.toJsonString

import id.gritlabs.dzikirku.util.fromJson

import id.gritlabs.dzikirku.model.AyahWbw
import id.gritlabs.dzikirku.model.LanguageString
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File

fun main() {
    val english =
        Jsoup.parse(File("englishhtml.json").inputStream().readBytes().toString(Charsets.UTF_8))
            .body().select("span").filter { it.hasClass("single-word") }
    val bahasa =
        Jsoup.parse(File("bahasahtml.json").inputStream().readBytes().toString(Charsets.UTF_8))
            .body().select("span").filter { it.hasClass("single-word") }

    write(english, "english")
    write(bahasa, "bahasa")
    join()

//    for (i in 1..114){
//        val ayah = (//            File("$i.json").inputStream().readBytes().toString(Charsets.UTF_8)).fromJson<ArrayList<AyahWbw>>().maxOf { it.ayah }
//        println(ayah)
//    }
}

fun join() {
    val english = (File("english.json").inputStream().readBytes().toString(Charsets.UTF_8)).fromJson<ArrayList<AyahWbw>>()
    val bahasa = (File("bahasa.json").inputStream().readBytes().toString(Charsets.UTF_8)).fromJson<ArrayList<AyahWbw>>()

    english.forEachIndexed { index, wbw ->
        bahasa[index].text.filter { it.language == "bahasa" }.firstOrNull()
            ?.let { wbw.text.add(it) }
    }

    writeToFile(english.toJsonString(), "2")
}

fun write(data: List<Element>, language: String) {
    val wbw = arrayListOf<AyahWbw>()
    data.forEach {
        if (it.id().split("-").count() > 1) {
            val surah = it.id().split("-")[0].toInt()
            val ayah = it.id().split("-")[1].toInt()
            val translation = it.select("span").filter { it.hasClass("word-translation") }
            val arabic = it.select("span").filter { it.hasClass("word-arabic word-arabic-uthmani-hafs") }
            translation.forEachIndexed { index, element ->
                wbw.add(
                    AyahWbw(
                        surah = surah, ayah = ayah, index = wbw.size, text = arrayListOf(
                            LanguageString(
                                text = translation[index].text(),
                                language = language
                            ),
                            LanguageString(
                                text = arabic[index].text(),
                                language = "arabic"
                            ),
                        )
                    )
                )
            }
        }
    }
    writeToFile(wbw.toJsonString(), language)
}

