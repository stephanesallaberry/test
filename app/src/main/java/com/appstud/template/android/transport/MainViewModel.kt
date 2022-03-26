package com.appstud.template.android.transport

import androidx.lifecycle.ViewModel
import com.appstud.template.android.domain.external.AccountInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class MainViewModel(private val accountInteractor: AccountInteractor) :
    ContainerHost<MainScreenState, MainScreenSideEffect>, ViewModel() {

    override val container =
        container<MainScreenState, MainScreenSideEffect>(
            MainScreenState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: MainScreenState) = intent {
        val userState = accountInteractor.isConnected()
        reduce {
            state.copy(isConnected = userState, isLoading = false)
        }
    }

    fun changeUserConnectionStatus(connected: Boolean) = intent {
        reduce {
            state.copy(isConnected = connected, isLoading = false)
        }
    }

    fun changeLoadingStatus(loading: Boolean) = intent {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    fun logout() {
        accountInteractor.logout()
        changeUserConnectionStatus(false)
    }
}
