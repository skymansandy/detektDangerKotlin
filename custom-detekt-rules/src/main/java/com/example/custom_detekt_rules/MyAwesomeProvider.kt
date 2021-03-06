package com.example.custom_detekt_rules

import com.example.custom_detekt_rules.rule.*
import com.example.custom_detekt_rules.rule.DirectIntentUseRule
import com.example.custom_detekt_rules.rule.FragmentDefaultConstructorRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class MyAwesomeProvider(override val ruleSetId: String = "my-app-my-rules") : RuleSetProvider {

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId, listOf(
                FragmentDefaultConstructorRule(),
                SingleSourceOfLaunchRule(),
                UseGCAssistSubscribeRule(),
                NoManualTextSpanRule(),
                LateInitAutoGCRule(),
                DirectIntentUseRule()
            )
        )
    }

}