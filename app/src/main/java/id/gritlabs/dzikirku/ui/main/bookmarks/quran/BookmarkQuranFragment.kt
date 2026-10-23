package id.gritlabs.dzikirku.ui.main.bookmarks.quran

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.data.room.dao.getAyahDao
import id.gritlabs.dzikirku.data.room.dao.getSurahDao
import id.gritlabs.dzikirku.databinding.FragmentBookmarkQuranBinding
import id.gritlabs.dzikirku.databinding.ItemBookmarkQuranBinding
import id.gritlabs.dzikirku.model.Bookmark
import id.gritlabs.dzikirku.ui.adapters.GenericAdapter
import id.gritlabs.dzikirku.util.LocaleProvider
import id.gritlabs.dzikirku.util.binding.ImageViewBinding.setTintColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookmarkQuranFragment : BaseFragment<FragmentBookmarkQuranBinding, BookmarkQuranViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_bookmark_quran
    override val viewModel: BookmarkQuranViewModel by viewModels()

    lateinit var genericAdapter: GenericAdapter<Bookmark, ItemBookmarkQuranBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        genericAdapter = GenericAdapter(ItemBookmarkQuranBinding::inflate, mListener1 = { bookmark, mBinding->

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

                }
            }
        })

        viewDataBinding?.recycler?.apply {
            adapter = genericAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }

        viewModel.bookmarks.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                genericAdapter.submitList(it)
            }
        }
    }
}