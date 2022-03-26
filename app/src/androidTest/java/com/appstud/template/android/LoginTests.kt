package com.appstud.template.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.appstud.template.android.transport.MainActivity
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTests {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
        MainActivity::class.java,
        true,
        true
    )

    @Test
    fun startAppAndAskForLogin() {
        onScreen<LoginScreen> {
            loginInputEmail {
                isDisplayed()
            }
        }
    }

    class LoginScreen : Screen<LoginScreen>() {
        val loginInputEmail = KEditText { withId(R.id.loginInputEmail) }
    }
}
