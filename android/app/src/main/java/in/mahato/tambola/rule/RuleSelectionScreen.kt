package `in`.mahato.tambola.rule

import android.content.Intent
import android.content.res.Configuration
import android.os.Parcelable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import `in`.mahato.tambola.rule.viewmodel.RuleViewModel
import `in`.mahato.tambola.R
import `in`.mahato.tambola.game.model.TambolaRule
import `in`.mahato.tambola.ui.theme.AppTheme
import kotlinx.parcelize.Parcelize


// -------------------- MODEL --------------------



// -------------------- SAMPLE DATA --------------------


enum class TambolaRuleType(val iconRes: Int) {
    EARLY_FIVE(R.drawable.ic_early_five),
    TOP_LINE(R.drawable.ic_line),
    MIDDLE_LINE(R.drawable.ic_middle_line),
    BOTTOM_LINE(R.drawable.ic_bottom_line),
    FULL_HOUSE(R.drawable.ic_full_house),
    SECOND_HOUSE(R.drawable.ic_full_house),
    THIRD_HOUSE(R.drawable.ic_full_house),
    CORNER(R.drawable.ic_corner),

    DIAMOND(R.drawable.ic_diamond),
    PYRAMID(R.drawable.ic_pyramid),
    INVERTED_PYRAMID(R.drawable.ic_inverted_pyramid),
    STAR(R.drawable.ic_early_five),
    ODDS(R.drawable.ic_numbers),
    EVEN(R.drawable.ic_numbers),
    FIRST_HALF(R.drawable.ic_split_first),
    SECOND_HALF(R.drawable.ic_split_second),
    BREAKFAST(R.drawable.ic_meal_breakfast),
    LUNCH(R.drawable.ic_meal_lunch),
    DINNER(R.drawable.ic_meal_dinner),
    TEMPERATURE(R.drawable.ic_temperature),
    BELOW_FIFTY(R.drawable.ic_above50),
    ABOVE_FIFTY(R.drawable.ic_below50)
}


