package id.gritlabs.dzikirku.model

import kotlinx.serialization.Serializable

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Serializable
data class LanguageString(
    var text: String? = null,
    var language: String? = null
):Parcelable