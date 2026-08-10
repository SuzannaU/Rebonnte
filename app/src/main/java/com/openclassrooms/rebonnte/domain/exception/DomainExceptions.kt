package com.openclassrooms.rebonnte.domain.exception

class UserNotFoundException(message: String) : Exception(message)
class DatabaseException(message: String) : Exception(message)
class NetworkException(message: String) : Exception(message)
class AuthException(message: String) : Exception(message)