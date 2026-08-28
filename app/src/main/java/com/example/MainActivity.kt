package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.LoginGateScreen
import com.example.ui.screens.MainMatrixScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MatrixViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MatrixViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val isAuthenticated by viewModel.isAuthenticated.collectAsState()

                AnimatedContent(
                    targetState = isAuthenticated,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "MatrixAuthTransition",
                    modifier = Modifier.fillMaxSize()
                ) { authenticated ->
                    if (authenticated) {
                        MainMatrixScreen(viewModel = viewModel)
                    } else {
                        LoginGateScreen(
                            onAuthenticate = { loginId, password ->
                                viewModel.authenticate(loginId, password)
                            }
                        )
                    }
                }
            }
        }
    }
}
