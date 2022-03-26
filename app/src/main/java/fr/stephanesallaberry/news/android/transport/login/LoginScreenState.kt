package fr.stephanesallaberry.news.android.transport.login

import fr.stephanesallaberry.news.android.transport.utils.validation.FormValidator

data class LoginScreenState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val fieldValidationMap: Map<LoginScreenField, Any> = mapOf(),
    val isConnected: Boolean = false,
)

sealed class LoginScreenSideEffect {
    data class Toast(val textResource: Int) : LoginScreenSideEffect()
    data class FieldValidationError(val fieldError: FormValidator.FieldStatus<LoginScreenField>) :
        LoginScreenSideEffect()
}
