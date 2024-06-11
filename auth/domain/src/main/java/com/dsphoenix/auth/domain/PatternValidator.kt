package com.dsphoenix.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}
