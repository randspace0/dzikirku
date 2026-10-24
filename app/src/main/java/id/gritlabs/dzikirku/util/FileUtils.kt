
package id.gritlabs.dzikirku.util

import android.content.Context
import android.util.Log
import java.io.*

object FileUtils {

    fun getJsonStringFromRaw(context: Context, rawInt: Int): String {
        val `is`: InputStream = context.resources.openRawResource(rawInt)
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        `is`.use { `is` ->
            val reader: Reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }
        return writer.toString()
    }


    fun Context.writeStringToFile(data: String,fileName:String) {
        val folder = File(filesDir,"DzikirQu Notes")
        if (!folder.exists()){
            folder.mkdir()
        }
        try {
            val outputStreamWriter =
                OutputStreamWriter(FileOutputStream(
                    "${filesDir}/DzikirQu Notes/$fileName.json"
                ))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }

    fun getJsonStringFromAssets(context: Context, assetPath: String, progress:(Int)->Any?={}):String{
        val `is`: InputStream = context.assets.open(assetPath)
        ProgressInputStream(`is`).addListener {
            progress.invoke(it)
        }
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        `is`.use { `is` ->
            val reader: Reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }
        return writer.toString()
    }

}