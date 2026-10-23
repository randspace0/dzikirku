package id.gritlabs.dzikirku.ui.bsd.prayer

import androidx.databinding.ObservableField
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.PrayerBook
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerBSDViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<PrayerBSDNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    val book = ObservableField<PrayerBook>()
    fun prayer(bookId:String) = dataManager.getPrayer(bookId)

}