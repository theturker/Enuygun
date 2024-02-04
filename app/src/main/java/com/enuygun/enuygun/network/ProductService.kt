package com.enuygun.enuygun.network

import com.enuygun.enuygun.model.Product
import com.enuygun.enuygun.model.ProductResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    fun getProducts(): Single<ProductResponse>

    @GET("products/{productId}")
    fun getProductDetails(@Path("productId") productId: Int): Single<Product>

}

