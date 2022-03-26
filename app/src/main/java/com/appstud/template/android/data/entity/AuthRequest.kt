package com.appstud.template.android.data.entity

data class AuthRequest(
    val login: String,
    val password: String,
    val rememberMe: Boolean? = true
)
