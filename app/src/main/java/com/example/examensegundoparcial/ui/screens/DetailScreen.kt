package com.example.examensegundoparcial.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.examensegundoparcial.data.model.Album
import com.example.examensegundoparcial.data.network.RetrofitInstance
import com.example.examensegundoparcial.ui.components.MiniPlayer
import com.example.examensegundoparcial.ui.theme.DarkPurple
import com.example.examensegundoparcial.ui.theme.PrimaryPurple
import com.example.examensegundoparcial.ui.theme.TextSecondary

@Composable
fun DetailScreen(albumId: String, onBack: () -> Unit) {
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(albumId) {
        try {
            album = RetrofitInstance.api.getAlbumById(albumId)
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Error al cargar álbum: ${e.message}"
            isLoading = false
        }
    }

    val trackList = remember(album) {
        (1..10).map { trackNumber -> "${album?.title ?: "Track"} • Track $trackNumber" }
    }

    Scaffold(
        bottomBar = { MiniPlayer(album = album) }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryPurple)
                }
            }
            errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                }
            }
            album != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                ) {
                    // HEADER
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                            AsyncImage(
                                model = album!!.image,
                                contentDescription = album!!.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier.fillMaxSize().background(
                                    Brush.verticalGradient(
                                        colors = listOf(DarkPurple.copy(alpha = 0.4f), DarkPurple.copy(alpha = 0.85f))
                                    )
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 40.dp).align(Alignment.TopCenter),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(onClick = onBack) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                                }
                                IconButton(onClick = { }) {
                                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.White)
                                }
                            }
                            Column(
                                modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 20.dp, vertical = 20.dp)
                            ) {
                                Text(text = album!!.title, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                                Text(text = album!!.artist, color = Color.White.copy(alpha = 0.85f), fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(14.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Box(
                                        modifier = Modifier.size(44.dp).background(PrimaryPurple, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        IconButton(onClick = { isPlaying = !isPlaying }) {
                                            Icon(
                                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = "Play",
                                                tint = Color.White,
                                                modifier = Modifier.size(26.dp)
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier.size(44.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        IconButton(onClick = { }) {
                                            Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = Color.White, modifier = Modifier.size(22.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ABOUT THIS ALBUM
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "About this album", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = album!!.description, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
                            }
                        }
                    }

                    // CHIP ARTISTA
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                            SuggestionChip(
                                onClick = { },
                                label = { Text(text = "Artist: ${album!!.artist}", fontSize = 13.sp) },
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // TRACK LIST
                    items(trackList.zip(1..10)) { (trackTitle, _) ->
                        TrackItem(
                            imageUrl = album!!.image,
                            trackTitle = trackTitle,
                            artist = album!!.artist,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun TrackItem(
    imageUrl: String,
    trackTitle: String,
    artist: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = trackTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = trackTitle, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = artist, color = TextSecondary, fontSize = 12.sp)
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options", tint = TextSecondary)
            }
        }
    }
}