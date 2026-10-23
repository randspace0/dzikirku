package id.gritlabs.dzikirku.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.gritlabs.dzikirku.base.BaseEventViewHolder
import id.gritlabs.dzikirku.constants.LocaleConstants
import id.gritlabs.dzikirku.constants.LocaleConstants.locale
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.databinding.ItemReadHadithListBinding
import id.gritlabs.dzikirku.databinding.ItemReadHadithPagedBinding
import id.gritlabs.dzikirku.model.Hadith
import id.gritlabs.dzikirku.model.HadithData
import id.gritlabs.dzikirku.model.events.SettingsEvent
import id.gritlabs.dzikirku.util.RxBus
import id.gritlabs.dzikirku.util.StringExt.getArabic
import id.gritlabs.dzikirku.util.StringExt.getText
import id.gritlabs.dzikirku.util.setOnLongClickCopy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class HadithReadAdapter : ListAdapter<HadithData, BaseEventViewHolder>(BookDiff) {

    private var mListener: Callback? = null
    var isPager = false

    object BookDiff : DiffUtil.ItemCallback<HadithData>() {
        override fun areItemsTheSame(oldItem: HadithData, newItem: HadithData): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(oldItem: HadithData, newItem: HadithData): Boolean {
            return oldItem == newItem
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.dispose()
    }

    override fun onBindViewHolder(holder: BaseEventViewHolder, position: Int) {
        holder.onBind(position)
        compositeDisposable.add(
            RxBus.getDefault().toObservables()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ obj ->
                    holder.onEvent(obj)
                }, { it.printStackTrace() })
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseEventViewHolder {
        return if (isPager) {
            val viewBinding = ItemReadHadithPagedBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            HadithPagedViewHolder(viewBinding)
        } else {
            val viewBinding = ItemReadHadithListBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            HadithViewHolder(viewBinding)
        }
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectedItem(hadith: Hadith)
    }

    inner class HadithViewHolder(private val mBinding: ItemReadHadithListBinding) :
        BaseEventViewHolder(mBinding.root) {
        override fun onEvent(obj: Any) {
            when (obj) {
                is SettingsEvent -> {
                    onSettingsEvent()
                }
            }
        }

        fun onSettingsEvent() {
            val prefs = Prefs
            mBinding.content.arabic.textSize = prefs.arabicTextSize
            mBinding.content.arabic.visibility = if (prefs.isUseArabic) View.VISIBLE else View.GONE
            mBinding.content.translation.textSize = prefs.translationTextSize
            mBinding.content.narrated.textSize = prefs.translationTextSize
            mBinding.content.translation.visibility =
                if (prefs.isUseTranslation) View.VISIBLE else View.GONE
        }

        override fun onBind(position: Int) {
            val hadith = getItem(position)
            mBinding.content.index.text = "${LocaleConstants.HADITH.locale()} no. ${hadith.index}"
            mBinding.content.arabic.text = hadith.text.getArabic()
            mBinding.content.translation.text = hadith.text.getText()
            mBinding.content.narrated.text = hadith.narrated.getText()
            mBinding.content.narrated.isVisible = hadith.narrated.getText().isNotBlank()
            mBinding.content.googleTranslate.isVisible = Prefs.language == "bahasa"

            mBinding.content.arabic.setOnLongClickCopy()
            mBinding.content.translation.setOnLongClickCopy()
            mBinding.content.narrated.setOnLongClickCopy()

            onSettingsEvent()
        }
    }

    inner class HadithPagedViewHolder(private val mBinding: ItemReadHadithPagedBinding) :
        BaseEventViewHolder(mBinding.root) {
        override fun onEvent(obj: Any) {
            when (obj) {
                is SettingsEvent -> {
                    onSettingsEvent()
                }
            }
        }

        fun onSettingsEvent() {
            val prefs = Prefs
            mBinding.content.arabic.textSize = prefs.arabicTextSize
            mBinding.content.arabic.visibility = if (prefs.isUseArabic) View.VISIBLE else View.GONE
            mBinding.content.translation.textSize = prefs.translationTextSize
            mBinding.content.narrated.textSize = prefs.translationTextSize
            mBinding.content.translation.visibility =
                if (prefs.isUseTranslation) View.VISIBLE else View.GONE
        }

        override fun onBind(position: Int) {
            val hadith = getItem(position)
            mBinding.content.index.text = "${LocaleConstants.HADITH.locale()} no. ${hadith.index}"
            mBinding.content.arabic.text = hadith.text.getArabic()
            mBinding.content.translation.text = hadith.text.getText()
            mBinding.content.narrated.text = hadith.narrated.getText()
            mBinding.content.narrated.isVisible = hadith.narrated.getText().isNotBlank()
            mBinding.content.googleTranslate.isVisible = Prefs.language == "bahasa"

            mBinding.content.arabic.setOnLongClickCopy()
            mBinding.content.translation.setOnLongClickCopy()
            mBinding.content.narrated.setOnLongClickCopy()

            onSettingsEvent()
        }
    }
}