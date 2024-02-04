package com.enuygun.enuygun.repository

import com.enuygun.enuygun.model.Product
import com.enuygun.enuygun.network.ProductService
import io.reactivex.rxjava3.core.Single

class ProductRepository(private val productService: ProductService) {

    fun getProducts(): Single<List<Product>> {
        return productService.getProducts()
            .map { it.products }
    }

    fun getProductDetails(productId: Int): Single<Product> {
        return productService.getProductDetails(productId)
    }
}
