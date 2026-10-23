package id.gritlabs.dzikirku.ui.main.home

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.databinding.FragmentMainHomeBinding
import id.gritlabs.dzikirku.model.PrayerBook
import id.gritlabs.dzikirku.model.events.MainTabEvent
import id.gritlabs.dzikirku.model.events.MainTabType
import id.gritlabs.dzikirku.ui.adapters.PrayerBookAdapter
import id.gritlabs.dzikirku.ui.bsd.prayer.PrayerBSD
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsActivity
import id.gritlabs.dzikirku.ui.main.bookmarks.BookmarkActivity
import id.gritlabs.dzikirku.ui.praytime.PraytimeActivity
import id.gritlabs.dzikirku.ui.search.SearchActivity
import id.gritlabs.dzikirku.util.AlertUtils.showAutostartPermissionAlert
import id.gritlabs.dzikirku.util.BooleanUtil.isXiaomi
import id.gritlabs.dzikirku.util.RxBus
import id.gritlabs.dzikirku.util.ViewUtils.dpToPx
import id.gritlabs.dzikirku.util.ViewUtils.height
import id.gritlabs.dzikirku.util.praytimes.PrayerTimeHelper
import id.gritlabs.dzikirku.util.startAutostartPermissionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import android.util.Pair as UtilPair


@AndroidEntryPoint
class MainHomeFragment : BaseFragment<FragmentMainHomeBinding, MainHomeViewModel>(),
    MainHomeNavigator,
    PrayerBookAdapter.Callback {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main_home
    override val viewModel: MainHomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = this

        viewModel.buildPrayerTime()
        viewModel.setUpLastRead()
        configureViews()
    }

    override fun onApplyWindowEvent(insets: Insets) {
        viewDataBinding?.statusBarHeight?.height(insets.top, duration = 0)
        viewModel.statusBarHeight.value = insets.top
        viewDataBinding?.viewHeader?.updatePadding(0,insets.top,0,0)
    }

    override fun onClickSearch() {
        val popupMenu = PopupMenu(requireActivity(), viewDataBinding?.search!!)
        popupMenu.menu.add(LocaleConstants.QURAN.locale())
        popupMenu.menu.add(LocaleConstants.PRAYER.locale())
        popupMenu.setOnMenuItemClickListener {
            when (it.title) {
                LocaleConstants.QURAN.locale() -> SearchActivity.startQuran(requireActivity())
                LocaleConstants.PRAYER.locale() -> SearchActivity.startPrayer(requireActivity())
            }
            true
        }
        popupMenu.show()
    }

    override fun onClickSettings() {
        SettingsActivity.start(requireActivity())
    }

    override fun onResume() {
        super.onResume()
        viewModel.time.value = Calendar.getInstance().apply{
            add(Calendar.MINUTE, 1)
        }.time
    }

    override fun onClickGrantPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun onClickReadQuran() {
        RxBus.getDefault().send(MainTabEvent(MainTabType.QURAN))
    }

    override fun onClickPraytime() {
        requireActivity().startActivity(
            Intent(requireActivity(), PraytimeActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(
                requireActivity(),
                UtilPair.create(viewDataBinding?.viewHeader, "header"),
                UtilPair.create(viewDataBinding?.viewPrayerTime, "text")
            ).toBundle()
        )
    }

    override fun onClickBookmark() {
        BookmarkActivity.startQuran(requireActivity())
    }

    fun configureViews() {
        viewDataBinding?.compose?.setContent {
            Compose()
        }
        viewDataBinding?.scrollView?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            viewDataBinding?.statusBarBackground?.alpha = viewDataBinding?.scrollView?.scrollY!! / dpToPx(240).toFloat()
        }
    }

    override fun onSelectedItem(book: PrayerBook) {
        PrayerBSD.newInstance(book).show(childFragmentManager, "")
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                lifecycleScope.launch {
                    PrayerTimeHelper.getPrayerTime(requireActivity())
                    if (isXiaomi()) {
                        requireActivity().showAutostartPermissionAlert {
                            requireActivity().startAutostartPermissionActivity()
                        }
                    }
                }
            }
        }
}