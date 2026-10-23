package id.gritlabs.dzikirku.ui.main.prayer

import androidx.lifecycle.MutableLiveData
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.events.ApplyWindowEvent
import id.gritlabs.dzikirku.model.events.BackEvent
import id.gritlabs.dzikirku.model.events.PrayerTypeEvent
import id.gritlabs.dzikirku.util.prayerTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainPrayerViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<MainPrayerNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is PrayerTypeEvent->{
                pagePosition.value = prayerTypes.indexOfFirst { it.title == obj.type.title } + 1
            }
            is BackEvent->{
                navigator?.onBackEvent()
            }
            is ApplyWindowEvent->{
                navigator?.onApplyWindowEvent(obj.insets)
            }
        }
    }

    var pagePosition = MutableLiveData(0)

}