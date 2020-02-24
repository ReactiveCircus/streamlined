package io.github.reactivecircus.streamlined.design

import com.google.android.material.imageview.ShapeableImageView

fun ShapeableImageView.enableDefaultCornerRadius() {
    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
        .setAllCornerSizes(resources.getDimension(R.dimen.dimen_8))
        .build()
}
