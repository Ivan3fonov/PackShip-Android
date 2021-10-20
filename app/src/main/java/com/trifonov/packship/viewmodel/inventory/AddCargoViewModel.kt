package com.trifonov.packship.viewmodel.inventory

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trifonov.packship.network.PackShipResponse
import com.trifonov.packship.network.model.cargo.CargoBody
import com.trifonov.packship.repository.InventoryRepository
import com.trifonov.packship.util.DispatcherProvider
import com.trifonov.packship.util.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class AddCargoViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val inventoryRepository: InventoryRepository,
    private val dispatcherProvider: DispatcherProvider

) : ViewModel() {

    val back = SingleLiveEvent<Unit>()

    val weight = MutableLiveData<String>()
    val price = MutableLiveData<String>()

    val inventoryId = savedStateHandle.getLiveData<String>("inventoryId")
    val containerId = savedStateHandle.getLiveData<String>("containerId")

    fun onSaveButtonClicked() {

        val weight = weight.value
        val price = price.value
        val inventoryId = inventoryId.value
        val containerId = containerId.value

        if (weight != null &&
            price != null &&
            inventoryId != null &&
            containerId != null
        ) {

            val cargo = CargoBody(weight.toDouble(), price.toDouble())

            viewModelScope.launch(dispatcherProvider.ui) {
                when (val response = inventoryRepository.addInventoryContainerCargo(
                    inventoryId,
                    containerId,
                    cargo
                )) {
                    is PackShipResponse.Success -> {

                        back.call()
                    }
                    is PackShipResponse.Error -> {
                        Timber.e(response.message)
                    }
                }
            }
        }
    }
}