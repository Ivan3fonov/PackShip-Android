package com.trifonov.packship.viewmodel.shipment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.trifonov.packship.R
import com.trifonov.packship.network.model.shipment.Shipment
import com.trifonov.packship.util.SingleLiveEvent

class ShipmentItemViewModel(private val onShipmentClicked: SingleLiveEvent<String>) : ViewModel() {

    private val shipment = MutableLiveData<Shipment>()

    val revenue = shipment.map { it.revenue.toString() }

    val transportationExpenses = shipment.map { it.transportationExpenses.toString() }

    val statusIcon = shipment.map {
        when(it.status) {
            "Undistributed" -> R.drawable.ic_undistributed_shipment
            else -> R.drawable.ic_distributed_shipment
        }
    }

    val packedContainers = shipment.map { it.packedContainers.toString() }

    val assignments = shipment.map { it.assignments.toString() }

    fun bind(item: Shipment) {
        shipment.value = item
    }

    fun onContainerClicked() {
        shipment.value?.let {
            onShipmentClicked.value = it.identity
        }
    }

}