package id.gritlabs.dzikirku.ui.share

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.slider.Slider
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.databinding.ActivityShareImageBinding
import id.gritlabs.dzikirku.util.ViewUtils
import id.gritlabs.dzikirku.util.saveImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareImageActivity : AppCompatActivity() {

    lateinit var binding:ActivityShareImageBinding

    companion object{

        const val EXTRA_ARABIC = "extra_arabic"
        const val EXTRA_TRANSLATION = "extra_translation"
        const val EXTRA_SOURCE = "extra_source"
        const val EXTRA_TITLE = "extra_title"

        fun start(context: Context, arabic:String, translation:String, source:String, title:String?=null){
            context.startActivity(Intent(context, ShareImageActivity::class.java).apply {
                putExtra(EXTRA_ARABIC, arabic)
                putExtra(EXTRA_TRANSLATION, translation)
                putExtra(EXTRA_SOURCE, source)
                putExtra(EXTRA_TITLE, title)
            })
        }

        fun ShareImageActivity.getArabic():String{
            return intent.getStringExtra(EXTRA_ARABIC).toString()
        }
        fun ShareImageActivity.getTranslation():String{
            return intent.getStringExtra(EXTRA_TRANSLATION).toString()
        }
        fun ShareImageActivity.getSource():String{
            return intent.getStringExtra(EXTRA_SOURCE).toString()
        }
        fun ShareImageActivity.getTitleValue():String?{
            return intent.getStringExtra(EXTRA_TITLE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareImageBinding.inflate(LayoutInflater.from(this))
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.toolbarTitle.text = LocaleConstants.SHARE.locale()
        binding.arabic.text = getArabic()
        binding.translation.text = getTranslation()
        binding.source.text = getSource()
        binding.shareButton.text = LocaleConstants.SHARE.locale()
        binding.shareButton.setOnClickListener {
            shareImage()
        }

        binding.titleText.text = getTitleValue()
        binding.titleText.isVisible = getTitleValue() != null
        binding.title.isVisible = getTitleValue() != null
        binding.titleTitle.isVisible = getTitleValue() != null

        binding.arabicSwitch.setOnCheckedChangeListener { compoundButton, b ->
            binding.arabic.isVisible = b
        }
        binding.translationSwitch.setOnCheckedChangeListener { compoundButton, b ->
            binding.translation.isVisible = b
        }
        binding.titleSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            binding.titleText.textSize = value
        })
        binding.arabicSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            binding.arabic.textSize = value
        })
        binding.translationSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            binding.translation.textSize = value
        })
    }

    fun shareImage(){
        saveImage(
            ViewUtils.getBitmapFromView(
                binding.image
            )
        ) { uri ->
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }, LocaleConstants.SHARE.locale()))
        }
    }

}