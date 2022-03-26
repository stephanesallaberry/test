package fr.stephanesallaberry.news.android.transport.login

import androidx.lifecycle.ViewModel
import fr.stephanesallaberry.news.android.R
import fr.stephanesallaberry.news.android.domain.external.AccountInteractor
import fr.stephanesallaberry.news.android.domain.external.entity.AsyncResult
import fr.stephanesallaberry.news.android.domain.external.entity.UserAccount
import fr.stephanesallaberry.news.android.transport.utils.validation.EmailValidator
import fr.stephanesallaberry.news.android.transport.utils.validation.FormValidator
import fr.stephanesallaberry.news.android.transport.utils.validation.PasswordValidator
import fr.stephanesallaberry.news.android.transport.utils.validation.ValueValidator
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class LoginViewModel(
    private val accountInteractor: AccountInteractor,
    private val emailValidator: ValueValidator<String, EmailValidator.Status>,
    private val passwordValidator: ValueValidator<String, PasswordValidator.Status>,
) :
    ContainerHost<LoginScreenState, LoginScreenSideEffect>, ViewModel() {

    override val container = container<LoginScreenState, LoginScreenSideEffect>(
        LoginScreenState(),
        onCreate = ::onCreate
    )

    val stateFlow = this.container.stateFlow

    private fun onCreate(initialState: LoginScreenState) = intent {
//        test()
    }

    fun changeEmail(newEmail: String) = intent {
        reduce {
            state.copy(email = newEmail)
        }
    }

    fun changePassword(newPassword: String) = intent {
        reduce {
            state.copy(password = newPassword)
        }
    }

    fun submitLoginForm() = intent {
        val formValidator = getFormValidator(state)
        reduce {
            state.copy(
                fieldValidationMap = formValidator.getFieldValidationMap(),
            )
        }

        val fieldValidationError = formValidator.getFirstError()
        if (fieldValidationError == null) {
            // no errors, so launch login request
            reduce {
                state.copy(isLoading = true)
            }
            val loginResult = accountInteractor.login(email = state.email, password = state.password)

            when (loginResult) {
                is AsyncResult.Success<*> -> {
                    Timber.d("connected user = ${(loginResult.data as? UserAccount)?.id}")
                    reduce {
                        state.copy(
                            isLoading = false,
                            isConnected = true,
                        )
                    }
                }
                is AsyncResult.Fail -> {
                    Timber.e(loginResult.exception)
                    reduce {
                        state.copy(
                            isLoading = false,
                            isConnected = false,
                        )
                    }
                    postSideEffect(LoginScreenSideEffect.Toast(R.string.general_error))
                }
            }
        } else {
            postSideEffect(LoginScreenSideEffect.FieldValidationError(fieldValidationError))
        }
    }

    private fun getFormValidator(state: LoginScreenState): FormValidator<LoginScreenField> {
        val formValidator = FormValidator<LoginScreenField>()

        formValidator.addFieldValidityStatus(
            field = LoginScreenField.EMAIL,
            okStatus = EmailValidator.Status.OK,
            actualStatus = emailValidator.getValidityStatus(state.email),
        )

        formValidator.addFieldValidityStatus(
            field = LoginScreenField.PASSWORD,
            okStatus = PasswordValidator.Status.OK,
            actualStatus = passwordValidator.getValidityStatus(state.password),
        )

        return formValidator
    }
}
