package benchmark

import benchmark.services.MasterService
import java.io.File
import java.net.URL
import java.net.URLClassLoader

fun main() {
    val masterService = MasterService()
    masterService.runBenchmark()
}