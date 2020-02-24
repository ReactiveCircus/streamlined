package io.github.reactivecircus.streamlined.ui.util

sealed class AdapterItem<out Data : Any, out HeaderData : Any, out FooterData : Any> {
    data class Content<out Data : Any>(
        val data: Data
    ) : AdapterItem<Data, Nothing, Nothing>()

    data class Header<out HeaderData : Any>(
        val headerData: HeaderData
    ) : AdapterItem<Nothing, HeaderData, Nothing>()

    data class Footer<out FooterData : Any>(
        val footerData: FooterData
    ) : AdapterItem<Nothing, Nothing, FooterData>()
}
