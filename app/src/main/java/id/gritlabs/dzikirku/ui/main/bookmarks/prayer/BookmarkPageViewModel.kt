package id.gritlabs.dzikirku.ui.main.bookmarks.prayer


import androidx.lifecycle.viewModelScope
import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.base.BaseViewModel
import id.gritlabs.dzikirku.data.AppDataManager
import id.gritlabs.dzikirku.model.Bookmark
import id.gritlabs.dzikirku.model.BookmarkHighlightUpdate
import id.gritlabs.dzikirku.model.BookmarkType
import id.gritlabs.dzikirku.model.Prayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkPageViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }

    val quran = dataManager.mBookmarkDatabase.bookmarkDao().getBookmarksByType(BookmarkType.QURAN)
    val prayer = dataManager.mBookmarkDatabase.bookmarkDao().getBookmarksByType(BookmarkType.PRAYER)

    fun addBookmark(prayer: Prayer){
        viewModelScope.launch {
            dataManager.mBookmarkDatabase.bookmarkDao().putBookmark(
                Bookmark(
                    idString = prayer.id,
                    type = BookmarkType.PRAYER
                )
            )
        }
    }

    fun removeBookmark(prayer: Prayer){
        viewModelScope.launch {
            dataManager.mBookmarkDatabase.bookmarkDao().deleteBookmark(prayer.id, BookmarkType.PRAYER)
        }
    }

    fun removeBookmark(ayahId:String){
        viewModelScope.launch {
            dataManager.mBookmarkDatabase.bookmarkDao().deleteBookmark(ayahId, BookmarkType.QURAN)
        }
    }

    fun updateHighlight(bookmark: Bookmark){
        viewModelScope.launch {
            dataManager.mBookmarkDatabase.bookmarkDao().updateHighlight(BookmarkHighlightUpdate(bookmark.id ?: 0,
                !bookmark.highlighted))
        }
    }

}