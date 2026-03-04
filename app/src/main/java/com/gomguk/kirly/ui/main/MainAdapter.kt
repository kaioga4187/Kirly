package com.gomguk.kirly.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                VerticalViewHolder(binding)
            }
            VIEW_TYPE_HORIZONTAL -> {
                val binding = ItemSectionHorizontalBinding.inflate(layoutInflater, parent, false)
                HorizontalViewHolder(binding)
            }
            VIEW_TYPE_GRID -> {
                val binding = ItemSectionGridBinding.inflate(layoutInflater, parent, false)
                GridViewHolder(binding)
            }
            VIEW_TYPE_DEFAULT -> {
                val binding = ItemSectionDefaultBinding.inflate(layoutInflater, parent, false)
                DefaultViewHolder(binding)
            }
            else -> {
                val binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = sectionInfoList[position] ?: return
        when (holder) {
            is VerticalViewHolder -> holder.bind(item)
            is HorizontalViewHolder -> holder.bind(item)
            is GridViewHolder -> holder.bind(item)
            is DefaultViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int = sectionInfoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newSectionInfos: List<SectionInfo?>) {
        sectionInfoList = newSectionInfos
        notifyDataSetChanged()
    }

    class VerticalViewHolder(private val binding: ItemSectionVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sectionInfo: SectionInfo) {
            val innerBinding = ItemSectionBinding.bind(binding.root)
            innerBinding.textView.text = sectionInfo.title
        }
    }

    class HorizontalViewHolder(private val binding: ItemSectionHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        private val productAdapter = ProductAdapter()
        
        init {
            binding.recyclerView.apply {
                adapter = productAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        fun bind(sectionInfo: SectionInfo) {
            val innerBinding = ItemSectionBinding.bind(binding.root)
            innerBinding.textView.text = sectionInfo.title
            
            // TODO: sectionInfo.url 등을 이용해 실제 상품 데이터를 가져와서 productAdapter.submitList() 호출 필요
            // 현재는 UI 구조만 세팅되어 있습니다.
        }
    }

    class GridViewHolder(private val binding: ItemSectionGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sectionInfo: SectionInfo) {
            val innerBinding = ItemSectionBinding.bind(binding.root)
            innerBinding.textView.text = sectionInfo.title
        }
    }

    class DefaultViewHolder(private val binding: ItemSectionDefaultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sectionInfo: SectionInfo) {
            val innerBinding = ItemSectionBinding.bind(binding.root)
            innerBinding.textView.text = sectionInfo.title
        }
    }

    class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}
