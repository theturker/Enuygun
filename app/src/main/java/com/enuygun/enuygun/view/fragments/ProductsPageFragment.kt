package com.enuygun.enuygun.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.enuygun.enuygun.R
import com.enuygun.enuygun.adapter.ProductAdapter
import com.enuygun.enuygun.databinding.FragmentProductsPageBinding
import com.enuygun.enuygun.model.Product
import com.enuygun.enuygun.viewmodel.ProductViewModel

class ProductsPageFragment : Fragment() {
    private lateinit var binding: FragmentProductsPageBinding
    private val viewModel: ProductViewModel by viewModels()

    private lateinit var originalList: List<Product>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductsPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchProducts()
        viewModel.fetchBrands()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.brands.observe(viewLifecycleOwner) { brands ->
            setupFilterPopupMenu(brands)
        }

        productAdapter = ProductAdapter { selectedProduct ->
            val bundle = Bundle().apply {
                putInt("productId", selectedProduct.id)
            }
            navController =
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.action_to_productDetailFragment, bundle)
        }

        binding.recyclerView.adapter = productAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.products.observe(viewLifecycleOwner) { products ->
            if (!::originalList.isInitialized) {
                originalList = products
            }
            binding.infoTv.text = "Toplam " + products.size.toString() + " adet"
            productAdapter.setProducts(products)
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val query = editable.toString().trim()
                filterList(query)
            }
        })

        binding.sortRl.setOnClickListener {
            showSortPopupMenu()
        }
    }

    private fun showSortPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.sortRl)
        popupMenu.menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sort_expensive_to_cheap -> {
                    sortListByPriceDescending()
                    true
                }
                R.id.sort_cheap_to_expensive -> {
                    sortListByPriceAscending()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun sortListByPriceDescending() {
        val sortedList = originalList.sortedByDescending { it.price }
        updateRecyclerView(sortedList)
    }

    private fun sortListByPriceAscending() {
        val sortedList = originalList.sortedBy { it.price }
        updateRecyclerView(sortedList)
    }

    private fun filterList(query: String) {
        val filteredList = originalList.filter { it.title.contains(query, ignoreCase = true) }
        updateRecyclerView(filteredList)
    }

    private fun updateRecyclerView(newList: List<Product>) {
        productAdapter.setProducts(newList)
    }

    private fun setupFilterPopupMenu(brands: List<String>) {
        val filterPopupMenu = PopupMenu(requireContext(), binding.filterRl)
        filterPopupMenu.menu.add(Menu.NONE, Menu.NONE, 0, "All Brands")

        brands.forEachIndexed { index, brand ->
            filterPopupMenu.menu.add(Menu.NONE, index + 1, index + 1, brand)
        }

        filterPopupMenu.setOnMenuItemClickListener { item ->
            val selectedBrand = if (item.itemId == 0) null else brands[item.itemId - 1]
            filterListByBrand(selectedBrand)
            true
        }

        binding.filterRl.setOnClickListener {
            filterPopupMenu.show()
        }
    }

    private fun filterListByBrand(selectedBrand: String?) {
        val filteredList = if (selectedBrand.isNullOrEmpty()) {
            originalList
        } else {
            originalList.filter { it.brand == selectedBrand }
        }
        updateRecyclerView(filteredList)
    }
}
