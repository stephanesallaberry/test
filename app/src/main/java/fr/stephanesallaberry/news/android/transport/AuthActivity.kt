package fr.stephanesallaberry.news.android.transport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import fr.stephanesallaberry.news.android.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        findViewById<FragmentContainerView>(R.id.authNavHostContainer)
            .findNavController()
            .setGraph(R.navigation.auth_navigation)
    }
}
