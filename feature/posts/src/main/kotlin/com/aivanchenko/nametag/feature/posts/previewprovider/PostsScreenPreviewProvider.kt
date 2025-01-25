package com.aivanchenko.nametag.feature.posts.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aivanchenko.nametag.feature.posts.PostsContract.ViewState

class PostsScreenPreviewProvider : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState>
        get() = sequenceOf(
            ViewState(
                posts = PostPreviewProvider().values.toList()
            ),
            ViewState(
                isLoading = true
            ),
            ViewState(
                error = "Failed to load posts"
            )
        )
}
