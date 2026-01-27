package `in`.mahato.tambola.rule.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import `in`.mahato.tambola.db.AppDatabase
import `in`.mahato.tambola.rule.entity.SavedRuleEntity
import `in`.mahato.tambola.rule.entity.WinningPrizeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import `in`.mahato.tambola.game.model.TambolaRule

class RuleViewModel(application: Application) : AndroidViewModel(application) {

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
            if (selectedRules.any { it.id == rule.id })
                selectedRules.filterNot { it.id == rule.id }
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
    // SAVE TO DB
    // -----------------------------

    fun saveRules(totalPoints: Int, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(getApplication())
            val ruleDao = db.ruleDao()

            // 1. Filter valid rules
            val validRules = selectedRules.filter { it.quantity > 0 }

            // 2. Map to Entities
            val savedRuleEntities = validRules.map { rule ->
                // Basic amount calculation - Logic can be refined if needed
                val amountPerItem = ((rule.percentage / 100.0) * totalPoints).toInt()
                val totalRuleAmount = amountPerItem * rule.quantity

                SavedRuleEntity(
                    ruleId = rule.id,
                    ruleName = rule.name,
                    percentage = rule.percentage,
                    quantity = rule.quantity,
                    amountPerItem = amountPerItem,
                    totalRuleAmount = totalRuleAmount
                )
            }

            // 3. Transactional Save (Clear + Insert)
            ruleDao.replaceRules(savedRuleEntities)

            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
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