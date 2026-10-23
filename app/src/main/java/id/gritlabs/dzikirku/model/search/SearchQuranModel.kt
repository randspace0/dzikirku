package id.gritlabs.dzikirku.model.search

import id.gritlabs.dzikirku.model.Ayah

data class SearchQuranModel(
    val surah:Int,
    val ayah:List<Ayah>
)