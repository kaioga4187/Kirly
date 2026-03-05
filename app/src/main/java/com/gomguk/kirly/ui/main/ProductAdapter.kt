package com.gomguk.kirly.ui.main

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gomguk.kirly.R
import com.gomguk.kirly.data.Product
import com.gomguk.kirly.databinding.ItemProductSmallBinding
import com.gomguk.kirly.databinding.ItemProductVerticalBinding
import com.gomguk.kirly.util.PriceUtil

class ProductAdapter(
    private val viewType: Int = VIEW_TYPE_SMALL,
    private val onFavoriteClick: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_SMALL = 0
        const val VIEW_TYPE_VERTICAL = 1
    }

    private var items: List<Product> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<Product>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_VERTICAL -> {
                val binding = ItemProductVerticalBinding.inflate(layoutInflater, parent, false)
                ProductVerticalViewHolder(binding, onFavoriteClick)
            }
            else -> {
                val binding = ItemProductSmallBinding.inflate(layoutInflater, parent, false)
                ProductSmallViewHolder(binding, onFavoriteClick)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is ProductSmallViewHolder -> holder.bind(item)
            is ProductVerticalViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class ProductSmallViewHolder(
        private val binding: ItemProductSmallBinding,
        private val onFavoriteClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.nameTextView.text = product.name
            
            val priceInfo = PriceUtil.calculatePriceInfo(product.originalPrice, product.discountedPrice)
            binding.priceTextView.text = priceInfo.priceText
            
            if (priceInfo.showDiscount) {
                binding.discountPercentTextView.visibility = View.VISIBLE
                binding.discountPercentTextView.text = priceInfo.discountPercentText
                
                binding.discountPriceTextView.visibility = View.VISIBLE
                binding.discountPriceTextView.text = priceInfo.originalPriceText
                binding.discountPriceTextView.paintFlags = binding.discountPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.discountPercentTextView.visibility = View.GONE
                binding.discountPriceTextView.visibility = View.GONE
            }

            binding.heartButton.setImageResource(
                if (product.isFavorite) R.drawable.ic_btn_heart_on else R.drawable.ic_btn_heart_off
            )
            binding.heartButton.setOnClickListener {
                product.isFavorite = !product.isFavorite
                binding.heartButton.setImageResource(
                    if (product.isFavorite) R.drawable.ic_btn_heart_on else R.drawable.ic_btn_heart_off
                )
                onFavoriteClick(product)
            }

            Glide.with(binding.imageView)
                .load(product.image)
                .into(binding.imageView)
        }
    }

    class ProductVerticalViewHolder(
        private val binding: ItemProductVerticalBinding,
        private val onFavoriteClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.nameTextView.text = product.name
            
            val priceInfo = PriceUtil.calculatePriceInfo(product.originalPrice, product.discountedPrice)
            binding.priceTextView.text = priceInfo.priceText
            
            if (priceInfo.showDiscount) {
                binding.discountPercentTextView.visibility = View.VISIBLE
                binding.discountPercentTextView.text = priceInfo.discountPercentText
                
                binding.discountPriceTextView.visibility = View.VISIBLE
                binding.discountPriceTextView.text = priceInfo.originalPriceText
                binding.discountPriceTextView.paintFlags = binding.discountPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.discountPercentTextView.visibility = View.GONE
                binding.discountPriceTextView.visibility = View.GONE
            }

            binding.heartButton.setImageResource(
                if (product.isFavorite) R.drawable.ic_btn_heart_on else R.drawable.ic_btn_heart_off
            )
            binding.heartButton.setOnClickListener {
                product.isFavorite = !product.isFavorite
                binding.heartButton.setImageResource(
                    if (product.isFavorite) R.drawable.ic_btn_heart_on else R.drawable.ic_btn_heart_off
                )
                onFavoriteClick(product)
            }

            Glide.with(binding.imageView)
                .load(product.image)
                .into(binding.imageView)
        }
    }
}
