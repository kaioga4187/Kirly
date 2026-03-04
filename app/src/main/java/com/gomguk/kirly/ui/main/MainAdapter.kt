package com.gomguk.kirly.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.gomguk.kirly.data.SectionInfo
import com.gomguk.kirly.data.SectionType
import com.gomguk.kirly.databinding.ItemLoadingBinding
import com.gomguk.kirly.databinding.ItemSectionDefaultBinding
import com.gomguk.kirly.databinding.ItemSectionGridBinding
import com.gomguk.kirly.databinding.ItemSectionHorizontalBinding
import com.gomguk.kirly.databinding.ItemSectionVerticalBinding
import com.gomguk.kirly.databinding.ItemSectionBinding

class MainAdapter(private var sectionInfoList: List<SectionInfo?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_VERTICAL = 1
        private const val VIEW_TYPE_HORIZONTAL = 2
        private const val VIEW_TYPE_GRID = 3
        private const val VIEW_TYPE_DEFAULT = 4
    }

    override fun getItemViewType(position: Int): Int {
        val item = sectionInfoList[position] ?: return VIEW_TYPE_LOADING
        return when (item.type) {
            is SectionType.Vertical -> VIEW_TYPE_VERTICAL
            is SectionType.Horizontal -> VIEW_TYPE_HORIZONTAL
            is SectionType.Grid -> VIEW_TYPE_GRID
            else -> VIEW_TYPE_DEFAULT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_VERTICAL -> {
                val binding = ItemSectionVerticalBinding.inflate(layoutInflater, parent, false)
                ItemViewHolder(binding)
            }
            VIEW_TYPE_HORIZONTAL -> {
                val binding = ItemSectionHorizontalBinding.inflate(layoutInflater, parent, false)
                ItemViewHolder(binding)
            }
            VIEW_TYPE_GRID -> {
                val binding = ItemSectionGridBinding.inflate(layoutInflater, parent, false)
                ItemViewHolder(binding)
            }
            VIEW_TYPE_DEFAULT -> {
                val binding = ItemSectionDefaultBinding.inflate(layoutInflater, parent, false)
                ItemViewHolder(binding)
            }
            else -> {
                val binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            sectionInfoList[position]?.let { holder.bind(it) }
        }
    }

    override fun getItemCount(): Int = sectionInfoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newSectionInfos: List<SectionInfo?>) {
        sectionInfoList = newSectionInfos
        notifyDataSetChanged()
    }

    class ItemViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sectionInfo: SectionInfo) {
            // 모든 item_section_*.xml은 item_section.xml을 include하고 있으므로,
            // 바인딩 객체에서 내부의 TextView에 접근할 수 있습니다.
            // include된 레이아웃의 ID가 지정되지 않았다면, root 뷰를 통해 접근하거나 
            // binding 객체의 타입을 체크하여 처리할 수 있습니다.
            // 여기서는 공통 인터페이스인 ItemSectionBinding을 활용하는 것이 좋습니다.
            val innerBinding = ItemSectionBinding.bind(binding.root)
            innerBinding.textView.text = sectionInfo.title
        }
    }

    class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}
