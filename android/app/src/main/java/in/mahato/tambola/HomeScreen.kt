
package `in`.mahato.tambola

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.compose.ui.focus.onFocusChanged


import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HomeScreen(
    welcomeText: String,
    newGameText: String,
    continueText: String,
    copyrightText: String,
    onNewGame: () -> Unit,
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // ✔ purple from theme
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(96.dp))

            // Title with logo centered above text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(26.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = welcomeText,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                    lineHeight = 36.sp,
                    textAlign = TextAlign.Center
                )
            }

            // START NEW GAME button
            ThemedButton(
                text = newGameText,
                onClick = onNewGame
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CONTINUE LAST GAME button
            ThemedButton(
                text = continueText,
                onClick = onContinue
            )
        }

        // Copyright text bottom-center
        Text(
            text = copyrightText,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), // ✔ light grey
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
        )
    }
}

@Composable
fun ThemedButton(
    text: String,
    onClick: () -> Unit
) {


    // Button with shadow and rounded corners
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(55.dp)
            .shadow(8.dp, RoundedCornerShape(30.dp))
            .background(
                MaterialTheme.colorScheme.primary,              // ✔ white (from theme)
                RoundedCornerShape(30.dp)
            )
            .clickable { onClick() },


        contentAlignment = Alignment.Center,

        ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,        // ✔ dark text
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
