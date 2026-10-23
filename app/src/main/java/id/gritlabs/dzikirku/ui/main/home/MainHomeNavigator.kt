package id.gritlabs.dzikirku.ui.main.home

import id.gritlabs.dzikirku.base.BaseNavigator

interface MainHomeNavigator:BaseNavigator{
    fun onClickSearch()
    fun onClickBookmark()
    fun onClickSettings()
    fun onClickGrantPermission()
    fun onClickReadQuran()
    fun onClickPraytime()
}