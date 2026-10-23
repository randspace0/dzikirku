package id.gritlabs.dzikirku.ui.praytime

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseActivity
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.constants.RingType
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.databinding.ActivityPraytimeBinding
import id.gritlabs.dzikirku.databinding.ItemHomePraytimeBinding
import id.gritlabs.dzikirku.ui.qibla.QiblaFragment
import id.gritlabs.dzikirku.util.ViewUtils.animToY
import id.gritlabs.dzikirku.util.ViewUtils.fadeShow
import id.gritlabs.dzikirku.util.isDeviceHasCompass
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PraytimeActivity : BaseActivity<ActivityPraytimeBinding, PraytimeViewModel>(),
    PraytimeNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_praytime
    override val viewModel: PraytimeViewModel by viewModels()

    companion object{
        fun start(context:Context){
            context.startActivity(Intent(context, PraytimeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        viewModel.navigator = this
        viewModel.configureTicker()
        viewModel.prayerTime.observe(this) {
            viewModel.buildPrayers(it)
        }
        configurePraytimes()
        viewModel.isDeviceHasCompass.set(isDeviceHasCompass())

        viewDataBinding.praytimeContainer.fadeShow(duration = 1200)
        viewDataBinding.praytimeContainer.animToY(0f, duration = 1200)
        viewDataBinding.qibla.setOnClickListener {
            QiblaFragment().show(supportFragmentManager, "")
        }

        setStatusbarColor(R.color.neutral_0)
    }

    fun configurePraytimes() {
        addPraytime(
            LocaleConstants.FAJR.locale(),
            R.drawable.ic_fajr_primary,
            viewModel.untilFajr,
            viewModel.praytimeFajr,
            viewModel.ringFajr
        )
        addPraytime(
            LocaleConstants.DHUHR.locale(),
            R.drawable.ic_dhuhr_primary,
            viewModel.untilDhuhr,
            viewModel.praytimeDhuhr,
            viewModel.ringDhuhr
        )
        addPraytime(
            LocaleConstants.ASR.locale(),
            R.drawable.ic_asr_primary,
            viewModel.untilAsr,
            viewModel.praytimeAsr,
            viewModel.ringAsr
        )
        addPraytime(
            LocaleConstants.MAGHRIB.locale(),
            R.drawable.ic_maghrib_primary,
            viewModel.untilMaghrib,
            viewModel.praytimeMaghrib,
            viewModel.ringMaghrib
        )
        addPraytime(
            LocaleConstants.ISYA.locale(),
            R.drawable.ic_isya_primary,
            viewModel.untilIsya,
            viewModel.praytimeIsya,
            viewModel.ringIsya
        )
    }

    fun addPraytime(
        title: String,
        icon: Int,
        until: MutableLiveData<String>,
        praytime: MutableLiveData<String>,
        ring: MutableLiveData<Int>
    ) {
        val view = ItemHomePraytimeBinding.inflate(LayoutInflater.from(this)).apply {
            this.title.text = title
            this.icon.setImageResource(icon)
            this.praytime
        }

        until.observe(this) {
            view.until.text = it
        }

        praytime.observe(this) {
            view.praytime.text = it
        }

        ring.observe(this) {
            when(it){
                RingType.SILENT-> view.ring.setImageResource(R.drawable.ic_volume_off)
                RingType.NOTIFICATION -> view.ring.setImageResource(R.drawable.ic_notification)
                RingType.SOUND -> view.ring.setImageResource(R.drawable.ic_volume_up)
            }
        }
        view.ring.configureRing(title)

        viewDataBinding?.praytimeContainer?.addView(view.root)
    }


    fun View.configureRing(title: String) {
        when (title) {
            LocaleConstants.FAJR.locale() -> {
                setOnClickListener {
                    Prefs.ringFajr = nextRingValue(viewModel.ringFajr.value ?: 0)
                    viewModel.ringFajr.value = Prefs.ringFajr
                }
            }
            LocaleConstants.DHUHR.locale() -> {
                setOnClickListener {
                    Prefs.ringDhuhr = nextRingValue(viewModel.ringDhuhr.value ?: 0)
                    viewModel.ringDhuhr.value = Prefs.ringDhuhr
                }
            }
            LocaleConstants.ASR.locale() -> {
                setOnClickListener {
                    Prefs.ringAsr = nextRingValue(viewModel.ringAsr.value ?: 0)
                    viewModel.ringAsr.value = Prefs.ringAsr
                }
            }
            LocaleConstants.MAGHRIB.locale() -> {
                setOnClickListener {
                    Prefs.ringMaghrib = nextRingValue(viewModel.ringMaghrib.value ?: 0)
                    viewModel.ringMaghrib.value = Prefs.ringMaghrib
                }
            }
            LocaleConstants.ISYA.locale() -> {
                setOnClickListener {
                    Prefs.ringIsya = nextRingValue(viewModel.ringIsya.value ?: 0)
                    viewModel.ringIsya.value = Prefs.ringIsya
                }
            }
        }
    }

    override fun onClickQibla() {
        QiblaFragment().show(supportFragmentManager, "")
    }

    fun nextRingValue(currentValue: Int): Int {
        return when (currentValue) {
            RingType.SOUND -> RingType.NOTIFICATION
            RingType.NOTIFICATION -> RingType.SILENT
            else -> RingType.SOUND
        }
    }
}