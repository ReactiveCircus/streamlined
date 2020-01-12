package io.github.reactivecircus.streamlined.testing

import io.github.reactivecircus.streamlined.ui.util.AnimationConfigs

class TestAnimationConfigs : AnimationConfigs() {

    override val defaultStartOffset: Int
        get() = 0

    override val defaultListItemAnimationStartOffset: Int
        get() = 0

    override val adapterPayloadAnimationDuration: Int
        get() = 0
}
