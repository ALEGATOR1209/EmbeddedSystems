import kotlin.system.measureTimeMillis

fun benchmarkFT(n: Int, wMax: Int, num: Int) {
    val s = Signal(n, wMax, num)
    val timeDFT = measureTimeMillis { s.dft() }
    println("DFT: $timeDFT msec")
    val timeFFT = measureTimeMillis { s.fft() }
    println("FFT: $timeFFT msec")
    println("DFT/FFT: ${timeDFT.toFloat() / timeFFT}")
}
