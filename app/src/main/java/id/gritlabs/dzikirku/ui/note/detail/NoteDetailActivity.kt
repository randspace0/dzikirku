package id.gritlabs.dzikirku.ui.note.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseActivity
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.databinding.ActivityNoteDetailBinding
import id.gritlabs.dzikirku.model.Note
import id.gritlabs.dzikirku.ui.note.composer.metadata.NoteMetadataView
import id.gritlabs.dzikirku.util.Appbar
import id.gritlabs.dzikirku.util.setMarkdown
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteDetailActivity : BaseActivity<ActivityNoteDetailBinding, NoteDetailViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_note_detail
    override val viewModel: NoteDetailViewModel by viewModels()

    companion object {
        const val EXTRA_NOTE = "extra_note"

        fun newIntent(
            context: Context,
            note: Note? = null,
        ): Intent {
            return Intent(context, NoteDetailActivity::class.java).apply {
                putExtra(EXTRA_NOTE, note)
            }
        }

        fun NoteDetailActivity.getNote(): Note? {
            return intent.getParcelableExtra(EXTRA_NOTE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewDataBinding.lifecycleOwner = this

        getNote()?.let {
            viewModel.setData(it)
            configureViews(it)
        }
    }

    fun configureViews(note: Note) {
        viewDataBinding.composeView.setContent {
            metaData()
        }
        viewDataBinding.preview.movementMethod = LinkMovementMethod.getInstance()
        viewDataBinding.preview.textSize = Prefs.notesTextSize
        viewDataBinding.preview.setMarkdown(note.content.toString())
        viewDataBinding.preview.isClickable = true
    }

    override fun onSettingsEvent() {
        super.onSettingsEvent()
        viewDataBinding.preview.textSize = Prefs.notesTextSize
    }

    @Composable
    fun metaData() {
        var isExpanded by remember { mutableStateOf(false) }

        val rightButtonImage = mutableListOf<Int>()
        val rightButtonHandler = mutableListOf<(View?) -> Unit>()

        rightButtonImage.add(R.drawable.ic_info)
        rightButtonHandler.add {
            isExpanded = !isExpanded
        }

        Column(Modifier.animateContentSize()) {
            val appbar = Appbar(backgroundColor = colorResource(id = android.R.color.transparent))
                .withBackButton()
                .setTitle(if (isExpanded) "" else viewModel.title.value)
                .setElevation(0)
                .setRightButton(rightButtonImage = rightButtonImage,
                    rightButtonHandler = rightButtonHandler)

            appbar.build()

            if (isExpanded) {
                NoteMetadataView(
                    isPreview = true,
                    title = viewModel.title.value,
                    onTitleChange = {
                        viewModel.title.value = it
                    },
                    subtitle = viewModel.subtitle.value,
                    onSubtitleChange = {
                        viewModel.subtitle.value = it
                    },
                    date = viewModel.date.value,
                    onDateClicked = {
                    },
                    presenter = viewModel.presenter.value,
                    onPresenterClicked = {
                    },
                    location = viewModel.location.value,
                    onLocationClicked = {
                    },
                    tags = viewModel.tags,
                    onTagClicked = {
                    },
                    folder = viewModel.folder.value,
                    onFolderClicked = {
                    }
                )
            }
        }

    }
}