package com.aivanchenko.nametag.feature.posts.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aivanchenko.nametag.domain.model.Post

class PostPreviewProvider : PreviewParameterProvider<Post> {
    override val values: Sequence<Post>
        get() = (0..3L).map { itemId ->
            Post(
                id = itemId.toString(),
                author = "Author $itemId",
                headline = "Headline $itemId",
                image = "",
                upvotes = itemId
            )
        }.asSequence()
}
