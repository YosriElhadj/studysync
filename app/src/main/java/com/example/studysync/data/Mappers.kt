package com.example.studysync.data.mapper

import com.example.studysync.data.remote.dto.UserDto
import com.example.studysync.domain.model.User

fun UserDto.toUser(): User {
    return User(
        id = id,
        email = email,
        displayName = displayName,

    )
}