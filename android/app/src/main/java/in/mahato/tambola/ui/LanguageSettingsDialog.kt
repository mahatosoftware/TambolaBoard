package `in`.mahato.tambola.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import `in`.mahato.tambola.R
import `in`.mahato.tambola.util.LanguageOption
import `in`.mahato.tambola.util.LanguageUtil

@Composable
fun LanguageSettingsDialog(
    onDismiss: () -> Unit,
    onLanguageChanged: (String) -> Unit
) {
    val context = LocalContext.current
    val currentLang = remember { mutableStateOf(LanguageUtil.getSelectedLanguage(context)) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .widthIn(max = 450.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.title_settings),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.label_select_language),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color(0xFFE0E0E0)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.hint_tv_language_select),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(LanguageUtil.SUPPORTED_LANGUAGES, key = { it.code }) { option ->
                        LanguageItemRow(
                            option = option,
                            isSelected = (currentLang.value == option.code),
                            onSelect = {
                                if (currentLang.value != option.code) {
                                    currentLang.value = option.code
                                    LanguageUtil.setSelectedLanguage(context, option.code)
                                    onLanguageChanged(option.code)
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                var isCloseFocused by remember { mutableStateOf(false) }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isCloseFocused = it.isFocused },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCloseFocused)
                            MaterialTheme.colorScheme.background
                        else
                            MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (isCloseFocused)
                            MaterialTheme.colorScheme.onTertiary
                        else
                            MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.btn_close),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageItemRow(
    option: LanguageOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val rowBackgroundColor = when {
        isFocused -> MaterialTheme.colorScheme.background
        isSelected -> MaterialTheme.colorScheme.primaryContainer // Distinct Green Container background for Selected Language!
        else -> Color.Transparent
    }

    val rowTextColor = when {
        isFocused -> MaterialTheme.colorScheme.onTertiary // Black when focused
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer // White on Green
        else -> MaterialTheme.colorScheme.onPrimary // White
    }

    val borderWidth = if (isFocused && isSelected) 2.dp else 0.dp
    val borderColor = if (isFocused && isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
            .background(
                color = rowBackgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .focusable()
            .clickable { onSelect() }
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.key) {
                        Key.Enter, Key.NumPadEnter, Key.DirectionCenter -> {
                            onSelect()
                            true
                        }
                        else -> false
                    }
                } else false
            }
            .padding(vertical = 10.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = when {
                    isFocused && isSelected -> Color(0xFF009E60)
                    isFocused -> Color.Black
                    else -> Color.White
                },
                unselectedColor = when {
                    isFocused -> Color.Black.copy(alpha = 0.6f)
                    else -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                }
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "${option.nameEnglish} (${option.nameNative})",
            fontSize = 18.sp,
            fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Medium,
            color = rowTextColor,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Text(
                text = "✓",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = if (isFocused) Color(0xFF009E60) else Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
