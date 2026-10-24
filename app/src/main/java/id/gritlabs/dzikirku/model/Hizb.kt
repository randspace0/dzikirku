package id.gritlabs.dzikirku.model

import kotlinx.serialization.Serializable

@Serializable
class Hizb(val hizb: Float, val surah: String, val verse: Int, var juz:Int)