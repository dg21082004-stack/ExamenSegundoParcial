package com.example.examensegundoparcial.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.examensegundoparcial.data.model.Album
import com.example.examensegundoparcial.data.network.RetrofitInstance
import com.example.examensegundoparcial.ui.components.AlbumCard
import com.example.examensegundoparcial.ui.components.MiniPlayer
import com.example.examensegundoparcial.ui.components.RecentlyPlayedItem
import com.example.examensegundoparcial.ui.theme.DarkPurple
import com.example.examensegundoparcial.ui.theme.PrimaryPurple

@Composable
fun HomeScreen(onAlbumClick: (String) -> Unit) {
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentAlbum by remember { mutableStateOf<Album?>(null) }

    LaunchedEffect(Unit) {
        try {
            albums = RetrofitInstance.api.getAlbums()
            currentAlbum = albums.firstOrNull()
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Error al cargar álbumes: ${e.message}"
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = {
            MiniPlayer(album = currentAlbum)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // HEADER
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(PrimaryPurple, DarkPurple)
                            ),
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Good Morning!", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
                        Text(text = "Juan Frausto", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ALBUMS LazyRow
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Albums", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    TextButton(onClick = { }) {
                        Text(text = "See more", color = PrimaryPurple, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator(color = PrimaryPurple) }
                    }
                    errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) { Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error) }
                    }
                    else -> {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(albums) { album ->
                                AlbumCard(
                                    album = album,
                                    onClick = {
                                        currentAlbum = album
                                        onAlbumClick(album.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // RECENTLY PLAYED LazyColumn
            item {
                Spacer(modifier = Modifier.height(28.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Recently Played", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    TextButton(onClick = { }) {
                        Text(text = "See more", color = PrimaryPurple, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (!isLoading && errorMessage == null) {
                items(albums) { album ->
                    RecentlyPlayedItem(
                        album = album,
                        onClick = {
                            currentAlbum = album
                            onAlbumClick(album.id)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}