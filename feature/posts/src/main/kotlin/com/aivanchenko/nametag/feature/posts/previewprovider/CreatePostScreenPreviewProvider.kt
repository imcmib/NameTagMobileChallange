package com.aivanchenko.nametag.feature.posts.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aivanchenko.nametag.feature.posts.CreatePostContract.ViewState

class CreatePostScreenPreviewProvider : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState>
        get() = sequenceOf(
            ViewState(),
            ViewState(
                isLoading = true
            ),
            ViewState(
                error = "Something went wrong"
            )
        )
}
