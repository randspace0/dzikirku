package id.gritlabs.dzikirku.ui.read.prayer

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.data.room.dao.BookmarkDao
import id.gritlabs.dzikirku.data.room.dao.PrayerDao
import id.gritlabs.dzikirku.databinding.FragmentReadPrayerBinding
import id.gritlabs.dzikirku.model.Bookmark
import id.gritlabs.dzikirku.model.BookmarkHighlightUpdate
import id.gritlabs.dzikirku.model.BookmarkType
import id.gritlabs.dzikirku.model.Prayer
import id.gritlabs.dzikirku.ui.adapters.PrayerReadAdapter
import id.gritlabs.dzikirku.ui.bsd.TextBSD
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsBSD
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsConstants
import id.gritlabs.dzikirku.util.Appbar
import id.gritlabs.dzikirku.util.StringExt.getText
import id.gritlabs.dzikirku.util.binding.ImageViewBinding.setTintColor
import id.gritlabs.dzikirku.util.io
import id.gritlabs.dzikirku.util.main
import id.gritlabs.dzikirku.util.start
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ReadPrayerFragment :
    BaseFragment<FragmentReadPrayerBinding, ReadPrayerViewModel>(),
    ReadPrayerNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_read_prayer
    override val viewModel: ReadPrayerViewModel by viewModels()

    @Inject
    lateinit var prayerReadAdapter: PrayerReadAdapter

    @Inject
    lateinit var mBookmarkDao: BookmarkDao

    @Inject
    lateinit var mPrayerDao: PrayerDao


    companion object {
        const val ARG_PRAYER_ID = "arg_prayer_id"
        fun newInstance(prayerId: String): Fragment {
            return ReadPrayerFragment().apply {
                arguments = bundleOf(ARG_PRAYER_ID to prayerId)
            }
        }

        fun ReadPrayerFragment.getPrayerId(): String? {
            return arguments?.getString(ARG_PRAYER_ID)
        }
    }

    var pageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                refreshReciteTextView(position)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this
        viewModel.prayerId.value = getPrayerId()
        observePrayerData()
        setUpPager()

        lifecycleScope.launch {
            mBookmarkDao.getBookmarkById(getPrayer().id)
                .observe(viewLifecycleOwner) {
                    configureBookmark(it)
                }
        }

        viewDataBinding?.toolbar?.setContent {
            Appbar()
                .withBackButton()
                .setTitle(viewModel.bookName.value, 16)
                .setSubtitle(viewModel.prayerName.value, 14)
                .setPadding(8)
                .isCentered()
                .setRightButton(rightButtonTitle = listOf(),
                    rightButtonImage = listOf(R.drawable.ic_settings),
                    rightButtonHandler = listOf {
                        onClickSettings()
                    }).build()
        }
    }

    fun configureBookmark(bookmark: Bookmark?) {
        val isBookmarked = bookmark != null
        viewDataBinding?.pin?.isVisible = isBookmarked
        viewDataBinding?.pin?.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.white))
        viewDataBinding?.pin?.setTintColor(if (bookmark?.highlighted == true) R.color.colorPrimary else R.color.neutral_400)
        viewDataBinding?.bookmark?.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.white))
        viewDataBinding?.bookmark?.setTintColor(if (isBookmarked) R.color.colorPrimary else R.color.neutral_400)

        viewDataBinding?.pin?.setOnClickListener {
            io {
                bookmark?.let {
                    mBookmarkDao.updateHighlight(BookmarkHighlightUpdate(bookmark.id ?: 0,
                        !it.highlighted))
                }
            }
        }

        io {
            val prayer = getPrayer()
            main {
                viewDataBinding?.bookmark?.setOnClickListener {
                    io {
                        if (!isBookmarked) {
                            mBookmarkDao.putBookmark(
                                Bookmark(
                                    idString = prayer.id,
                                    type = BookmarkType.PRAYER
                                )
                            )
                        } else {
                            mBookmarkDao.deleteBookmark(prayer.id, BookmarkType.PRAYER)
                        }
                    }
                }
            }
        }
    }

    suspend fun getPrayer(): Prayer {
        return mPrayerDao.getPrayerByIdSuspend(getPrayerId() ?: "").firstOrNull() ?: Prayer(
            id = "",
            book_id = null,
            order = null,
            title = null
        )
    }

    fun setUpPager() {
        viewDataBinding?.pager?.adapter = prayerReadAdapter
        TabLayoutMediator(viewDataBinding?.tab!!, viewDataBinding?.pager!!) { tab, position ->
            tab.text = (position + 1).toString()
        }.attach()
        viewDataBinding?.pager?.registerOnPageChangeCallback(pageChangeCallback)
        viewDataBinding?.tab?.tabMode = TabLayout.MODE_AUTO
    }

    fun observePrayerData() {
        viewModel.prayerData.observe(viewLifecycleOwner) {
            viewModel.onSuccessPrayerData(it)
            refreshReciteTextView(0)
        }
    }

    fun refreshReciteTextView(position: Int) {
        val data = viewModel.prayerData
        if (data.value?.isEmpty() == true) return
        if (data.value?.get(position)?.notes?.getText()?.isNotBlank() == true) {
            viewDataBinding?.bottomContainer?.setBackgroundColor(ContextCompat.getColor(
                requireActivity(),
                R.color.neutral_200))
            viewDataBinding?.recite?.isVisible = true
            viewDataBinding?.recite?.text = data.value?.get(position)?.notes?.getText()
            viewDataBinding?.recite?.setOnClickListener {
                data.value?.get(position)?.link?.let {
                    it.start(requireActivity())
                } ?: kotlin.run {
                    TextBSD.newInstance(data.value?.get(position)?.notes?.getText())
                        .show(childFragmentManager, "")
                }
            }
        } else {
            viewDataBinding?.bottomContainer?.setBackgroundColor(0)
            viewDataBinding?.recite?.visibility = View.INVISIBLE
        }
    }

    override fun onClickSettings() {
        SettingsBSD(arrayListOf(SettingsConstants.FONTS)).show(childFragmentManager, "")
    }
}


