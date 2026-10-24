package id.gritlabs.dzikirku.util

import androidx.core.content.FileProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.CountDownTimer
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.gritlabs.dzikirku.model.Link
import id.gritlabs.dzikirku.util.textfont.TypeFactory


fun tick(millisInFuture:Long, interval:Long,onTick:()->Unit):CountDownTimer{
    return object:CountDownTimer(millisInFuture,interval){
        override fun onTick(millisUntilFinished: Long) {
            onTick.invoke()
        }
        override fun onFinish() {
        }
    }
}

fun Context.saveImage(bitmap: Bitmap?, onSave:(Uri?)->Unit){
    bitmap?.let {
        SaveBitmapToFile.getInstance(this, it) { file ->
            file?.let { f -> onSave(FileProvider.getUriForFile(this, "$packageName.fileprovider", f)) }
            null
        }.execute()
    }
}

fun TextView.arabic(){
    typeface = TypeFactory(context).uthman
}

fun TextView.regular(){
    typeface = TypeFactory(context).regular
}

fun TextView.bold(){
    typeface = TypeFactory(context).bold
}

fun BottomSheetDialogFragment.skipCollapsed(){
    dialog?.setOnShowListener { dialog ->
        val d = dialog as BottomSheetDialog
        val bottomSheet =
            d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
    }
}

fun Link.start(context:Context){
    link?.let{
        context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(link) })
    }
}
