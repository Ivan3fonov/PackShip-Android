package com.trifonov.packship.viewmodel.inventory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.trifonov.packship.network.PackShipResponse
import com.trifonov.packship.network.model.inventory.Inventory
import com.trifonov.packship.repository.InventoryRepository
import com.trifonov.packship.util.DispatcherProvider
import com.trifonov.packship.util.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class InventoriesViewModel @ViewModelInject constructor(
    private val inventoryRepository: InventoryRepository,
        private val dispatcherProvider: DispatcherProvider
) : ViewModel(), LifecycleObserver {

    //  val isLoading = MutableLiveData(false)

    val onInventoryClicked = SingleLiveEvent<String>()
    val inventories = MutableLiveData<List<Inventory>>()
    val addInventory = SingleLiveEvent<Unit>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun getInventories() {
        // isLoading.value = true
        viewModelScope.launch(dispatcherProvider.ui) {
            when (val response = inventoryRepository.getInventories()) {
                is PackShipResponse.Success -> {
                   Timber.e(response.data.toString())
                   inventories.value = response.data
                }
                is PackShipResponse.Error -> {
                    Timber.d(response.message)
                }
            }
            //  isLoading.value = false
        }
    }

    fun onMoreButtonClicked() {
        addInventory.call()
    }

}