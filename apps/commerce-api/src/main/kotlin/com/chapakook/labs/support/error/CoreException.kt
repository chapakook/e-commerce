package com.chapakook.labs.support.error

class CoreException(
    val errorType: ErrorType,
    val customMessage: String? = null,
) : RuntimeException(customMessage ?: errorType.message)
