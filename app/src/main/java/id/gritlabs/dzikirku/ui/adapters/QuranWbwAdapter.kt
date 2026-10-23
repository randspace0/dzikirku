package id.gritlabs.dzikirku.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import id.gritlabs.dzikirku.base.BaseViewHolder
import id.gritlabs.dzikirku.databinding.*
import id.gritlabs.dzikirku.model.AyahWbw

import id.gritlabs.dzikirku.util.StringExt.getArabic
import id.gritlabs.dzikirku.util.StringExt.getText


class QuranWbwAdapter : ListAdapter<AyahWbw, BaseViewHolder>(AyahWbwDiff) {

    object AyahWbwDiff : DiffUtil.ItemCallback<AyahWbw>() {
        override fun areItemsTheSame(
            oldItem: AyahWbw,
            newItem: AyahWbw
        ): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(
            oldItem: AyahWbw,
            newItem: AyahWbw
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemAyahWbwBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(private val mBinding: ItemAyahWbwBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val word = getItem(position)
            mBinding.arabic.text = word.text.getArabic()
            mBinding.translation.text = word.text.getText()
        }
    }
}