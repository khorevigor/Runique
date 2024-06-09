package com.dsphoenix.runique

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dsphoenix.auth.presentation.intro.IntroScreen
import com.dsphoenix.auth.presentation.register.RegisterScreen
import com.dsphoenix.auth.presentation.register.RegisterState
import com.dsphoenix.core.presentation.designsystem.AnalyticsIcon
import com.dsphoenix.core.presentation.designsystem.RuniqueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RuniqueTheme {
                RegisterScreen(RegisterState(), onAction = {})
            }
        }
    }
}
