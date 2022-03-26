package com.appstud.template.android.transport.utils.validation

interface ValueValidator<T, S> {

    fun getValidityStatus(value: T): S
}
