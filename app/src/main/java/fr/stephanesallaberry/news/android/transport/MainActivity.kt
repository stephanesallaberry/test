package fr.stephanesallaberry.news.android.transport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.stephanesallaberry.news.android.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val startAuthForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            viewModel.changeUserConnectionStatus(
                connected = result.resultCode == Activity.RESULT_OK
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val mainNavHostContainer = supportFragmentManager.findFragmentById(
            R.id.mainNavHostContainer
        ) as NavHostFragment

        try {
            navController = mainNavHostContainer.navController

            // Setup the bottom navigation view with navController
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNavigationView.setupWithNavController(navController)
            // Setup the ActionBar with navController and 3 top level destinations
            appBarConfiguration = AppBarConfiguration(
                setOf(R.id.homeFragment, R.id.favoriteListFragment, R.id.accountFragment)
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
        } catch (exception: IllegalStateException) {
            Timber.e(exception)
        }

        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: MainScreenState) {
        Timber.d("render $state")
        if (!state.isConnected && !state.isLoading) {
            startAuthForResult.launch(Intent(this, AuthActivity::class.java))
            viewModel.changeLoadingStatus(loading = true)
        }
    }

    private fun handleSideEffect(sideEffect: MainScreenSideEffect) {
        when (sideEffect) {
            is MainScreenSideEffect.Toast -> Toast.makeText(
                this,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}
