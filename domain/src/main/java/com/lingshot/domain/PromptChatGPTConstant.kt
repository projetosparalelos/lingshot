package com.lingshot.domain

object PromptChatGPTConstant {
    val PROMPT_TRANSLATE: (String?) -> String = { language ->
        "You translate and improve the meaning of the sentences into $language."
    }
    const val PROMPT_CORRECT_SPELLING = "Correct the spelling if necessary, otherwise leave it as it is."
}
