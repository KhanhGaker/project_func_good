package me.vadik.knigopis.model

class FinishedBook(
    override val id: String,
    override val title: String,
    override val author: String,
    val notes: String,
    val createdAt: String,
    val user: User
) : Book