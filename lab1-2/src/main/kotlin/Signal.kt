import java.util.*
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Signal(val n: Int, val wMax: Int, val num: Int) {
    var values: Array<Float> = arrayOf()
        get() {
            if (field.size < num) generate()
            return field
        }
        private set

    val x: List<Int>
        get() = values.indices.toList()

    val y: List<Float>
        get() = values.toList()

    val m
        get() = values.average()

    val d
        get() = values.map { (it - m).pow(2) }.sum() / (num - 1)

    private fun generate() {
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

        values = signals
    }

    infix fun tau(tau: Int): Signal {
        if (tau >= num) error("Invalid tau: $tau/$num")
        return Signal(n, wMax, num - tau).also {
            it.values = values.drop(tau).toTypedArray()
        }
    }

    fun correlation(that: Signal, normed: Boolean = false): Float {
        val n = min(this.num, that.num)
        var cov = 0f.toDouble()

        val x = this.values
        val mx = this.m
        val y = that.values
        val my = that.m

        for (i in 0 until n) cov += (x[i] - mx) * (y[i] - my)

        val dx = x.map { (it - mx).pow(2) }.sum()
        val dy = y.map { (it - my).pow(2) }.sum()

        val corr = if (normed) cov / sqrt(dx * dy) else cov / (n - 1)
        return corr.toFloat()
    }
}
