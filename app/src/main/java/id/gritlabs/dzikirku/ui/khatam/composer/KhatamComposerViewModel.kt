package id.gritlabs.dzikirku.ui.khatam.composer

import androidx.lifecycle.MutableLiveData
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.Khatam
import id.gritlabs.dzikirku.model.KhatamStateConstants
import id.gritlabs.dzikirku.model.QuranLastRead
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class KhatamComposerViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {}

    val khatam:MutableLiveData<Khatam> = MutableLiveData()

    fun addNewKhatam(){
        khatam.value = khatam.value?.apply {
            iteration = iteration?.toCollection(arrayListOf())?.apply {
                add(QuranLastRead(
                    surah = 1,
                    ayah = 1,
                    page = null,
                    isSavedFromPage = null,
                    timestamp = null
                ).apply {
                    lap = iteration?.count()?.plus(1) ?: 1
                    state = KhatamStateConstants.INACTIVE
                })
            }
        }
    }

}