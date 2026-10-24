package id.gritlabs.dzikirku.util

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

/**
 * Lenient on purpose: bundled assets and server payloads carry extra keys, and old rows
 * stored by Room converters may lack newer fields.
 */
val AppJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
    encodeDefaults = true
    explicitNulls = false
}

inline fun <reified T> String.fromJson(): T = AppJson.decodeFromString(this)

inline fun <reified T> T.toJsonString(): String = AppJson.encodeToString(this)

/** LatLng is a Maps SDK class; keep the {"latitude","longitude"} shape stored by earlier builds. */
object LatLngSerializer : KSerializer<LatLng> {
    @Serializable
    private class Surrogate(val latitude: Double, val longitude: Double)

    override val descriptor: SerialDescriptor = Surrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: LatLng) =
        encoder.encodeSerializableValue(Surrogate.serializer(), Surrogate(value.latitude, value.longitude))

    override fun deserialize(decoder: Decoder): LatLng =
        decoder.decodeSerializableValue(Surrogate.serializer()).let { LatLng(it.latitude, it.longitude) }
}
