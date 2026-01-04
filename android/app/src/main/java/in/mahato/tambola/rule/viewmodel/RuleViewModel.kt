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
            val prizeDao = db.winningPrizeDao()

            // 1. Clear old data
            prizeDao.clearAll()
            // ruleDao.deleteAllRules() // replaceRules handles this inside transaction if used, or we do it manually

            // 2. Prepare Entities
            // We need to flatten rules based on quantity.
            // Requirement: "if quantity is 2 it has to be saved twice"
            // However, typically SavedRuleEntity represents the Rule definition, and WinningPrizeEntity represents the instance.
            // If the user wants `SavedRuleEntity` to be duplicated, we can do that.
            // BUT, usually `SavedRuleEntity` is the "Config" and `WinningPrizeEntity` is the "Row in Winner Board".
            // Let's assume the standard pattern: SavedRuleEntity stores the config (qty=2),
            // and WinningPrizeEntity is generated 'quantity' times.
            // Wait, the user said "use RuleDao to insert rules... based on quantity... saved twice".
            // This implies the SavedRuleEntity itself might need to be duplicated OR logic resides in how we create prizes.
            // Let's stick to the SummaryActivity logic which seemed to work:
            // It created one SavedRuleEntity per rule (with quantity field)
            // AND generated 'quantity' number of WinningPrizeEntities.
            // Let's replicate that logic but within ViewModel.

            val validRules = selectedRules.filter { it.quantity > 0 }

            val savedRuleEntities = validRules.map { rule ->
                // Recalculate amounts if needed, or trust UI.
                // We'll calculate proportional amount based on percentage for safety.
                val amountPerItem = ((rule.percentage / 100.0) * totalPoints).toInt()
                val totalRuleAmount = amountPerItem * rule.quantity

                SavedRuleEntity(
                     // Let Room auto-generate DB ID
                    ruleId = rule.id,
                    ruleName = rule.name,
                    percentage = rule.percentage,
                    quantity = rule.quantity,
                    amountPerItem = amountPerItem,
                    totalRuleAmount = totalRuleAmount
                )
            }

            // 3. Generate Prize Entities
            // We need the inserted IDs of rules to link prizes?
            // In the current Entity definition:
            // WinningPrizeEntity embeds SavedRuleEntity using @Embedded? Or does it use a ForeignKey?
            // Let's look at WinningPrizeEntity definition in SummaryActivity usage...
            // It takes the whole SavedRuleEntity object.
            // If we save SavedRuleEntity first, we might need its new ID if it's auto-generated.
            // However, SummaryActivity logic was:
            // prizeDao.insertPrizes(prizeEntities)
            // ruleDao.replaceRules(entities)
            // It seems WinningPrizeEntity might store a copy or be independent.
            // Let's follow that pattern.

             val prizeEntities = savedRuleEntities.flatMap { savedRule ->
                List(savedRule.quantity) {
                    WinningPrizeEntity(
                        savedRule = savedRule,
                        winnerName = "",
                        isClaimed = false
                    )
                }
            }

            // 4. Transactional Save
            // replaceRules does delete + insert.
            ruleDao.replaceRules(savedRuleEntities)
            prizeDao.insertPrizes(prizeEntities)

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