package com.appstud.template.android.transport.utils.validation

import android.util.Patterns

class EmailValidator : ValueValidator<String, EmailValidator.Status> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getValidityStatus(email: String): Status {
        val emailTrimmed = email.trim()
        val pattern = Patterns.EMAIL_ADDRESS
        val isFormatOK = pattern.matcher(emailTrimmed).matches()
        val isEmpty = emailTrimmed.isEmpty()
        return when {
            isEmpty -> Status.EMPTY
            !isFormatOK -> Status.MISFORMED
            else -> Status.OK
        }
    }

    enum class Status {
        OK, MISFORMED, EMPTY
    }
}
