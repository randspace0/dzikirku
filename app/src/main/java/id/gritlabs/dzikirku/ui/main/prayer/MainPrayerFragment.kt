package id.gritlabs.dzikirku.ui.main.prayer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.tabs.TabLayoutMediator
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.databinding.FragmentMainPrayerBinding
import id.gritlabs.dzikirku.databinding.TabImageCircleBinding
import id.gritlabs.dzikirku.databinding.TabTextCircleBinding
import id.gritlabs.dzikirku.model.events.MainTabEvent
import id.gritlabs.dzikirku.model.events.MainTabType
import id.gritlabs.dzikirku.ui.adapters.FragmentPagerAdapter
import id.gritlabs.dzikirku.ui.main.bookmarks.BookmarkActivity
import id.gritlabs.dzikirku.ui.main.prayer.fragments.grid.PrayerGridFragment
import id.gritlabs.dzikirku.ui.main.prayer.fragments.list.PrayerListFragment
import id.gritlabs.dzikirku.ui.search.SearchActivity
import id.gritlabs.dzikirku.util.Appbar
import id.gritlabs.dzikirku.util.RxBus
import id.gritlabs.dzikirku.util.StringExt.getText
import id.gritlabs.dzikirku.util.ViewUtils.height
import id.gritlabs.dzikirku.util.prayerTypes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainPrayerFragment : BaseFragment<FragmentMainPrayerBinding, MainPrayerViewModel>(),
    MainPrayerNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main_prayer
    override val viewModel: MainPrayerViewModel by viewModels()

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, MainPrayerFragment::class.java))
        }

        fun newIntent(mContext: Context): Intent {
            return Intent(mContext, MainPrayerFragment::class.java)
        }

        fun start(view: View) {
            val mContext = view.context
            mContext.startActivity(Intent(mContext, MainPrayerFragment::class.java))
        }
    }


    @OptIn(ExperimentalPagerApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.statusBarHeight?.height(Prefs.statusBarHeight, duration = 0)
        viewDataBinding?.appbar?.setContent {
            Column(
                modifier = Modifier,
            ) {
                Appbar(Modifier,
                    Modifier.padding(top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp),
                    backgroundColor = colorResource(id = android.R.color.transparent)).setTitle(
                    LocaleConstants.PRAYER.locale(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22
                ).setRightButton(
                    rightButtonImage = listOf(
                        R.drawable.ic_bookmarks
                    ), rightButtonHandler = listOf {
                        BookmarkActivity.startPrayer(requireActivity())
                    }
                ).setElevation(0).setSearch(LocaleConstants.SEARCH_PRAYER.locale()) {
                    startActivity(SearchActivity.newPrayerIntent(requireActivity()))
                }.build()
            }
        }

        viewDataBinding?.let { viewDataBinding ->
            viewDataBinding.pager.adapter =
                FragmentPagerAdapter(requireActivity() as AppCompatActivity,
                    arrayListOf<Fragment>().apply {
                        add(PrayerGridFragment.newInstance())
                        addAll(prayerTypes.map { PrayerListFragment.newInstance(it) })
                    })

            TabLayoutMediator(viewDataBinding.tab, viewDataBinding.pager) { tab, position ->
                if (position == 0) {
                    tab.customView =
                        TabImageCircleBinding.inflate(LayoutInflater.from(requireActivity()))
                            .apply {
                                image.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                                    R.drawable.ic_grid))
                            }.root
                } else {
                    tab.customView =
                        TabTextCircleBinding.inflate(LayoutInflater.from(requireActivity())).apply {
                            title.text = prayerTypes[position - 1].title.getText()
                        }.root
                }
            }.attach()
        }

        viewDataBinding?.pager?.registerOnPageChangeCallback(pageChangeCallback)

        viewModel.pagePosition.observe(viewLifecycleOwner) {
            viewDataBinding?.pager?.setCurrentItem(it, true)
        }
    }

    val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.pagePosition.value = position
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding?.pager?.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    override fun onBackEvent() {
        if (viewModel.pagePosition.value != 0) {
            viewModel.pagePosition.value = 0
        } else {
            RxBus.getDefault().send(MainTabEvent(MainTabType.HOME))
        }
    }

}
