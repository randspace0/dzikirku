package id.gritlabs.dzikirku.base

import androidx.core.graphics.Insets

interface BaseNavigator {
    fun finish()
    fun toast(message:String?)
    fun showLoading()
    fun hideLoading()
    fun onSettingsEvent()
    fun onApplyWindowEvent(insets:Insets)
}