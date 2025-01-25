package com.aivanchenko.nametag.feature.posts.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.aivanchenko.nametag.domain.model.Post
import com.aivanchenko.nametag.feature.posts.R
import com.aivanchenko.nametag.feature.posts.previewprovider.PostPreviewProvider
import com.aivanchenko.nametag.presentation.theme.NametagTheme

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: Post,
    canVote: Boolean = false,
    onUpVoteClick: () -> Unit = {},
    onDownVoteClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxWidth(),
                model = post.image,
                contentDescription = post.headline,
                contentScale = ContentScale.FillWidth,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.secondaryContainer)
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.error),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )

            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                text = post.author,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = post.headline,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (canVote) {
                IconButton(
                    onClick = onUpVoteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.like)
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(all = 16.dp),
                text = post.upvotes.toString(),
                style = MaterialTheme.typography.bodySmall
            )

            if (canVote) {
                IconButton(
                    onClick = onDownVoteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.dislike)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NametagTheme {
        PostCard(
            post = PostPreviewProvider().values.first()
        )
    }
}

@Preview
@Composable
private fun PreviewAuthorized() {
    NametagTheme {
        PostCard(
            post = PostPreviewProvider().values.first(),
            canVote = true
        )
    }
}
