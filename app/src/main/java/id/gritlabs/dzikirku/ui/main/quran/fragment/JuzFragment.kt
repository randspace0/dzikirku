package id.gritlabs.dzikirku.ui.main.quran.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.databinding.FragmentJuzBinding
import id.gritlabs.dzikirku.model.Hizb
import id.gritlabs.dzikirku.model.Juz
import id.gritlabs.dzikirku.ui.adapters.JuzAdapter
import id.gritlabs.dzikirku.ui.read.ReadActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JuzFragment : BaseFragment<FragmentJuzBinding, JuzViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_juz
    override val viewModel: JuzViewModel by viewModels()

    @Inject
    lateinit var juzAdapter: JuzAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.recycler?.apply {
            layoutManager = linearLayoutManager
            adapter = juzAdapter
        }


        viewModel.juz.observe(viewLifecycleOwner){
            juzAdapter.clearItems()
            juzAdapter.addItems(it.toCollection(arrayListOf()))
        }

        juzAdapter.setListener(object:JuzAdapter.Callback{
            override fun onSelectJuz(juz: Juz) {
                ReadActivity.startJuz(requireActivity(),juz)
            }
            override fun onSelectHizb(hizb: Hizb) {
                ReadActivity.startHizb(requireActivity(),hizb )
            }

            override fun onTapExpand(juz: Juz) {
                viewModel.isShowHizbOnJuz.value = juz.juz
            }
        })
    }

}
