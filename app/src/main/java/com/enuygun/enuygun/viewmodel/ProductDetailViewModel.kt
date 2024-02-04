package com.enuygun.enuygun.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enuygun.enuygun.model.Product
import com.enuygun.enuygun.network.ApiClient
import com.enuygun.enuygun.network.ProductService
import com.enuygun.enuygun.repository.ProductRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ProductDetailViewModel : ViewModel() {
    private val productService: ProductService = ApiClient.createService(ProductService::class.java)
    private val repository = ProductRepository(productService)

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> = _selectedProduct

    private val compositeDisposable = CompositeDisposable()

    fun fetchProductDetails(productId: Int) {
        compositeDisposable.add(
            repository.getProductDetails(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        _selectedProduct.value = result
                    },
                    { error ->
                        Log.e("ProductDetailViewModel", "Error fetching product details", error)
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
