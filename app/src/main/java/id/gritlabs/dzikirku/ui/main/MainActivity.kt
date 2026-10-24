package id.gritlabs.dzikirku.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseActivity
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.data.room.dao.getNoteDao
import id.gritlabs.dzikirku.databinding.ActivityMainBinding
import id.gritlabs.dzikirku.model.events.ApplyWindowEvent
import id.gritlabs.dzikirku.model.events.BackEvent
import id.gritlabs.dzikirku.model.events.MainTabEvent
import id.gritlabs.dzikirku.model.events.MainTabType
import id.gritlabs.dzikirku.util.RxBus
import id.gritlabs.dzikirku.util.ViewUtils.fadeShow
import id.gritlabs.dzikirku.util.foreground.NoteBackupWorker
import id.gritlabs.dzikirku.util.praytimes.Praytime
import id.gritlabs.dzikirku.util.setCurrentItem
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, MainActivity::class.java))
        }

        fun newIntent(mContext: Context): Intent {
            return Intent(mContext, MainActivity::class.java)
        }

        fun start(view: View) {
            val mContext = view.context
            mContext.startActivity(Intent(mContext, MainActivity::class.java))
        }
    }

    lateinit var navView: BottomNavigationView

    var paddingTop = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        viewDataBinding.lifecycleOwner = this

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.BLACK
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(viewDataBinding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(insets.left, 0, insets.right, insets.bottom)
            RxBus.getDefault().send(ApplyWindowEvent(insets))
            Prefs.statusBarHeight = insets.top
            WindowInsetsCompat.CONSUMED
        }

        navView = viewDataBinding.navView
        navView.menu.add(0, 0, 0, LocaleConstants.HOME.locale()).apply {
            icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_home, null)
        }
        navView.menu.add(0, 1, 1, LocaleConstants.QURAN.locale()).apply {
            icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_quran, null)
        }
        navView.menu.add(0, 2, 2, LocaleConstants.PRAYER.locale()).apply {
            icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_book, null)
        }
        viewModel.tab.observe(this) {
            navView.setCurrentItem(it.id)
        }

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                0 -> {
                    RxBus.getDefault().send(MainTabEvent(MainTabType.HOME))
                    hideAll()
                    viewDataBinding.home.fadeShow(duration = 0)
                    true
                }
                1 -> {
                    hideAll()
                    viewDataBinding.quran.fadeShow(duration = 0)
                    RxBus.getDefault().send(MainTabEvent(MainTabType.QURAN))
                    true
                }
                2 -> {
                    hideAll()
                    viewDataBinding.prayer.fadeShow(duration = 0)
                    RxBus.getDefault().send(MainTabEvent(MainTabType.PRAYER))
                    true
                }
                else -> false;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        Praytime.configureForegroundService(this)
        updateSuspendedNote()
    }

    fun updateSuspendedNote(){
        if(Prefs.suspendedNoteId != -1){
            lifecycleScope.launch {
                getNoteDao().getNoteByIdSuspend(Prefs.suspendedNoteId)?.apply {
                    content = Prefs.suspendedNoteContent
                }?.let { getNoteDao().updateNote(it) }
                Prefs.suspendedNoteId = -1
                Prefs.suspendedNoteContent = ""
            }
        }
    }


    fun hideAll() {
        viewDataBinding.home.isVisible = false
        viewDataBinding.quran.isVisible = false
        viewDataBinding.prayer.isVisible = false
    }

    val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
            RxBus.getDefault().toObservables()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ obj ->
                    if (obj is MainTabEvent) {
                        navView.setCurrentItem(obj.type.id)
                    }
                }, { it.printStackTrace() })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        NoteBackupWorker.start(this)
        compositeDisposable.dispose()
    }

    override fun onBackPressed() {
        if (navView.selectedItemId == 2) {
            RxBus.getDefault().send(BackEvent())
        } else if (navView.selectedItemId != 0) {
            RxBus.getDefault().send(MainTabEvent(MainTabType.HOME))
        } else {
            super.onBackPressed()
        }
    }

}