package com.enuygun.enuygun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enuygun.enuygun.R
import com.enuygun.enuygun.model.Product


class ProductAdapter(private val onItemClick: (Product) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products: List<Product> = emptyList()

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnailImageView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)

        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.titleTextView.text = product.title
        holder.descriptionTextView.text = product.description
        holder.priceTextView.text = product.price.toString() + "TL"

        Glide.with(holder.itemView.context)
            .load(product.thumbnail)
            .into(holder.thumbnailImageView)

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setProducts(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

}
