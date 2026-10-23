package id.gritlabs.dzikirku.ui.khatam.fragments

import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KhatamDetailViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<KhatamDetailNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }
}