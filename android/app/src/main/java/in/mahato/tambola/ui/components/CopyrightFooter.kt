package `in`.mahato.tambola.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import `in`.mahato.tambola.util.GeneralUtil

@Composable
fun CopyrightFooter(
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
) {
    Text(
        text = GeneralUtil.getCopyrightMessage(),
        color = textColor,
        fontSize = 12.sp,
        modifier = modifier
    )
}
