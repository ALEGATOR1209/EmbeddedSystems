import kotlin.math.*

data class Complex(val r: Double, val i: Double) {
    operator fun times(times: Float) = Complex(r * times, i * times)
    operator fun times(times: Double) = Complex(r * times, i * times)
    operator fun times(times: Complex) = Complex(r * times.r - i * times.i, i * times.r + r * times.i)
    operator fun plus(that: Complex) = Complex(this.r + that.r, this.i + that.i)
    operator fun plus(that: Double) = Complex(this.r + that, this.i)
    operator fun minus(that: Complex) = Complex(this.r - that.r, this.i - that.i)
    fun abs() = sqrt(r.pow(2) + i.pow(2))
}

operator fun Double.plus(that: Complex) = Complex(that.r + this, that.i)
operator fun Double.minus(that: Complex) = Complex(this - that.r, -that.i)

typealias W = (Int) -> Complex

fun wWithBase(n: Int): (Int) -> Complex {
    val arg = 2 * PI / n
    val cache = mutableMapOf<Int, Complex>()

    return { i: Int ->
        cache.getOrPut(i % n) {
            Complex(cos(arg * i), -sin(arg * i))
        }
    }
}

fun dft(values: List<Float>, w: W? = null): List<Complex> {
    val num = values.size
    val w = w ?: wWithBase(num)

    return List(num) { p ->
        var f = Complex(0.0, 0.0)
        for (k in 0 until num) f += w(p * k) * values[k]
        f
    }
}

fun Signal.dft(normed: Boolean = false): List<Double> {
    val f = dft(y).map { it.abs() / num }
    return if (normed) f.map { it * 2 }.dropLast(num / 2) else f
}

private fun fft(values: List<Float>): List<Complex> {
    val n = values.size
    val w = wWithBase(n)
    if (n <= 32) return dft(values)

    val half = n / 2
    val even = MutableList(n / 2) { 0f }
    val odd = MutableList(n / 2) { 0f }

    for (i in 0 until half) {
        even[i] = values[2 * i]
        odd[i] = values[2 * i + 1]
    }

    val xEven = fft(even)
    val xOdd = fft(odd)

    val f = MutableList(n) { Complex(0.0, 0.0) }
    for (p in 0 until half) {
        f[p] = xEven[p] + w(p) * xOdd[p]
        f[half + p] = xEven[p] - w(p) * xOdd[p]
    }

    return f
}

fun Signal.fft(normed: Boolean = false): List<Double> {
    val f = fft(y).map { it.abs() / num }
    return if (normed) f.map { it * 2 }.dropLast(num / 2) else f
}
