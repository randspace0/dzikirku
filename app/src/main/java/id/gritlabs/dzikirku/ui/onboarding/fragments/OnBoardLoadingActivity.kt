package id.gritlabs.dzikirku.ui.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseActivity
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.databinding.ActivityOnBoardLoadingBinding
import id.gritlabs.dzikirku.ui.main.MainActivity
import id.gritlabs.dzikirku.ui.onboarding.fragments.viewmodels.OnBoardLoadingViewModel
import id.gritlabs.dzikirku.util.*
import id.gritlabs.dzikirku.util.AlertUtils.showAutostartPermissionAlert
import id.gritlabs.dzikirku.util.BooleanUtil.isLocationGranted
import id.gritlabs.dzikirku.util.BooleanUtil.isXiaomi
import id.gritlabs.dzikirku.util.ViewUtils.animToY
import id.gritlabs.dzikirku.util.ViewUtils.fadeShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardLoadingActivity :
    BaseActivity<ActivityOnBoardLoadingBinding, OnBoardLoadingViewModel>() {

    override val bindingVariable: Int get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_on_board_loading
    override val viewModel: OnBoardLoadingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        configureData()
    }

    fun configureData() {
        viewModel.isBookLoaded.observe(this) { result ->
            if (result != ResultEnum.Success) return@observe
            viewDataBinding.progressPrayer.fadeShow()
            viewDataBinding.progressPrayer.animToY(0f, duration = 1000)
            Prefs.isBookLoaded = true
        }
        viewModel.isPrayerLoaded.observe(this) { result ->
            if (result != ResultEnum.Success) return@observe
            viewDataBinding.progressQuran.fadeShow()
            viewDataBinding.progressQuran.animToY(0f, duration = 1000)
            Prefs.isPrayerLoaded = true
        }
        viewModel.isQuranLoaded.observe(this) { result ->
            if (result != ResultEnum.Success) return@observe
            viewDataBinding.progressHadith.fadeShow()
            viewDataBinding.progressHadith.animToY(0f, duration = 1000)
            Prefs.isQuranLoaded = true
        }
        viewModel.isHadithLoaded.observe(this) { result ->
            if (result != ResultEnum.Success) return@observe

            Prefs.isHadithLoaded = true
            Prefs.isFirstRun = false
            io {
                putDefaultBookmarks()
                main {
                    if (Prefs.isFirstRun && isXiaomi() && isLocationGranted()) {
                        showAutostartPermissionAlert {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                            startAutostartPermissionActivity()
                        }
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

}

