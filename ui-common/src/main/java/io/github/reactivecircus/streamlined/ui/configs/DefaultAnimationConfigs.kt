package io.github.reactivecircus.streamlined.ui.configs

import javax.inject.Inject

class DefaultAnimationConfigs @Inject constructor() : AnimationConfigs {

    override val defaultStartOffset = 200

    override val defaultListItemAnimationStartOffset = 10

    override val adapterPayloadAnimationDuration = 300
}
