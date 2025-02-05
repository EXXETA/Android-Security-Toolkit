package com.exxeta.mobilesecuritytoolkitexample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exxeta.mobilesecuritytoolkitexample.ui.theme.MobileSecurityToolkitExampleTheme

@Composable
fun ThreatStatusRow(threatStatus: ThreatStatus) {
    val redColor = Color(1f, 0.23f, 0.18f)
    val greenColor = Color(0.3f, 0.85f, 0.4f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = threatStatus.title,
                style = MaterialTheme.typography.titleLarge,
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(CornerSize(size = 4.dp)))
                    .background(
                        if (threatStatus.isSafe) greenColor else redColor,
                    ),
            ) {
                Text(
                    text = if (threatStatus.isSafe) "SAFE" else "UNSAFE",
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .padding(horizontal = 8.dp),
                )
            }
        }
        Text(
            threatStatus.description,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    MobileSecurityToolkitExampleTheme {
        ThreatStatusRow(
            ThreatStatus(
                "Jailbreak",
                "Description",
                true,
            ),
        )
    }
}
