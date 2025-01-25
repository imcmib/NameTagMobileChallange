package com.aivanchenko.nametag.feature.auth.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.aivanchenko.nametag.feature.auth.AuthContract

class AuthScreenPreviewProvider : PreviewParameterProvider<AuthContract.ViewState> {
    override val values: Sequence<AuthContract.ViewState>
        get() = sequenceOf(
            AuthContract.ViewState(),
            AuthContract.ViewState(
                isLoading = true
            ),
            AuthContract.ViewState(
                error = "Auth failed"
            )
        )
}
