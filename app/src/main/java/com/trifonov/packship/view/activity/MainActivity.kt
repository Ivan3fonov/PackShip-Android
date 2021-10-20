package com.trifonov.packship.view.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.exceptions.InvalidArgumentException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trifonov.packship.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.inventoriesFragment,
                R.id.shipmentFragment,
                R.id.suppliersFragment,
                R.id.containersFragment
            )
        )
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        try {
             BraintreeFragment.newInstance(this, "sandbox_4xskhsbr_jtqy8chkdrqbsy3v")
            // mBraintreeFragment is ready to use!
        } catch (e: InvalidArgumentException) {
            // There was an issue with your authorization string.
        }
    }
}