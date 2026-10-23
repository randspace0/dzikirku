package id.gritlabs.dzikirku.ui.search.prayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.databinding.FragmentSearchPrayerBinding
import id.gritlabs.dzikirku.model.Prayer
import id.gritlabs.dzikirku.model.events.NoteInsertEvent
import id.gritlabs.dzikirku.ui.adapters.PrayerAdapter
import id.gritlabs.dzikirku.ui.read.ReadActivity
import id.gritlabs.dzikirku.util.RxBus
import id.gritlabs.dzikirku.util.StringExt.getText
import id.gritlabs.dzikirku.util.ViewUtils.isHidden
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchPrayerFragment : BaseFragment<FragmentSearchPrayerBinding, SearchPrayerViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_search_prayer
    override val viewModel: SearchPrayerViewModel by viewModels()

    @Inject
    lateinit var prayerAdapter: PrayerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewModel.result.observe(viewLifecycleOwner){
            viewDataBinding?.recycler?.isHidden(it.isEmpty())
            prayerAdapter.submitList(it)
        }
        viewDataBinding?.recycler?.apply {
            adapter = prayerAdapter.apply{
                isSearch()
                setListener(object:PrayerAdapter.Callback{
                    override fun onSelectedItem(prayer: Prayer) {
                        if (viewModel.isNoteDeeplink){
                            RxBus.getDefault().send(NoteInsertEvent("[${prayer.title?.getText()}](https://dzikirqu.com/prayer/${prayer.id}/)"))
                        } else{
                            ReadActivity.startPrayer(requireActivity(), prayer)
                        }
                        requireActivity().finish()
                    }
                })
            }
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }
}