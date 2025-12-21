package `in`.mahato.tambola.rule

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RuleViewModel : ViewModel() {

    var selectedRules by mutableStateOf<List<TambolaRule>>(emptyList())
        private set

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
    }

    fun updatePercentage(index: Int, delta: Int) {
        selectedRules = selectedRules.mapIndexed { i, rule ->
            if (i == index) {
                rule.copy(
                    percentage = (rule.percentage + delta)
                        .coerceIn(0, 100)
                )
            } else rule
        }
    }

    fun updateQuantity(index: Int, delta: Int, autoMode: Boolean) {
        selectedRules = selectedRules.mapIndexed { i, rule ->
            if (i == index) {
                rule.copy(quantity = (rule.quantity + delta).coerceAtLeast(1))
            } else rule
        }
        if (autoMode) autoDistribute()
    }

    fun autoDistribute() {
        if (selectedRules.isEmpty()) return

        val totalQty = selectedRules.sumOf { it.quantity }
        var remaining = 100

        selectedRules = selectedRules.mapIndexed { index, rule ->
            val percent =
                if (index == selectedRules.lastIndex)
                    remaining
                else
                    (rule.quantity * 100) / totalQty

            remaining -= percent
            rule.copy(percentage = percent)
        }
    }
}
