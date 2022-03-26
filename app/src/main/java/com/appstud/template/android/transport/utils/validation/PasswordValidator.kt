package com.appstud.template.android.transport.utils.validation

class PasswordValidator : ValueValidator<String, PasswordValidator.Status> {

    companion object {
        const val MINIMUM_LENGTH = 8
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getValidityStatus(password: String): Status {
        val trimmed = password.trim()
        val isFormatOK = trimmed.length >= MINIMUM_LENGTH
        val isEmpty = trimmed.isEmpty()
        return when {
            isEmpty -> Status.EMPTY
            !isFormatOK -> Status.TOO_SHORT
            else -> Status.OK
        }
    }

    enum class Status {
        OK, TOO_SHORT, EMPTY
    }
}
