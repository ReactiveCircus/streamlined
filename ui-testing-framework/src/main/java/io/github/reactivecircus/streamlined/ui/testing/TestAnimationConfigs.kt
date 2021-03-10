package io.github.reactivecircus.streamlined.ui.testing

import io.github.reactivecircus.streamlined.ui.common.configs.AnimationConfigs
import javax.inject.Inject

class TestAnimationConfigs @Inject constructor() : AnimationConfigs {
    override val defaultStartOffset: Int = 0
    override val adapterPayloadAnimationDuration: Int = 0
}
