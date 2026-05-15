package com.example.examensegundoparcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.examensegundoparcial.navigation.NavGraph
import com.example.examensegundoparcial.ui.theme.ExamenSegundoParcialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExamenSegundoParcialTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}