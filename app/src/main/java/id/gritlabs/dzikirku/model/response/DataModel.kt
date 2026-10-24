package id.gritlabs.dzikirku.model.response

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

@Serializable
data class DataModel<out T>(
        @SerialName("data") val data: T? = null,
        @SerialName("next_cursor") val nextCursor: Int? = 0,
        @SerialName("use_tfa") val use_tfa: Boolean? = false
)