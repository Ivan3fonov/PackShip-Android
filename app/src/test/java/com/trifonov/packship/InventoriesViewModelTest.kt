package com.trifonov.packship

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.trifonov.packship.extension.runBlockingTest
import com.trifonov.packship.extension.verify
import com.trifonov.packship.extension.verifyCalled
import com.trifonov.packship.network.PackShipResponse
import com.trifonov.packship.network.model.inventory.Inventory
import com.trifonov.packship.repository.InventoryRepository
import com.trifonov.packship.util.DispatcherProvider
import com.trifonov.packship.viewmodel.inventory.InventoriesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.Mock


@ExperimentalCoroutinesApi
class InventoriesViewModelTest : BaseAppTest() {

    @Mock
    private lateinit var packShipRepository: InventoryRepository

    @Mock
    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var lifecycleRegistry: LifecycleRegistry

    private lateinit var viewModel: InventoriesViewModel

    companion object {
        private val TEST_INVETORY_ID = "test_id"
    }

    override fun setUp() {
        viewModel = InventoriesViewModel(
            packShipRepository, dispatcherProvider
        )

        lifecycleRegistry = LifecycleRegistry(mock())

        lifecycleRegistry.addObserver(viewModel)
    }

    @Test
    fun `test successfully fetched inventories`() =
        mainCoroutineRule.runBlockingTest {

            val inventory = mock<Inventory>()
            val inventory2 = mock<Inventory>()

            val inventories = listOf(inventory, inventory2)

            whenever(dispatcherProvider.ui).thenReturn(mainCoroutineRule.testDispatcher)
            whenever(packShipRepository.getInventories()) doReturn
                    PackShipResponse.Success(inventories)

            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)

            viewModel.inventories.verify(inventories)
        }

    @Test
    fun `test add inventory called`() {
        viewModel.onMoreButtonClicked()

        viewModel.addInventory.verifyCalled()
    }

    @Test
    fun `test on inventory clicked`() {

        viewModel.onInventoryClicked.value = TEST_INVETORY_ID

        viewModel.onInventoryClicked.verify(TEST_INVETORY_ID)
    }
}