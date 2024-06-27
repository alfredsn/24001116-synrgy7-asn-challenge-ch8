package com.example.data.datasource.local

import com.example.domain.model.EmailData

data class EmailDataImpl(
    override val sendTo: String,
    override val name: String,
    override val replyTo: String,
    override val isHtml: Boolean,
    override val title: String,
    override val body: String
) : EmailData