package com.trifonov.packship.network.endpoint

import com.trifonov.packship.network.model.supplier.Supplier
import com.trifonov.packship.network.model.supplier.SupplierBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SupplierEndpoint {
    @GET("suppliers")
    suspend fun getSuppliers() : List<Supplier>

    @POST("suppliers")
    suspend fun addSupplier(@Body supplierBody: SupplierBody)
}