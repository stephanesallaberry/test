package fr.stephanesallaberry.news.android

import fr.stephanesallaberry.news.android.domain.external.AccountInteractor
import fr.stephanesallaberry.news.android.domain.external.entity.AsyncResult
import fr.stephanesallaberry.news.android.domain.external.entity.UserAccount
import fr.stephanesallaberry.news.android.transport.login.LoginScreenField
import fr.stephanesallaberry.news.android.transport.login.LoginScreenSideEffect
import fr.stephanesallaberry.news.android.transport.login.LoginScreenState
import fr.stephanesallaberry.news.android.transport.login.LoginViewModel
import fr.stephanesallaberry.news.android.transport.utils.validation.EmailValidator
import fr.stephanesallaberry.news.android.transport.utils.validation.FormValidator
import fr.stephanesallaberry.news.android.transport.utils.validation.PasswordValidator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.orbitmvi.orbit.test

class LoginViewModelTest {

    private val accountInteractorTest = mock(AccountInteractor::class.java)
    private val emailValidator = mock(EmailValidator::class.java)
    private val passwordValidator = mock(PasswordValidator::class.java)
    private val testSubject =
        LoginViewModel(accountInteractorTest, emailValidator, passwordValidator).test(
            LoginScreenState()
        )

    @ExperimentalCoroutinesApi
    @Before
    fun setupMocks() {
        runBlockingTest {
            `when`(emailValidator.getValidityStatus(anyString())).thenReturn(EmailValidator.Status.MISFORMED)

            `when`(passwordValidator.getValidityStatus(anyString())).thenReturn(PasswordValidator.Status.TOO_SHORT)

            `when`(accountInteractorTest.isConnected()).thenReturn(false)

            `when`(
                accountInteractorTest.login(
                    anyString(),
                    anyString()
                )
            ).thenReturn(
                AsyncResult.Success(
                    UserAccount(email = "email@example.com", id = 1)
                )
            )

            testSubject.runOnCreate()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check email and password format after submit only`() {
        runBlockingTest {
            val emailWithBadFormat = "email@badformat"
            val badPassword = "pass"
            // 1st case: only change email and password
            testSubject.testIntent {
                changeEmail(emailWithBadFormat)
            }
            testSubject.testIntent {
                changePassword(badPassword)
            }

            testSubject.assert(LoginScreenState()) {
                states(
                    { copy(email = emailWithBadFormat, fieldValidationMap = emptyMap()) },
                    { copy(password = badPassword, fieldValidationMap = emptyMap()) }
                )
            }

            testSubject.testIntent {
                submitLoginForm()
            }

            testSubject.assert(LoginScreenState()) {
                states(
                    { copy(email = emailWithBadFormat, fieldValidationMap = emptyMap()) },
                    { copy(password = badPassword, fieldValidationMap = emptyMap()) },
                    {
                        copy(
                            email = emailWithBadFormat,
                            password = badPassword,
                            fieldValidationMap = mapOf(
                                LoginScreenField.EMAIL to EmailValidator.Status.MISFORMED,
                                LoginScreenField.PASSWORD to PasswordValidator.Status.TOO_SHORT,
                            ),
                        )
                    },
                )

                postedSideEffects(
                    LoginScreenSideEffect.FieldValidationError(
                        FormValidator.FieldStatus(LoginScreenField.EMAIL, EmailValidator.Status.MISFORMED)
                    )
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `use empty field error messages`() {
        runBlockingTest {
            `when`(emailValidator.getValidityStatus(anyString())).thenReturn(EmailValidator.Status.EMPTY)
            `when`(passwordValidator.getValidityStatus(anyString())).thenReturn(PasswordValidator.Status.EMPTY)

            // submit with no email and no password
            testSubject.testIntent {
                submitLoginForm()
            }

            testSubject.assert(LoginScreenState()) {
                states(
                    {
                        copy(
                            fieldValidationMap = mapOf(
                                LoginScreenField.EMAIL to EmailValidator.Status.EMPTY,
                                LoginScreenField.PASSWORD to PasswordValidator.Status.EMPTY,
                            ),
                        )
                    },
                )

                postedSideEffects(
                    LoginScreenSideEffect.FieldValidationError(
                        FormValidator.FieldStatus(LoginScreenField.EMAIL, EmailValidator.Status.EMPTY)
                    )
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `use wrong format field error messages`() {
        runBlockingTest {
            // submit with wrong email and short password
            val wrongEmail = "email@d"
            val shortPassword = "pass"
            testSubject.testIntent {
                changeEmail(wrongEmail)
            }
            testSubject.testIntent {
                changePassword(shortPassword)
            }
            testSubject.testIntent {
                submitLoginForm()
            }

            testSubject.assert(LoginScreenState()) {
                states(
                    { copy(email = wrongEmail, fieldValidationMap = emptyMap()) },
                    { copy(password = shortPassword, fieldValidationMap = emptyMap()) },
                    {
                        copy(
                            email = wrongEmail,
                            password = shortPassword,
                            fieldValidationMap = mapOf(
                                LoginScreenField.EMAIL to EmailValidator.Status.MISFORMED,
                                LoginScreenField.PASSWORD to PasswordValidator.Status.TOO_SHORT,
                            ),
                        )
                    },
                )

                postedSideEffects(
                    LoginScreenSideEffect.FieldValidationError(
                        FormValidator.FieldStatus(LoginScreenField.EMAIL, EmailValidator.Status.MISFORMED)
                    )
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `use email and password validators`() {
        runBlockingTest {
            testSubject.testIntent {
                submitLoginForm()
            }

            verify(emailValidator, times(1)).getValidityStatus(anyString())
            verify(passwordValidator, times(1)).getValidityStatus(anyString())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `set loading state while logging in the user and connected state after`() {
        runBlockingTest {
            `when`(emailValidator.getValidityStatus(anyString())).thenReturn(EmailValidator.Status.OK)
            `when`(passwordValidator.getValidityStatus(anyString())).thenReturn(PasswordValidator.Status.OK)

            // submit with right email and right password
            val email = "email@example.com"
            val password = "password"
            testSubject.testIntent {
                changeEmail(email)
            }
            testSubject.testIntent {
                changePassword(password)
            }
            testSubject.testIntent {
                submitLoginForm()
            }
            testSubject.assert(LoginScreenState()) {
                states(
                    { copy(email = email) },
                    { copy(password = password) },
                    {
                        copy(
                            fieldValidationMap = mapOf(
                                LoginScreenField.EMAIL to EmailValidator.Status.OK,
                                LoginScreenField.PASSWORD to PasswordValidator.Status.OK,
                            ),
                        )
                    },
                    { copy(isLoading = true) },
                    { copy(isLoading = false, isConnected = true) }
                )
            }

            verify(accountInteractorTest, times(1)).login(anyString(), anyString())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `toast and reenable form after login failed`() {
        runBlockingTest {
            `when`(emailValidator.getValidityStatus(anyString())).thenReturn(EmailValidator.Status.OK)
            `when`(passwordValidator.getValidityStatus(anyString())).thenReturn(PasswordValidator.Status.OK)
            `when`(
                accountInteractorTest.login(
                    anyString(),
                    anyString()
                )
            ).thenReturn(
                AsyncResult.Fail(
                    Exception("401 Unauthorized")
                )
            )

            val email = "email@example.com"
            val password = "password"
            testSubject.testIntent {
                changeEmail(email)
            }
            testSubject.testIntent {
                changePassword(password)
            }
            testSubject.testIntent {
                submitLoginForm()
            }
            testSubject.assert(LoginScreenState()) {
                states(
                    { copy(email = email) },
                    { copy(password = password) },
                    {
                        copy(
                            fieldValidationMap = mapOf(
                                LoginScreenField.EMAIL to EmailValidator.Status.OK,
                                LoginScreenField.PASSWORD to PasswordValidator.Status.OK,
                            ),
                        )
                    },
                    { copy(isLoading = true) },
                    { copy(isLoading = false, isConnected = false) }
                )
                postedSideEffects(
                    LoginScreenSideEffect.Toast(R.string.general_error)
                )
            }
        }
    }
}
