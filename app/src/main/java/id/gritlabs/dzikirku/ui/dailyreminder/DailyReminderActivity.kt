package id.gritlabs.dzikirku.ui.dailyreminder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.gritlabs.dzikirku.BR
import id.gritlabs.dzikirku.R
import id.gritlabs.dzikirku.base.BaseActivity
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.data.room.dao.getDailyReminderParentDao
import id.gritlabs.dzikirku.databinding.ActivityDailyReminderBinding
import id.gritlabs.dzikirku.model.DailyReminder.Companion.getStartTimeDate
import id.gritlabs.dzikirku.ui.main.home.DailyReminderCard
import id.gritlabs.dzikirku.util.Appbar
import id.gritlabs.dzikirku.util.verticalSpacer
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DailyReminderActivity : BaseActivity<ActivityDailyReminderBinding, DailyReminderViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_daily_reminder
    override val viewModel: DailyReminderViewModel by viewModels()

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, DailyReminderActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.composeView.setContent {

            val dailyReminders =
                getDailyReminderParentDao().getDailyReminder().observeAsState().value?.sortedBy {
                    it.getStartTimeDate().time
                } ?: listOf()

            Column {
                Appbar().setTitle(
                    LocaleConstants.DAILY_PRAYERS.locale(),
                )
                    .setElevation(3)
                    .withBackButton()
                    .build()
                LazyColumn(Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        verticalSpacer(height = 16)
                    }
                    items(dailyReminders) { dailyReminder ->
                        DailyReminderCard(this@DailyReminderActivity, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp), parent = dailyReminder)
                    }
                    item {
                        verticalSpacer(height = 16)
                    }
                }
            }
        }
    }
}