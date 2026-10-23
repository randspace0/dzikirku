package id.gritlabs.dzikirku.model.events

data class OnBoardEvent(val type:OnBoardType)

enum class OnBoardType {
    LANGUAGE,
    LOCATION,
    PRAYTIME,
}