package id.gritlabs.dzikirku.util

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import id.gritlabs.dzikirku.data.Prefs


class LocaleProvider(private var context: Context?) {


    private var mStringMap: HashMap<String, String>? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var ins: LocaleProvider? = null

        @Synchronized
        fun getInstance(): LocaleProvider {
            return ins!!
        }

        @Synchronized
        fun getString(key:String):String{
            return ins!!.getString(key)
        }

        fun init(context: Context) {
            ins = LocaleProvider(context)
        }
    }

    fun getString(key: String): String {
        val language = Prefs.language
        val jsonString = context?.resources?.getIdentifier("locale_$language", "raw", context?.packageName)?.let {
            context?.resources?.openRawResource(it)?.bufferedReader().use { it?.readText() }
        } ?: return key
        return jsonString.fromJson<JsonObject>()[key]?.jsonPrimitive?.content ?: key
    }
}
