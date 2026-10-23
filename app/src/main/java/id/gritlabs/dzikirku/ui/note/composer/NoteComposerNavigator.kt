package id.gritlabs.dzikirku.ui.note.composer

import id.gritlabs.dzikirku.base.BaseNavigator

interface NoteComposerNavigator:BaseNavigator {
    fun onEditorFocusEvent(focus:Boolean)
    fun onNoteEditorEvent()
}