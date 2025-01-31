package com.exxeta.mobilesecuritytoolkitexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.exxeta.mobilesecuritytoolkitexample.ui.theme.MobileSecurityToolkitExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileSecurityToolkitExampleTheme {
                ThreatStatusList()
            }
        }
    }
}
