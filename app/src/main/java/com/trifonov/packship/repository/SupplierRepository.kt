package com.trifonov.packship.repository

import com.trifonov.packship.network.PackShipResponse
import com.trifonov.packship.network.endpoint.SupplierEndpoint
import com.trifonov.packship.network.model.supplier.Supplier
import com.trifonov.packship.network.model.supplier.SupplierBody
import com.trifonov.packship.network.safeApiCall
import javax.inject.Inject

class SupplierRepository @Inject constructor(private val api: SupplierEndpoint) {

    suspend fun getSuppliers(): PackShipResponse<List<Supplier>> {
        return safeApiCall { api.getSuppliers() }
    }

    suspend fun addSuppliers(supplier: SupplierBody) : PackShipResponse<Unit> {
        return safeApiCall { api.addSupplier(supplier) }
    }
}