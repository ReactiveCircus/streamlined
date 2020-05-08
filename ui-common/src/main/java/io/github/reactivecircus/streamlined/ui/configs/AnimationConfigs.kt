package io.github.reactivecircus.streamlined.ui.configs

import javax.inject.Inject

interface AnimationConfigs {
    val defaultStartOffset: Int
    val adapterPayloadAnimationDuration: Int
}

class DefaultAnimationConfigs @Inject constructor() : AnimationConfigs {
    override val defaultStartOffset = 200
    override val adapterPayloadAnimationDuration = 300
}
