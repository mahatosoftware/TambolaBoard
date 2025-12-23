package  `in`.mahato.tambola.game.model

import android.os.Parcelable
import `in`.mahato.tambola.rule.TambolaRuleType
import kotlinx.parcelize.Parcelize


@Parcelize
data class TambolaRule(
    val id: Int,
    val name: String,
    val description: String,
    val type: TambolaRuleType,
    val winningPattern: List<Int>,
    val quantity: Int = 1,
    val percentage: Int = 0,
    val weight: Int = 1,
    val isFullHouse: Boolean =false

) : Parcelable