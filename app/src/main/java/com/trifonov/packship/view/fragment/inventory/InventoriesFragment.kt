package com.trifonov.packship.view.fragment.inventory

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.trifonov.packship.R
import com.trifonov.packship.adapter.inventory.InventoriesAdapter
import com.trifonov.packship.databinding.FragmentInventoriesBinding
import com.trifonov.packship.util.getNavigationResultLiveData
import com.trifonov.packship.viewmodel.inventory.InventoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class InventoriesFragment : Fragment() {

    private val viewModel: InventoriesViewModel by viewModels()

    private lateinit var binding: FragmentInventoriesBinding

    private lateinit var inventoriesAdapter: InventoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_inventories, container, false
        )

        lifecycle.addObserver(viewModel)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inventoriesAdapter = InventoriesAdapter(viewLifecycleOwner, viewModel.onInventoryClicked)

        binding.rcvInventories.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = inventoriesAdapter
        }

        viewModel.inventories.observe(viewLifecycleOwner) { inventories ->
            inventoriesAdapter.setInventories(inventories)
        }

        viewModel.addInventory.observe(viewLifecycleOwner) {

            val dropInRequest = DropInRequest().amount("22")
                .clientToken("sandbox_4xskhsbr_jtqy8chkdrqbsy3v")
            startActivityForResult(dropInRequest.getIntent(context), 1000);

//            findNavController()
//                .navigate(
//                    InventoriesFragmentDirections
//                        .actionInventoriesFragmentToAddInventoryFragment()
//                )
        }

        viewModel.onInventoryClicked.observe(viewLifecycleOwner) { id ->

            findNavController()
                .navigate(
                    InventoriesFragmentDirections
                        .actionInventoriesFragmentToInventoryContainersFragment(id)
                )
        }

        getNavigationResultLiveData<Unit>("NEW_INVENTORY")?.observe(viewLifecycleOwner, {
            viewModel.getInventories()
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_test, menu)

        val menuItem = menu.findItem(R.id.menu_preferences)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Type something..."

        val close: View = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        close.setBackgroundResource(R.drawable.ic_preference)
        val  searchPlate: View? = searchView.findViewById(androidx.appcompat.R.id.search_plate)
        if (searchPlate!=null) {
            context?.let { ContextCompat.getColor(it, R.color.white) }?.let {
                searchPlate.setBackgroundColor(
                    it
                )
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_preferences -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000) {
            when (resultCode) {
                RESULT_OK -> {
                    val result: DropInResult? =
                        data?.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)

                    Timber.e(result?.paymentMethodNonce.toString())
                    // use the result to update your UI and send the payment method nonce to your server
                }
                RESULT_CANCELED -> {
                    // the user canceled
                }
                else -> {
                    // handle errors here, an exception may be available in
                    val error = data?.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception?

                    Timber.e(error)
                }
            }
        }
    }
}