import kotlinx.coroutines.*
import kscience.plotly.*
import kscience.plotly.models.XAnchor
import kscience.plotly.models.YAnchor
import kscience.plotly.palettes.Xkcd
import java.io.File
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun plotSignal(n: Int, wMax: Int, num: Int) {
    val signal = Signal(n, wMax, num)
    val records: Array<Float>
    val time = measureTimeMillis { records = signal.generate() }
    val m = records.average()
    val d = records.map { (it - m).pow(2) }.sum() / (num - 1)
    println("Signal generated in $time msec, m = $m, D = $d")
    plot(
        records.indices, records.toList(),
        xAxis = "t",
        yAxis = "x(t)",
        clr = Xkcd.GREEN,
    )
}

fun benchmark(
    nMin: Int,
    nMax: Int,
    nStep: Int,
    wMax: Int,
    numMin: Int,
    numMax: Int,
    out: String? = null
) {
    val nTotal = (nMax - nMin) / nStep
    val numStep = (numMax - numMin) / nTotal
    val list = List(nTotal) { i ->
        GlobalScope.async {
            val n = nMin + i * nStep
            val num = numMin + i * numStep
            val time = measureTimeMillis { Signal(n, wMax, num).generate() }
            println("$i. Generated for n = $n N = $num, $time msec")
            time
        }
    }

    runBlocking {
        val time = list.awaitAll()
        plot(time.indices.mapIndexed { i, _ -> nMin + i * nStep }, time, "n", "msec")

        if (out != null) {
            val file = File(out)
            val d = ";"
            val csv = buildString {
                append("n" + d + "t")
                time.forEachIndexed { i, time -> append("\n$i$d$time") }
            }
            file.writeText(csv)
        }
    }
}

private fun plot(
    xData: Iterable<Number>,
    yData: Iterable<Number>,
    xAxis: String? = null,
    yAxis: String? = null,
    clr: String = Xkcd.BRIGHT_BLUE,
) {
    Plotly.page(mathJaxHeader, cdnPlotlyHeader) {
        plot {
            scatter {
                x.set(xData)
                y.set(yData)
                line { color(clr) }
            }
            layout {
                height = 750
                width = 1000
                margin { l = 50; r = 20; b = 20; t = 50 }
                xaxis.title = xAxis
                yaxis.title = yAxis
                legend {
                    x = 0.97
                    y = 1
                    borderwidth = 1
                    font { size = 32 }
                    xanchor = XAnchor.right
                    yanchor = YAnchor.top
                }
            }
        }
    }.makeFile()

}
