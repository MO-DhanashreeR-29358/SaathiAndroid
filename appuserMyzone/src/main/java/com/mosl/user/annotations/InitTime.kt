package com.mosl.core.annotations

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Created by lalkishore on 8/12/2016.
 */
@Retention(RUNTIME)
@Qualifier
annotation class InitTime
