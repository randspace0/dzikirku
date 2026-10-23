package id.gritlabs.dzikirku.ui.bsd.quran.deeplink

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFullDialog
import id.gritlabs.dzikirku.databinding.ActivityQuranDeeplinkBinding
import id.gritlabs.dzikirku.model.Surah
import id.gritlabs.dzikirku.ui.adapters.SurahAdapter
import id.gritlabs.dzikirku.ui.jump.JumpQuranActivity
import id.gritlabs.dzikirku.ui.read.ReadActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuranDeeplinkBSD : BaseFullDialog<ActivityQuranDeeplinkBinding, QuranDeeplinkViewModel>(), QuranDeeplinkNavigator{

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_quran_deeplink
    override val viewModel: QuranDeeplinkViewModel by viewModels()

    @Inject
    lateinit var surahAdapter: SurahAdapter
    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.jump?.setOnClickListener {
            JumpQuranActivity.start(requireActivity(),true)
        }

        viewDataBinding?.surah?.apply {
            layoutManager = linearLayoutManager
            adapter = surahAdapter
        }
        surahAdapter.setListener(object:SurahAdapter.Callback{
            override fun onClickSurah(surah: Surah) {
                ReadActivity.startSurah(requireActivity(), surah.id, 1, true)
            }

            override fun onLongClickSurah(surah: Surah) {

            }
        })
        viewModel.surah.observe(viewLifecycleOwner){
            surahAdapter.submitList(it)
        }
    }

    override fun onDeeplinkEvent() {
        dismiss()
    }

}