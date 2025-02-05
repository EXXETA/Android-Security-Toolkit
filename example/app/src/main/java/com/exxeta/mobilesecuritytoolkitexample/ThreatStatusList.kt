package com.exxeta.mobilesecuritytoolkitexample

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exxeta.mobilesecuritytoolkitexample.ui.theme.MobileSecurityToolkitExampleTheme
import com.exxeta.securitytoolkit.ThreatDetectionCenter
import com.exxeta.securitytoolkit.ThreatNotPresent
import com.exxeta.securitytoolkit.ThreatReport

@Composable
fun ThreatStatusList() {
    val context = LocalContext.current
    val detectionCenter = remember {
        mutableStateOf(ThreatDetectionCenter(context))
    }

    val report = remember {
        mutableStateOf(ThreatReport())
    }

    LaunchedEffect(Unit) {
        detectionCenter.value.threatReports.collect {
            report.value = it
            System.out.println("New state: $it")
        }
    }

    val threats = listOf(
        ThreatStatus(
            "Root",
            "Is a way of acquiring privileged control over the operating system of a device. Tools such as Magisk or Shadow can hide the privileged access",
            report.value.rootPrivileges is ThreatNotPresent,
        ),
        ThreatStatus(
            "Hooks",
            "Intercept system or application calls and then modify them (modify the return value of a function call for example)",
            report.value.hooks is ThreatNotPresent,
        ),
        ThreatStatus(
            "Emulator",
            "Running the application in an Emulator",
            report.value.simulator is ThreatNotPresent,
        ),
        ThreatStatus(
            "Passcode",
            "Indicates if current device is unprotected with a passcode. Biometric protection requires a passcode to be set up",
            report.value.devicePasscode is ThreatNotPresent,
        ),
        ThreatStatus(
            "Hardware protection",
            "Refers to hardware capabilities of current device, specific to hardware-backed cryptography operations. If not available, no additional hardware security layer can be used when working with keys, certificates and keychain",
            report.value.hardwareCryptography is ThreatNotPresent,
        ),
        ThreatStatus(
            "Signature missmatch",
            "Expects app to be signed with a given certificate. For PlayStore should match the one provided by the store via Play Console",
            report.value.appSignature is ThreatNotPresent,
        ),
    )

    LazyColumn(
        modifier = Modifier
            .padding(16.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(48.dp))
            Icon(
                painter = painterResource(R.drawable.stethoscope_24px),
                contentDescription = "stethoscope_24px",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(80.dp),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Protection",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Here is a list of the threats that could put you at risk",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray,
            )
        }
        items(threats) { item ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(1.dp),
            ) {
                ThreatStatusRow(threatStatus = item)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    MobileSecurityToolkitExampleTheme {
        ThreatStatusList()
    }
}
