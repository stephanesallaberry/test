package fr.stephanesallaberry.news.android.transport.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import fr.stephanesallaberry.news.android.R
import fr.stephanesallaberry.news.android.databinding.LoginFragmentBinding
import fr.stephanesallaberry.news.android.transport.utils.validation.EmailValidator
import fr.stephanesallaberry.news.android.transport.utils.validation.FormValidator
import fr.stephanesallaberry.news.android.transport.utils.validation.PasswordValidator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber

class LoginFragment : Fragment() {

    private lateinit var loginFragmentBinding: LoginFragmentBinding
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginFragmentBinding = LoginFragmentBinding.inflate(inflater, container, false)
        loginFragmentBinding.lifecycleOwner = viewLifecycleOwner
        loginFragmentBinding.viewModel = viewModel
        return loginFragmentBinding.root
    }

    private fun render(state: LoginScreenState) {
        if (state.isConnected) {
            // should not be here, close the activity
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }

        Timber.d("render $state")

        // Get validation status for Email input field
        state.fieldValidationMap[LoginScreenField.EMAIL]
            .let { validationStatus ->
                // Attempt to get validation error message
                val errorMessage = getFieldErrorMessage(validationStatus)

                // Set/remove red border to input field
                loginFragmentBinding.loginInputEmailError.isErrorEnabled = errorMessage != null

                // And set validation error message
                loginFragmentBinding.loginInputEmailError.error = errorMessage
            }

        state.fieldValidationMap[LoginScreenField.PASSWORD]
            .let { validationStatus ->
                val errorMessage = getFieldErrorMessage(validationStatus)
                loginFragmentBinding.loginInputPasswordError.isErrorEnabled = errorMessage != null
                loginFragmentBinding.loginInputPasswordError.error = errorMessage
            }
    }

    private fun handleSideEffect(sideEffect: LoginScreenSideEffect) {
        when (sideEffect) {
            is LoginScreenSideEffect.Toast -> Toast.makeText(context, sideEffect.textResource, Toast.LENGTH_SHORT)
                .show()
            is LoginScreenSideEffect.FieldValidationError -> toastFieldValidationError(sideEffect.fieldError)
        }
    }

    private fun toastFieldValidationError(fieldError: FormValidator.FieldStatus<LoginScreenField>) {
        val errorMessage = getFieldErrorMessage(fieldError.status)
        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFieldErrorMessage(errorStatus: Any?): String? {
        return when (errorStatus) {
            EmailValidator.Status.EMPTY -> getString(R.string.login_field_empty)
            EmailValidator.Status.MISFORMED -> getString(R.string.login_email_bad_format)

            PasswordValidator.Status.EMPTY -> getString(R.string.login_field_empty)
            PasswordValidator.Status.TOO_SHORT ->
                getString(R.string.login_password_too_short, PasswordValidator.MINIMUM_LENGTH)

            else -> null
        }
    }
}
