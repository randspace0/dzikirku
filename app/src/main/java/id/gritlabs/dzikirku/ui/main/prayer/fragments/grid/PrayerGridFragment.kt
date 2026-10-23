package id.gritlabs.dzikirku.ui.main.prayer.fragments.grid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseFragment
import id.gritlabs.dzikirku.databinding.FragmentPrayerGridBinding
import id.gritlabs.dzikirku.model.events.PrayerTypeEvent
import id.gritlabs.dzikirku.ui.adapters.PrayerTypeGridAdapter
import id.gritlabs.dzikirku.ui.main.prayer.PrayerType
import id.gritlabs.dzikirku.util.RxBus
import id.gritlabs.dzikirku.util.prayerTypes
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PrayerGridFragment : BaseFragment<FragmentPrayerGridBinding, PrayerGridViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_prayer_grid
    override val viewModel: PrayerGridViewModel by viewModels()

    companion object {
        fun newInstance(): PrayerGridFragment {
            return PrayerGridFragment()
        }
    }

    @Inject
    lateinit var prayerTypeGridAdapter: PrayerTypeGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.recycler?.apply {
            layoutManager = GridLayoutManager(requireActivity(), 4)
            adapter = prayerTypeGridAdapter
            prayerTypeGridAdapter.submitList(prayerTypes)
            prayerTypeGridAdapter.setListener(object:PrayerTypeGridAdapter.Callback{
                override fun onSelectedItem(type: PrayerType) {
                    RxBus.getDefault().send(PrayerTypeEvent(type))
                }
            })
        }

    }

}