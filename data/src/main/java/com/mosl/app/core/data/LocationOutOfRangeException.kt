package com.mosl.app.core.data

// Create this class in your project
class LocationOutOfRangeException(message: String = "No office found within allowed radius") : Exception(message)
