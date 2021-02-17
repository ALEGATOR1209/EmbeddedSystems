import java.util.*
import kotlin.math.sin

data class Signal(val n: Int, val wMax: Int, val num: Int)

fun Signal.generate(): Array<Float> {
    val random = Random()
    val signals = Array(num) { 0f }
    for (i in 1..n) {
        val a = random.nextFloat()
        val fi = random.nextFloat()
        val w = wMax.toFloat() * i / n
        for (t in 0 until num) {
            val s = a * sin(w * t + fi)
            signals[t] += s
        }
    }

    return signals
}
