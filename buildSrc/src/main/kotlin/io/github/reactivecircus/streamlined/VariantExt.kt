package io.github.reactivecircus.streamlined

import com.android.build.api.variant.BuildConfigField
import com.android.build.api.variant.ResValue
import com.android.build.api.variant.Variant
import java.io.Serializable

fun Variant.addResValue(key: String, type: String, value: String) {
    resValues.put(makeResValueKey(type, key), ResValue(value))
}

fun <T : Serializable> Variant.addBuildConfigField(key: String, value: T) {
    val buildConfigField = BuildConfigField(type = value::class.java.simpleName, value = value, comment = null)
    buildConfigFields.put(key, buildConfigField)
}
