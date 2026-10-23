package id.gritlabs.dzikirku.ui.bsd.prayerbook

import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.ui.bsd.prayer.PrayerBSDNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerBookBSDViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<PrayerBSDNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    val books = dataManager.getBooks()

}