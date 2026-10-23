package id.gritlabs.dzikirku.ui.khatam.presenter

import androidx.lifecycle.lifecycleScope
import id.gritlabs.dzikirku.data.room.dao.getKhatamDao
import id.gritlabs.dzikirku.model.*
import id.gritlabs.dzikirku.ui.main.MainActivity
import id.gritlabs.dzikirku.util.getFirstDayOfHijriMonth
import id.gritlabs.dzikirku.util.getHijriMonthName
import id.gritlabs.dzikirku.util.getLastDayOfHijriMonth
import id.gritlabs.dzikirku.util.io
import kotlinx.coroutines.launch
import java.util.*

