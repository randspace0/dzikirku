package id.gritlabs.dzikirku.ui.bsd.khatam

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseDialog
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.data.room.dao.getSurahDao
import id.gritlabs.dzikirku.databinding.BsdKhatamFooterBinding
import id.gritlabs.dzikirku.ui.khatam.presenter.setUpKhatam
import id.gritlabs.dzikirku.util.openPagedQuran
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KhatamFooterBSD : BaseDialog<BsdKhatamFooterBinding, KhatamFooterViewModel>(), KhatamFooterNavigator{

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.bsd_khatam_footer
    override val viewModel: KhatamFooterViewModel by viewModels()

    companion object{
        const val ARG_SURAH = "arg_surah"
        const val ARG_AYAH = "arg_ayah"

        fun newInstance(surah:Int?, ayah:Int?):KhatamFooterBSD{
            return KhatamFooterBSD().apply {
                arguments = bundleOf(ARG_SURAH to surah, ARG_AYAH to ayah)
            }
        }

        fun KhatamFooterBSD.getAyah():Int{
            return arguments?.getInt(ARG_AYAH, 1) ?: 1
        }
        fun KhatamFooterBSD.getSurah():Int{
            return arguments?.getInt(ARG_SURAH, 1) ?: 1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launch {
            getSurahDao().getSurahByIdSingle(getSurah())?.let { surah->
                viewModel.readCurrentAyahInPagedMode.postValue(String.format(LocaleConstants.READ_N_IN_PAGED_MODE.locale(),
                    "${surah.name} ${getAyah()}"))
            }
        }

        setUpKhatam()
    }

    override fun onClickPage() {
        requireActivity().openPagedQuran(getSurah(),
            getAyah())
    }

}