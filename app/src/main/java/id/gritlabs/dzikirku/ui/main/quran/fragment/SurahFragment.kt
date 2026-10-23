package id.gritlabs.dzikirku.ui.main.quran.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.databinding.FragmentSurahBinding
import id.gritlabs.dzikirku.model.Surah
import id.gritlabs.dzikirku.ui.adapters.SurahAdapter
import id.gritlabs.dzikirku.ui.bsd.StartReadQuranBSD
import id.gritlabs.dzikirku.ui.read.ReadActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SurahFragment : BaseFragment<FragmentSurahBinding, SurahViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_surah
    override val viewModel: SurahViewModel by viewModels()

    @Inject
    lateinit var surahAdapter: SurahAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    companion object {
        const val ARG_IS_NOTE_DEEPLINK = "arg_is_note_deeplink"

        fun newInstance(isNoteDeeplink: Boolean): SurahFragment {
            return SurahFragment().apply {
                arguments = bundleOf(ARG_IS_NOTE_DEEPLINK to isNoteDeeplink)
            }
        }

        fun SurahFragment.isNoteDeeplink(): Boolean {
            return arguments?.getBoolean(ARG_IS_NOTE_DEEPLINK, false) ?: false
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.recycler?.apply {
            layoutManager = linearLayoutManager
            adapter = surahAdapter
        }

        viewModel.surah.observe(requireActivity()) {
            viewModel.isLoading.value = false
            surahAdapter.submitList(it)
        }

        surahAdapter.setListener(object : SurahAdapter.Callback {
            override fun onClickSurah(surah: Surah) {

                if (isNoteDeeplink()){
                    ReadActivity.startSurah(requireActivity(), surah.id, 1, isNoteDeeplink())
                    return
                }


                StartReadQuranBSD.newInstantInstance(surah = surah.id, ayah = 1).show(childFragmentManager, "")
            }

            override fun onLongClickSurah(surah: Surah) {
                StartReadQuranBSD.newSelectionInstance(surah = surah.id, ayah = 1).show(childFragmentManager, "")
            }
        })
    }
}