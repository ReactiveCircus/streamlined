package io.github.reactivecircus.store.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test

@ExperimentalStdlibApi
class RefreshScopeGeneratorTest {

    @Test
    fun `generated refreshScope is a unique combination of Store key and the Store's Output type`() {
        assertThat(getRefreshScope<String, Int>("key1").scope)
            .isEqualTo("key1 class java.lang.Integer")

        assertThat(getRefreshScope<String, Long>("key2").scope)
            .isEqualTo("key2 class java.lang.Long")

        assertThat(getRefreshScope<Int, ArrayDeque<String>>(3).scope)
            .isEqualTo("3 class kotlin.collections.ArrayDeque")
    }
}
