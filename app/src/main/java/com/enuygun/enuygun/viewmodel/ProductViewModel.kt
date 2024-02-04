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
import java.io.Console

class ProductViewModel : ViewModel() {

    private val productService: ProductService = ApiClient.createService(ProductService::class.java)
    private val repository = ProductRepository(productService)

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _brands = MutableLiveData<List<String>>()
    val brands: LiveData<List<String>> = _brands

    private val compositeDisposable = CompositeDisposable()

    fun fetchProducts() {
        compositeDisposable.add(
            repository.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        result -> _products.value = result
                    },
                    {
                        error -> Log.e("ProductViewModel", "Error fetching products", error)
                    }
                )
        )
    }

    fun fetchBrands() {
        compositeDisposable.add(
            repository.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { products ->
                        val uniqueBrands = products.map { it.brand }.distinct()
                        _brands.value = uniqueBrands
                    },
                    { error ->
                        Log.e("ProductViewModel", "Error fetching brands", error)
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
