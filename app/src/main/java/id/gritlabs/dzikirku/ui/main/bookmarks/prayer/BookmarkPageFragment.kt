package id.gritlabs.dzikirku.ui.main.bookmarks.prayer

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.data.room.dao.getAyahDao
import id.gritlabs.dzikirku.data.room.dao.getBookmarkDao
import id.gritlabs.dzikirku.data.room.dao.getPrayerDao
import id.gritlabs.dzikirku.data.room.dao.getSurahDao
import id.gritlabs.dzikirku.databinding.FragmentBookmarkPageBinding
import id.gritlabs.dzikirku.databinding.ItemBookmarkQuranBinding
import id.gritlabs.dzikirku.databinding.ItemPrayerBinding
import id.gritlabs.dzikirku.model.Bookmark
import id.gritlabs.dzikirku.model.BookmarkType
import id.gritlabs.dzikirku.model.Prayer
import id.gritlabs.dzikirku.ui.adapters.GenericAdapter
import id.gritlabs.dzikirku.ui.read.ReadActivity
import id.gritlabs.dzikirku.util.LocaleProvider
import id.gritlabs.dzikirku.util.StringExt.getText
import id.gritlabs.dzikirku.util.binding.ImageViewBinding.setTintColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookmarkPageFragment : BaseFragment<FragmentBookmarkPageBinding, BookmarkPageViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_bookmark_page
    override val viewModel: BookmarkPageViewModel by viewModels()

    lateinit var prayerAdapter: GenericAdapter<Prayer, ItemPrayerBinding>
    lateinit var quranAdapter: GenericAdapter<Bookmark, ItemBookmarkQuranBinding>
    lateinit var quranPage: GenericAdapter<Bookmark, ItemBookmarkQuranBinding>

    companion object{

        const val ARG_TYPE = "arg_type"

        fun newInstance(type:String):BookmarkPageFragment{
            return BookmarkPageFragment().apply {
                arguments = bundleOf(ARG_TYPE to type)
            }
        }

        fun BookmarkPageFragment.getType():String{
            return arguments?.getString(ARG_TYPE) ?: BookmarkType.QURAN
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        prayerAdapter = GenericAdapter(ItemPrayerBinding::inflate, mListener1 = { prayer, mBinding->
            mBinding.name.text = prayer.title?.getText()
            requireActivity().getBookmarkDao().getBookmarkById(prayer.id).observe(viewLifecycleOwner){
                it?.let { bookmark->
                    mBinding.bookmark.setTintColor(R.color.colorPrimary)
                    mBinding.bookmark.setOnClickListener {
                        viewModel.removeBookmark(prayer)
                    }
                    mBinding.highlight.isVisible = true
                    mBinding.highlight.setTintColor(if(bookmark.highlighted) R.color.colorPrimary else R.color.neutral_300)
                    mBinding.highlight.setOnClickListener {
                        viewModel.updateHighlight(bookmark)
                    }
                }?: kotlin.run {
                    mBinding.bookmark.setTintColor(R.color.neutral_300)
                    mBinding.bookmark.setOnClickListener {
                        viewModel.addBookmark(prayer)
                    }
                    mBinding.highlight.isVisible = false
                }
            }
            mBinding.root.setOnClickListener {
                ReadActivity.startPrayer(requireActivity(), prayer)
            }
        })

        quranAdapter = GenericAdapter(ItemBookmarkQuranBinding::inflate, mListener1 = { bookmark, mBinding->

            lifecycleScope.launch {
                val ayah = getAyahDao().getAyahById(bookmark.idString?.toInt() ?: 1)
                val surah = requireActivity().getSurahDao().getSurahById(ayah?.chapterId?: 1).firstOrNull()
                mBinding.surah.text = surah?.name
                mBinding.ayah.text = String.format(
                    LocaleProvider.getString(LocaleConstants.AYAH_N),
                    ayah?.verse_number
                )
                mBinding.bookmark.setTintColor(R.color.colorPrimary)
                mBinding.bookmark.setOnClickListener {
                    viewModel.removeBookmark(ayah?.id.toString())
                }
                mBinding.root.setOnClickListener {
                    ReadActivity.startSurah(requireActivity(), ayah?.chapterId, ayah?.verse_number)
                }
            }
        })

        viewDataBinding?.recycler?.apply {
            adapter = if (getType() == BookmarkType.QURAN) quranAdapter else prayerAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.quran.observe(viewLifecycleOwner){
            quranAdapter.submitList(it)
        }

        viewModel.prayer.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                prayerAdapter.submitList(it.map {
                    requireActivity().getPrayerDao().getPrayerByIdSuspend(it.idString.toString())
                        .firstOrNull()
                })
            }
        }

    }
}