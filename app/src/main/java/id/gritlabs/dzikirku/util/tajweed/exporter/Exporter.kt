package id.gritlabs.dzikirku.util.tajweed.exporter

import id.gritlabs.dzikirku.util.tajweed.model.Result

interface Exporter {
    fun export(ayah: String, results: List<Result>, stringResult:(String) -> Unit)
    fun onOutputStarted() {}
    fun onOutputCompleted() {}
}