package com.trifonov.packship

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.trifonov.packship.extension.verify
import com.trifonov.packship.util.SingleLiveEvent
import com.trifonov.packship.viewmodel.inventory.InventoryItemViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class InventoryItemViewModelTest : BaseAppTest() {

    private lateinit var viewModel: InventoryItemViewModel

    private val onInventoryClicked = SingleLiveEvent<String>()

    override fun setUp() {

        viewModel = InventoryItemViewModel(onInventoryClicked)
    }

    @Test
    fun `test on item clicked`() {

        viewModel.bind(mock { on { identity } doReturn "id" })

        viewModel.onItemClicked()

        onInventoryClicked.verify("id")


    }

}