package `in`.mahato.tambola.game

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.mahato.tambola.ui.components.CopyrightFooter
import `in`.mahato.tambola.util.QrCodeGenerator
import androidx.compose.ui.res.stringResource
import `in`.mahato.tambola.R
import java.util.UUID

@Composable
fun NewGameSetupScreen(
    onContinue: (String) -> Unit, // Pass the game ID
    onBack: () -> Unit
) {
    var gameId by remember { mutableStateOf<String?>(null) }
    var useExistingId by remember { mutableStateOf(false) }
    var existingIdInput by remember { mutableStateOf("") }
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gameId) {
        if (gameId != null && !useExistingId) {
            qrCodeBitmap = QrCodeGenerator.generateQrCode(gameId!!)
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = {
                Text(text = stringResource(R.string.warning),
                        color = MaterialTheme.colorScheme.error)
            },
            text = {
                Text(stringResource(R.string.setup_confirm_msg))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmationDialog = false
                        if (gameId != null) {
                            onContinue(gameId!!)
                        }
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmationDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp), // Reduced padding
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Text(
            text = stringResource(R.string.new_game_setup_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Main Content Area - Scrollable
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (gameId == null) {
                    // Initial Choice: New or Existing
                    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
                    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
                    
                    if (isLandscape) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            SetupInstructions(modifier = Modifier.weight(1f))
                            Spacer(Modifier.width(32.dp))
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                InitialButtons(
                                    onCreateNew = {
                                        gameId = UUID.randomUUID().toString().substring(0, 8).uppercase()
                                        useExistingId = false
                                    },
                                    onUseExisting = {
                                        gameId = "" 
                                        useExistingId = true
                                    }
                                )
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            SetupInstructions(modifier = Modifier.fillMaxWidth())
                            InitialButtons(
                                onCreateNew = {
                                    gameId = UUID.randomUUID().toString().substring(0, 8).uppercase()
                                    useExistingId = false
                                },
                                onUseExisting = {
                                    gameId = "" 
                                    useExistingId = true
                                }
                            )
                        }
                    }
                } else if (useExistingId) {
                    // Input for Existing ID
                     Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.enter_game_id),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        
                        TextField(
                            value = existingIdInput,
                            onValueChange = { existingIdInput = it },
                            modifier = Modifier.width(300.dp),
                            singleLine = true,
                             colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )

                        Button(
                            onClick = {
                                if (existingIdInput.isNotBlank()) {
                                    gameId = existingIdInput
                                    useExistingId = false
                                }
                            },
                             colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer // Green
                            ),
                             modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(stringResource(R.string.show_qr_code), color = Color.White)
                        }
                        
                        TextButton(onClick = { 
                            gameId = null 
                            useExistingId = false
                            existingIdInput = ""
                        }) {
                            Text(stringResource(R.string.back), color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                } else {
                    // Show Generated ID and QR - Responsive Layout
                    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
                    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

                    if (isLandscape) {
                        // Landscape Layout (Row)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            QrCodeSection(qrCodeBitmap)
                            Spacer(modifier = Modifier.width(32.dp))
                            GameIdSection(gameId, { showConfirmationDialog = true }, { gameId = null; qrCodeBitmap = null })
                        }
                    } else {
                        // Portrait Layout (Column)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                             horizontalAlignment = Alignment.CenterHorizontally,
                             verticalArrangement = Arrangement.Center
                        ) {
                            QrCodeSection(qrCodeBitmap)
                            Spacer(modifier = Modifier.height(24.dp))
                            GameIdSection(gameId, { showConfirmationDialog = true }, { gameId = null; qrCodeBitmap = null })
                        }
                    }
                }
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        // Banner Ad Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Reduced height
                .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.banner_ad_placeholder), color = Color.White, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        CopyrightFooter(textColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f), modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun QrCodeSection(qrCodeBitmap: Bitmap?) {
    if (qrCodeBitmap != null) {
        Image(
            bitmap = qrCodeBitmap.asImageBitmap(),
            contentDescription = stringResource(R.string.game_id_qr_desc),
            modifier = Modifier
                .size(200.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(8.dp)
        )
    } else {
        Box(modifier = Modifier.size(200.dp).background(Color.LightGray)) {
            Text(stringResource(R.string.generating_qr), modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun GameIdSection(gameId: String?, onContinue: () -> Unit, onBack: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.game_id_label),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = gameId ?: "",
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = `in`.mahato.tambola.ui.theme.Gold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer // Green
            ),
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text(stringResource(R.string.continue_setup), color = Color.White)
        }

        TextButton(onClick = onBack) {
            Text(stringResource(R.string.back), color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun InitialButtons(onCreateNew: () -> Unit, onUseExisting: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Button(
            onClick = onCreateNew,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer // Gold/Bronze
            ),
            modifier = Modifier.width(300.dp).height(60.dp)
        ) {
            Text(stringResource(R.string.create_new_game_id), color = Color.Black)
        }

        Button(
            onClick = onUseExisting,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary // Dark Purple
            ),
            modifier = Modifier.width(300.dp).height(60.dp)
        ) {
            Text(stringResource(R.string.use_existing_game_id), color = Color.White)
        }
    }
}

@Composable
fun SetupInstructions(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha=0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
           Text(
                stringResource(R.string.setup_instructions_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                 color = `in`.mahato.tambola.ui.theme.Gold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            val bullet = "• "
            val instructions = listOf(
                stringResource(R.string.setup_instr_1),
                stringResource(R.string.setup_instr_2),
                stringResource(R.string.setup_instr_3),
                stringResource(R.string.setup_instr_4),
                stringResource(R.string.setup_instr_5)
            )
            
            instructions.forEach { instruction ->
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(bullet, color = Color.White)
                    Text(
                        instruction,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}
