package id.gritlabs.dzikirku.ui.main.bookmarks.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.databinding.FragmentMainBookmarkBinding
import id.gritlabs.dzikirku.databinding.TabTextCircleBinding
import id.gritlabs.dzikirku.model.BookmarkType
import id.gritlabs.dzikirku.ui.adapters.FragmentPagerAdapter
import id.gritlabs.dzikirku.ui.main.bookmarks.prayer.BookmarkPageFragment
import id.gritlabs.dzikirku.util.Appbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookmarkMainFragment : BaseFragment<FragmentMainBookmarkBinding, BookmarkMainViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main_bookmark
    override val viewModel: BookmarkMainViewModel by viewModels()

    companion object{

        const val ARG_TYPE = "arg_type"

        fun start(context:Context){
            context.startActivity(Intent(context, BookmarkMainFragment::class.java))
        }

        fun BookmarkMainFragment.getType():String{
            return requireActivity().intent.getStringExtra("extra_type") ?: BookmarkType.QURAN
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.let { configureViews(it) }
    }

    fun configureViews(viewDataBinding: FragmentMainBookmarkBinding){
        viewDataBinding.lifecycleOwner = this

        viewDataBinding.appbar.setContent {
            Appbar().setTitle("Bookmarks", fontSize = 20).withBackButton().build()
        }

        val fragments = arrayListOf(BookmarkPageFragment.newInstance(BookmarkType.QURAN),BookmarkPageFragment.newInstance(BookmarkType.PRAYER))
        val titles = arrayListOf(LocaleConstants.QURAN.locale(), LocaleConstants.PRAYER.locale())

        viewDataBinding.pager.apply {
            adapter = FragmentPagerAdapter(
                requireActivity() as AppCompatActivity,
                if (getType() == BookmarkType.QURAN) fragments else fragments.reversed()
            )
        }
        TabLayoutMediator(viewDataBinding.tab, viewDataBinding.pager) { tab, position ->
            tab.customView = TabTextCircleBinding.inflate(LayoutInflater.from(requireActivity())).apply{
                title.text = if (getType() == BookmarkType.QURAN) titles[position] else titles.reversed()[position]
            }.root
        }.attach()
    }
}