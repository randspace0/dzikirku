package id.gritlabs.dzikirku.ui.bsd.flyer

import androidx.lifecycle.MutableLiveData
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel

import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.Flyer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlyerBSDViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<BaseNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    val flyer = MutableLiveData<Flyer>()

}