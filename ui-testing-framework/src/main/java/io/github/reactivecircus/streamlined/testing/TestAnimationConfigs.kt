package io.github.reactivecircus.streamlined.testing

import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import javax.inject.Inject

class TestAnimationConfigs @Inject constructor() : AnimationConfigs {
    override val defaultStartOffset: Int = 0
    override val adapterPayloadAnimationDuration: Int = 0
}
