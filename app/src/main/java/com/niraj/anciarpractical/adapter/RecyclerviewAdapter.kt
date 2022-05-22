package com.niraj.anciarpractical.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.niraj.anciarpractical.BuildConfig
import com.niraj.anciarpractical.model.GeneralizedModel
import com.niraj.anciarpractical.model.GeneralizedModel.Companion.TYPE_HEADER
import com.niraj.anciarpractical.model.GeneralizedModel.Companion.TYPE_ITEM
import com.niraj.anciarpractical.custom_ui.StickyAdapter
import com.niraj.anciarpractical.databinding.HeaderItemBinding
import com.niraj.anciarpractical.databinding.ImagesAdapterViewBinding
import com.niraj.anciarpractical.model.ModelTypeItem
import com.niraj.anciarpractical.model.ModelTypeHeader

/**
 * Adapter class for Exam Review screen
 */
class RecyclerviewAdapter(dataList: MutableList<GeneralizedModel>,
                          private var headerPositionList: ArrayList<Int>, private val context: Context) :
    StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG: String = "ExamReviewFilterAdapter"
    }

    private var generalizedModelList: MutableList<GeneralizedModel> = dataList

    /**
     *@param holder instance of view holder
     * @param position int view position
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataItem = generalizedModelList[position]
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onBindViewHolder: ${dataItem.type()} $position")
        }
        if (dataItem.type() == TYPE_HEADER) {
            (holder as HeaderViewHolder).binding.titleTextView.text = (dataItem as ModelTypeHeader).title
            val layoutParam = holder.itemView.layoutParams as ViewGroup.LayoutParams
            if (layoutParam is StaggeredGridLayoutManager.LayoutParams) {
                layoutParam.isFullSpan = getIsFullSpan(TYPE_HEADER, position)
                holder.itemView.layoutParams = layoutParam
                ((holder.itemView.tag as RecyclerView).layoutManager as StaggeredGridLayoutManager).invalidateSpanAssignments()
            }
        } else {
            Glide.with(context).load((dataItem as ModelTypeItem).fileUrl)
                .into((holder as ItemViewHolder).binding.imageView)
            val layoutParam = holder.itemView.layoutParams as ViewGroup.LayoutParams
            if (layoutParam is StaggeredGridLayoutManager.LayoutParams) {
                layoutParam.isFullSpan = getIsFullSpan(TYPE_ITEM, position)
                holder.itemView.layoutParams = layoutParam
                ((holder.itemView.tag as RecyclerView).layoutManager as StaggeredGridLayoutManager).invalidateSpanAssignments()
            }
        }
    }

    /**
     * @param itemPosition
     * @return Int position
     */
    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        val headerPosition = generalizedModelList[itemPosition].headerPosition()
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getHeaderPositionForItem: $itemPosition $headerPosition")
        }
        return headerPosition
    }

    /**
     * @param holder
     * @param headerPosition
     */
    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?, headerPosition: Int) {
        (holder as HeaderViewHolder).binding.titleTextView.text = (generalizedModelList[headerPosition] as ModelTypeHeader).title
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return createViewHolder(parent, TYPE_HEADER)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val binding = HeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.root.tag = parent
            HeaderViewHolder(binding)
        } else {
            val binding = ImagesAdapterViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.root.tag = parent
            ItemViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return generalizedModelList[position].type()
    }

    override fun getItemCount(): Int {
        return generalizedModelList.size
    }

    class HeaderViewHolder internal constructor(var binding: HeaderItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class ItemViewHolder internal constructor(var binding: ImagesAdapterViewBinding) : RecyclerView.ViewHolder(binding.root)

    private fun getIsFullSpan(itemType: Int, position: Int): Boolean {
        if (TYPE_HEADER == itemType) {
            return true
        } else {
            var nextHeaderPosition = -1
            for (i in headerPositionList) {
                if (i > position) {
                    nextHeaderPosition = i
                    break
                }
            }
            var previousHeaderPosition = if (nextHeaderPosition == -1){
                headerPositionList[headerPositionList.size - 1]
            } else {
                headerPositionList[headerPositionList.indexOf(nextHeaderPosition) - 1]
            }
            if (previousHeaderPosition < 0) {
                previousHeaderPosition = 0
            }
            return (position - previousHeaderPosition) % 3 == 0
        }
    }
}