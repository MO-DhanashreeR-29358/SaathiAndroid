package com.mosl.app.core.exception

/**
 * Created by lalkishore on 3/1/2017.
 */

class ServiceFailureException @JvmOverloads constructor(
    @JvmField val errorCode: String,
    @JvmField val errorMessage: String = errorCode
) : RuntimeException()