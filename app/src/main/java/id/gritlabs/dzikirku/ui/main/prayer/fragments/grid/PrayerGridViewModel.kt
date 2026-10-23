package id.gritlabs.dzikirku.ui.main.prayer.fragments.grid

import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerGridViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<PrayerGridNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }



}