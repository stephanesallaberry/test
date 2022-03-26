package fr.stephanesallaberry.news.android.transport.utils.validation

interface ValueValidator<T, S> {

    fun getValidityStatus(value: T): S
}
