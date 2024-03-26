package com.test.blindappproject.data.model

import com.test.blindappproject.data.model.dto.ContentDto
import com.test.blindappproject.data.model.entity.ContentEntity
import com.test.blindappproject.domain.model.Content
import java.util.Date

object ContentMapper {

    fun Content.toRequest() = ContentDto(
        id = id,
        title = title,
        content = content,
        category = category,
        likeCount = likeCount,
        commentCount = commentCount,
        viewCount = viewCount
    )

    fun ContentDto.toContent() = Content(
        id = id ?: -1,
        title = title,
        content = content,
        category = category,
        likeCount = likeCount ?: 0,
        viewCount = viewCount ?: 0,
        commentCount = commentCount ?: 0,
        createDate = createDate ?: Date()
    )

    fun Content.toEntity() = ContentEntity(
        id = id ?: -1,
        title = title,
        content = content,
        category = category,
        likeCount = likeCount,
        commentCount = commentCount,
        viewCount = viewCount,
        createDate = createDate ?: Date( )
    )

    fun ContentEntity.toContent() = Content(
        id = id,
        title = title,
        content = content,
        category = category,
        viewCount = viewCount,
        commentCount = commentCount,
        likeCount = likeCount,
        createDate = createDate
    )
}