package id.gritlabs.dzikirku.ui.note.composer.input

import id.gritlabs.dzikirku.base.BaseNavigator
import id.gritlabs.dzikirku.model.events.NoteFormat

interface NoteInputNavigator:BaseNavigator {
    fun onFormatEvent(format:NoteFormat)
    fun onNoteFocusEvent(focus:Boolean)
    fun onNoteInsertEvent(url:String)
}