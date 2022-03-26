package com.appstud.template.android.transport.utils.validation

class FormValidator<F> {

    private val fieldOkStatusList: MutableList<Any> = mutableListOf()
    private val fieldValidationStatusMap: MutableMap<F, Any> = mutableMapOf()

    fun <S : Any> addFieldValidityStatus(field: F, okStatus: S, actualStatus: S) {
        fieldOkStatusList.add(okStatus)
        fieldValidationStatusMap[field] = actualStatus
    }

    fun getFieldValidationMap(): Map<F, Any> {
        return fieldValidationStatusMap
    }

    fun getFirstError(): FieldStatus<F>? {
        val firstError = fieldValidationStatusMap
            .filterValues { status -> !fieldOkStatusList.contains(status) }
            .toList()
            .firstOrNull()
            ?.let { firstErrorPair ->
                return@let FieldStatus(
                    field = firstErrorPair.first,
                    status = firstErrorPair.second,
                )
            }

        return firstError
    }

    fun isValid(): Boolean {
        return fieldValidationStatusMap
            .filterValues { status -> !fieldOkStatusList.contains(status) }
            .isEmpty()
    }

    data class FieldStatus<F>(
        val field: F,
        val status: Any,
    )
}
