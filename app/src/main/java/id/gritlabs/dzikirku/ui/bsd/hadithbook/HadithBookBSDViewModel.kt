package id.gritlabs.dzikirku.ui.bsd.hadithbook

import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HadithBookBSDViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<HadithBookBSDNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }
}