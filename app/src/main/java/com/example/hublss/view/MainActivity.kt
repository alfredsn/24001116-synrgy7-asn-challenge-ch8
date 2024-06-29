package com.example.hublss.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.hublss.R
import com.example.domain.datastore.DataStoreManager
import com.example.hublss.viewmodel.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val dataStoreManager: DataStoreManager by inject() // Inject DataStoreManager
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        lifecycleScope.launch {
            dataStoreManager.getLoginStatus(this@MainActivity).collect { isLoggedIn ->
                if (isLoggedIn) {
                    if (navController.currentDestination?.id == R.id.loginFragment) {
                        navController.navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                } else {
                    if (navController.currentDestination?.id == R.id.mainFragment) {
                        navController.navigate(R.id.action_mainFragment_to_loginFragment)
                    }
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.mainFragment) {
                    finish()
                } else {
                    navController.navigateUp()
                }
            }
        })

        navController.addOnDestinationChangedListener { _, _, _ ->
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isMainFragment = navController.currentDestination?.id == R.id.mainFragment

        menu?.setGroupVisible(R.id.menu_group_main, isMainFragment)
        supportActionBar?.setDisplayHomeAsUpEnabled(!isMainFragment && isMainFragment)

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_logout -> {
                loginViewModel.logout(
                    this,
                    onSuccess = {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.mainFragment, true)
                            .build()
                        navController.navigate(R.id.loginFragment, null, navOptions)
                    },
                    onError = {
                        Toast.makeText(this, "Logout failed", Toast.LENGTH_SHORT).show()
                    }
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}