val tambolaRules = listOf(
    TambolaRule(
        1,
        "Full House",
        "Complete all numbers on the ticket.",
        TambolaRuleType.FULL_HOUSE,
        (0..26).toList(),
        weight = 50,
        isFullHouse = true
    ),
    TambolaRule(2, "Second House", "Second player to complete the ticket.", TambolaRuleType.SECOND_HOUSE, (0..26).toList(),weight = 30, isFullHouse = true),
    TambolaRule(3, "Third House", "Third player to complete the ticket.", TambolaRuleType.THIRD_HOUSE, (0..26).toList(),weight = 20, isFullHouse = true),

    TambolaRule(4, "Early Five", "First player to mark any five numbers.", TambolaRuleType.EARLY_FIVE, listOf(0, 4, 12, 18, 26) ,weight = 10),
    TambolaRule(5, "Top Line", "Complete all numbers in the top row.", TambolaRuleType.TOP_LINE, (0..8).toList(),weight = 10),
    TambolaRule(6, "Middle Line", "Complete all numbers in the middle row.", TambolaRuleType.MIDDLE_LINE, (9..17).toList(),weight =10 ),
    TambolaRule(7, "Bottom Line", "Complete all numbers in the bottom row.", TambolaRuleType.BOTTOM_LINE, (18..26).toList(),weight = 10),
    TambolaRule(8, "Corner", "Top Row:1st ,5th Number\n Bottom Row: 1st ,5th Number.", TambolaRuleType.CORNER, listOf(0, 7, 18, 26),weight = 10),
    TambolaRule(9, "Diamond", "Top Row:1st ,5th Number\n Middle Row: 3rd Number \n Bottom Row: 1st ,5th Number.", TambolaRuleType.DIAMOND, listOf(0, 7,14, 18, 26),weight = 10),
    TambolaRule(10, "Pyramid", "Top Row: 3rd Number \n Middle Row: 2nd, 4th Number\n Bottom Row: 1st, 3rd, 5th Number", TambolaRuleType.PYRAMID, listOf(4,  12, 15, 18,21, 26),weight = 10),
    TambolaRule(11, "Inverted Pyramid", "Top Row: 1st, 3rd and 5th number \n Middle Row: 2nd and 4th number\n Bottom Row 3rd number.", TambolaRuleType.INVERTED_PYRAMID, listOf(0, 4, 7, 12, 15, 21),weight = 10),
    TambolaRule(12, "Star", "Top Row: 1st, 3rd, 5th Number\n Middle Row: All Numbers \n Bottom Row: 1st, 3rd, 5th Number.", TambolaRuleType.STAR, listOf(0,4,7,10,12, 14,15, 17, 18,21,26),weight = 10),
    TambolaRule(13, "Odds", "Top Row: 1st, 3rd, 5th Number \n Middle Row: 1st, 3rd, 5th Number \n Bottom Row: 1st, 3rd, 5th Number.", TambolaRuleType.ODDS, listOf(0, 4, 7,10,14,17,18,21, 26),weight = 10),
    TambolaRule(14, "Even", "Top Row: 2nd, 4th Number \n Middle Row: 2nd, 4th Number \n Bottom Row: 2nd, 4th Number.", TambolaRuleType.EVEN, listOf(2,5,12,15,20,25),weight = 10),
    TambolaRule(15, "First Half", "First three numbers from every row.", TambolaRuleType.FIRST_HALF, listOf(0,2,4,10,12,14,18,20,21),weight = 10),
    TambolaRule(16, "Second Half", "Last three numbers from every row.", TambolaRuleType.SECOND_HALF, listOf(4,5,7,14,15,17,21,25,26),weight = 10),
    TambolaRule(17, "Breakfast", "Column 1,2,3. Mark numbers from 1 to 29.", TambolaRuleType.BREAKFAST, listOf(0,2,10,18,20),weight = 10),
    TambolaRule(18, "Lunch", "Column 4,5,6. Mark numbers from 30 to 59.", TambolaRuleType.LUNCH, listOf(4,5,12,14,21),weight = 10),
    TambolaRule(19, "Dinner", "Column 7,8,9. Mark numbers from 60 to 90.", TambolaRuleType.DINNER, listOf(7,15,17,25,26),weight = 10),
    TambolaRule(20, "Temperature", "Mark 1st and Last numbers of a ticket.", TambolaRuleType.TEMPERATURE, listOf(0,26),weight = 10),
    TambolaRule(21, "Below Fifty", "All numbers present on a ticket which are less than 50. Numbers from 1 to 49", TambolaRuleType.BELOW_FIFTY, listOf(0,2,4,10,12,18,20,21),weight = 10),
    TambolaRule(22, "Above Fifty", "All numbers present on a card which are greater than equal to 50. Numbers from 50 to 90", TambolaRuleType.ABOVE_FIFTY, listOf(5,7,14,15,17,25,26),weight = 10)
)


// -------------------- MAIN SCREEN --------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambolaRuleSelectionScreen(ruleViewModel: RuleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val selectedRules = ruleViewModel.selectedRules
    val context = LocalContext.current
    val selected = selectedRules.map { it.id }.toSet()
    var detailRule by remember { mutableStateOf<TambolaRule?>(null) }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {

            CenterAlignedTopAppBar(title = { Text("Select Tambola Rules") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ))
        },
        bottomBar = {
            var submitFocused by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    if (selected.isNotEmpty()) {

                        val intent =
                            Intent(context, PointsDistributionActivity::class.java)

                        intent.putIntegerArrayListExtra(
                            "SELECTED_RULE_IDS",
                            ArrayList(selected)
                        )

                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .onFocusChanged { submitFocused = it.isFocused }
                    .height(56.dp)
                    .focusable(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (submitFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (submitFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary
                )

            ) {
                Text(" Selected (${selected.size}) Rules Distribute Points", fontSize = 18.sp)
            }
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .focusProperties {
                    canFocus = false   // ⭐ prevents grid stealing focus
                },
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tambolaRules) { rule ->
                RuleCard(
                    rule = rule,
                    isSelected = rule.id in selected,
                    onToggle = {
                        ruleViewModel.toggleRule(rule)

                    },
                    onInfo = { detailRule = rule }
                )
            }
        }
    }

    detailRule?.let {
        RuleDetailDialog(rule = it, onDismiss = { detailRule = null })
    }
}

// -------------------- RULE CARD (TV DPAD) --------------------

