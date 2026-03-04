package com.gomguk.kirly.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gomguk.kirly.data.SectionInfo
import com.gomguk.kirly.databinding.ItemLoadingBinding
import com.gomguk.kirly.databinding.ItemSectionBinding

class MainAdapter(private var sectionInfoList: List<SectionInfo?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (sectionInfoList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else {
            val binding =
                ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
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

    class ItemViewHolder(private val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sectionInfo: SectionInfo) {
            binding.textView.text = sectionInfo.title
        }
    }

    class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}