package id.gritlabs.dzikirku.ui.read.quran.fragments.paged

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.databinding.FragmentReadQuranBinding
import id.gritlabs.dzikirku.ui.adapters.FragmentPagerAdapter
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsBSD
import id.gritlabs.dzikirku.ui.bsd.settings.SettingsConstants
import id.gritlabs.dzikirku.ui.read.ReadActivity
import id.gritlabs.dzikirku.ui.read.quran.ReadQuranNavigator
import id.gritlabs.dzikirku.ui.read.quran.ReadQuranViewModel
import id.gritlabs.dzikirku.ui.read.quran.paged.QuranPagedFragment
import id.gritlabs.dzikirku.util.ViewUtils.fadeHide
import id.gritlabs.dzikirku.util.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReadQuranPagedFragment(val page: Int? = null) :
    BaseFragment<FragmentReadQuranBinding, ReadQuranViewModel>(), BaseNavigator,
    ReadQuranNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_read_quran
    override val viewModel: ReadQuranViewModel by viewModels()

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this

        page?.let {
            configurePages(it)
        }
    }

    fun configurePages(page:Int){
        buildViews()
        viewDataBinding?.appbar?.fadeHide()
        viewDataBinding?.tab?.isVisible = false
        viewDataBinding?.pagedPager?.apply {
            val pages = arrayListOf<Int>().apply {
                for (i in 1..604) {
                    add(i)
                }
            }.reversed().toCollection(arrayListOf())
            val fragments = pages.map { page->
                QuranPagedFragment.newInstance(page)
            }
            adapter = FragmentPagerAdapter(requireActivity() as AppCompatActivity, fragments.toCollection(arrayListOf()))
            setCurrentItem(pages.indexOf(page), false)
        }
    }

    fun buildViews() {
        viewDataBinding?.pagedPager?.show()
//        viewDataBinding?.tab?.hide()
//        viewDataBinding?.appbar?.elevation = 2f
//        viewDataBinding?.recycler?.hide()
//        viewDataBinding?.progress?.hide()
//        viewDataBinding?.settings?.hide()
//        viewDataBinding?.footerContainer?.hide()
//        viewDataBinding?.footerShadow?.hide()
//        viewDataBinding?.lockContainer?.hide()
    }

    override fun onClickLastRead() {
        ReadActivity.startSurah(requireActivity(), Prefs.quranLastRead.surah, Prefs.quranLastRead.ayah)
        finish()
    }


    override fun onClickSettings() {
        SettingsBSD(arrayListOf(SettingsConstants.FONTS,SettingsConstants.QURAN)).show(childFragmentManager,"")
    }

    override fun onClickPage() {
    }

}