@Composable
fun isTvDevice(): Boolean {
    val config = LocalConfiguration.current
    return config.uiMode and Configuration.UI_MODE_TYPE_MASK ==
            Configuration.UI_MODE_TYPE_TELEVISION
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RuleCard(
    rule: TambolaRule,
    isSelected: Boolean,
    onToggle: () -> Unit,
    onInfo: () -> Unit
) {
    val isTv = isTvDevice()
    var focused by remember { mutableStateOf(false) }
    var isLongPressActive by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (focused) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .height(180.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .onFocusChanged { focused = it.isFocused }
            .combinedClickable(
                onClick = {
                    if (!isLongPressActive) {
                        onToggle()
                    }
                    isLongPressActive = false
                },
                onLongClick = {
                    isLongPressActive = true
                    onInfo()
                },
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected && focused -> MaterialTheme.colorScheme.primary
                isSelected -> MaterialTheme.colorScheme.tertiaryContainer
                focused -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.secondary
            }
        ),
        border = BorderStroke(
            width = if (focused) 3.dp else 1.dp,
            color = if (focused) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {

            /* ℹ️ INFO ICON - VISIBLE ON PHONE/TAB */
            if (!isTv) {
                IconButton(
                    onClick = { onInfo() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Rule Info",
                        tint = if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer
                        else MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            /* ✅ SELECTED INDICATOR */
            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp).align(Alignment.TopStart)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            /* 📄 CONTENT */
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(rule.type.iconRes),
                    contentDescription = rule.name,
                    modifier = Modifier.size(56.dp),
                    tint = Color.Unspecified
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = rule.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )

                /* 📺 TV HINT - ONLY FOR TV */
                if (isTv ) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "OK: Select | Hold: Info",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RuleDetailDialog(rule: TambolaRule, onDismiss: () -> Unit) {
    val closeButtonFocusRequester = remember { FocusRequester() }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                // ⭐ Prevent focus from escaping to background
                .focusProperties {
                    exit = { FocusRequester.Cancel }
                }
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(rule.name, style = MaterialTheme.typography.headlineSmall, color = Color.White)

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, null, tint = Color.White)
                    }
                }

                Spacer(Modifier.height(24.dp))
                TicketVisualizer(rule.winningPattern)
                Spacer(Modifier.height(24.dp))

                Text(
                    text = rule.description,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(Modifier.height(24.dp))

                // ⭐ HIGH CONTRAST BUTTON FOR TV
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(closeButtonFocusRequester)
                        .focusable()
                        .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(100.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("CLOSE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // ⭐ Focus transition delay for TV stability
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        closeButtonFocusRequester.requestFocus()
    }
}


// -------------------- TICKET VISUAL --------------------

@Composable
fun TicketVisualizer(highlighted: List<Int>) {

    // Fixed Tambola ticket (3 x 9)
    val ticket = listOf(
        listOf(1, null, 23, null, 41, 52, null, 78, null),
        listOf(null, 15, null, 34, null, 56, 63, null, 82),
        listOf(9, null, 27, 38, null, null, null, 74, 90)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0F7FA), RoundedCornerShape(10.dp))
            .border(2.dp, Color(0xFF006064), RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {

        /* ---------- HEADER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF006064), RoundedCornerShape(6.dp))
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "TAMBOLA BOARD",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(Modifier.height(6.dp))

        /* ---------- FULL WIDTH GRID ---------- */
        ticket.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEachIndexed { colIndex, number ->
                    val index = rowIndex * 9 + colIndex
                    val active = index in highlighted && number != null

                    Box(
                        modifier = Modifier
                            .weight(1f)                // ⭐ Equal column width
                            .aspectRatio(1f)           // ⭐ Square cells
                            .padding(2.dp)
                            .background(
                                when {
                                    number == null -> Color.Transparent
                                    active -> Color(0xFF00BCD4)
                                    else -> Color.White
                                },
                                RoundedCornerShape(4.dp)
                            )
                            .border(
                                if (number != null) 1.dp else 0.dp,
                                Color.LightGray,
                                RoundedCornerShape(4.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (number != null) {
                            Text(
                                number.toString(),
                                fontSize = 12.sp,
                                fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                                color = if (active) Color.White else Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}


// -------------------- PREVIEWS --------------------

@Preview(device = Devices.PIXEL_4)
@Composable
fun PhonePreview() {
    AppTheme { TambolaRuleSelectionScreen() }
}

@Preview(device = Devices.PIXEL_C)
@Composable
fun TabletPreview() {
    AppTheme { TambolaRuleSelectionScreen() }
}

@Preview(device = Devices.TV_1080p)
@Composable
fun TvPreview() {
    AppTheme { TambolaRuleSelectionScreen() }
}
