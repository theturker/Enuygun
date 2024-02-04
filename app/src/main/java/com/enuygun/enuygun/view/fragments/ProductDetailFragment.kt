package com.enuygun.enuygun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.enuygun.enuygun.databinding.FragmentProductDetailBinding
import com.enuygun.enuygun.viewmodel.ProductDetailViewModel

class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val productId = it.getInt("productId", -1)

            viewModel.fetchProductDetails(productId)

            viewModel.selectedProduct.observe(viewLifecycleOwner) { product ->
                binding.titleTextView.text = product.title
                binding.descriptionTextView.text = product.description
                binding.priceTextView.text = product.price.toString() + "TL"
                Glide.with(requireContext())
                    .load(product.thumbnail)
                    .into(binding.thumbnailImageView)
            }
        }
    }
}
