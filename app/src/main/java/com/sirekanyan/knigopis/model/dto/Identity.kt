package com.sirekanyan.knigopis.model.dto

import com.sirekanyan.knigopis.common.functions.createUserImageUrl

class Identity(
    val id: String,
    private val nickname: String?,
    private val booksCount: Int
) {
    val name get() = nickname ?: id
    val avatarUrl get() = createUserImageUrl(id)
}