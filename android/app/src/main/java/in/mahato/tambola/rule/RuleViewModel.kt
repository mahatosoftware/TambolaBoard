package `in`.ahato.tambola.rule

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import `in`.mahato.tambola.rule.TambolaRule

class RuleViewModel : ViewModel() {

    var selectedRules by mutableStateOf<List<TambolaRule>>(emptyList())
        private set

    // -----------------------------
    // SET / TOGGLE
    // -----------------------------

    fun setRules(rules: List<TambolaRule>) {
        selectedRules = rules
        autoDistribute()
    }

    fun toggleRule(rule: TambolaRule) {
        selectedRules =
            if (rule in selectedRules)
                selectedRules - rule
            else
                selectedRules + rule

        autoDistribute()
    }

    // -----------------------------
    // MANUAL UPDATES
    // -----------------------------

    fun updatePercentage(index: Int, delta: Int) {
        selectedRules = selectedRules.mapIndexed { i, rule ->
            if (i == index) {
                rule.copy(
                    percentage = (rule.percentage + delta).coerceIn(0, 100)
                )
            } else rule
        }
    }

    fun updateQuantity(index: Int, delta: Int, autoMode: Boolean) {
        selectedRules = selectedRules.mapIndexed { i, rule ->
            if (i == index) {
                rule.copy(
                    quantity = (rule.quantity + delta).coerceAtLeast(1)
                )
            } else rule
        }

        if (autoMode) autoDistribute()
    }

    // -----------------------------
    // AUTO DISTRIBUTION (FIXED)
    // -----------------------------

    fun autoDistribute() {
        if (selectedRules.isEmpty()) return

        val rules = selectedRules

        val fullHouses = rules.filter { it.isFullHouse }
        val others = rules.filterNot { it.isFullHouse }

        // Full House allocation rule
        val fullHousePercent = when {
            fullHouses.size == 1 -> 40
            fullHouses.size > 1 -> 60
            else -> 0
        }

        val remainingPercent = 100 - fullHousePercent
        val fullHouseWeight = fullHouses.sumOf { it.weight }
        val otherTotalWeight = others.sumOf { it.weight * it.quantity }

        val updatedRules = rules.map { rule ->
            when {
                rule.isFullHouse && fullHouses.isNotEmpty() -> {
                    rule.copy(
                        percentage = fullHousePercent *rule.weight/ fullHouseWeight
                    )
                }

                !rule.isFullHouse && otherTotalWeight > 0 -> {
                    val percent =
                        ((rule.weight * rule.quantity).toDouble() /
                                otherTotalWeight * remainingPercent).toInt()

                    rule.copy(percentage = percent)
                }

                else -> rule.copy(percentage = 0)
            }
        }

        // ✅ REASSIGN (NO clear / addAll)
        selectedRules = normalize(updatedRules)
    }

    // -----------------------------
    // NORMALIZE TO EXACT 100%
    // -----------------------------

    private fun normalize(rules: List<TambolaRule>): List<TambolaRule> {
        val total = rules.sumOf { it.percentage }
        val diff = 100 - total

        if (diff == 0 || rules.isEmpty()) return rules

        val maxRule = rules.maxBy { it.percentage }

        return rules.map {
            if (it == maxRule)
                it.copy(percentage = it.percentage + diff)
            else it
        }
    }
}
