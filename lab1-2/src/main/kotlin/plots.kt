import kscience.plotly.*
import kscience.plotly.models.XAnchor
import kscience.plotly.models.YAnchor
import kscience.plotly.palettes.Xkcd
import java.io.File
import kotlin.math.min

private fun plotCorrelation(
    s1: Signal,
    s2: Signal,
    tauMin: Int,
    tauMax: Int,
    normed: Boolean,
    name: String,
    color: String,
    file: File,
) {
    val num = min(s1.num, s2.num)
    if (tauMin >= num || tauMax >= num) error("Invalid bounds: $tauMin-$tauMax")
    if (tauMin < 0 || tauMax < tauMin) error("Invalid bounds: $tauMin-$tauMax")

    val correlations = List(tauMax - tauMin + 1) {
        val i = tauMin + it
        s1.correlation(s2 tau i, normed)
    }

    Plot("tau", name).apply {
        addLine(correlations.indices.map { it + tauMin }, correlations, color, name)
    }.draw()

    val csv = buildString {
        append("corr;tau")
        correlations.forEachIndexed { i, cor -> append("\n${tauMin + i};$cor") }
    }

    file.writeText(csv)
}

fun plotSelfCorrelation(
    n: Int,
    wMax: Int,
    num: Int,
    tauMin: Int,
    tauMax: Int,
    normed: Boolean = false,
    out: String,
) {
    val s1 = Signal(n, wMax, num)
    plotCorrelation(s1, s1, tauMin, tauMax, normed, "cor(s1, s1 + tau)", Xkcd.GREEN, File(out))
}

fun plotCloneCorrelation(
    n: Int,
    wMax: Int,
    num: Int,
    tauMin: Int,
    tauMax: Int,
    normed: Boolean = false,
    out: String,
) {
    val s1 = Signal(n, wMax, num)
    val s2 = Signal(n, wMax, num)
    plotCorrelation(s1, s2, tauMin, tauMax, normed, "cor(s1, s2 + tau)", Xkcd.BLUE, File(out))
}

private class Plot(
    private val xAxis: String?,
    private val yAxis: String?,
) {
    private val lines = mutableListOf<Line>()

    fun addLine(x: Iterable<Number>, y: Iterable<Number>, color: String, name: String) {
        lines += Line(x, y, color, name)
    }

    fun draw() {
        Plotly.page(mathJaxHeader, cdnPlotlyHeader) {
            plot {
                lines.forEach { line ->
                    scatter {
                        x.set(line.x)
                        y.set(line.y)
                        line { color(line.color) }
                        name = line.name
                    }
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

    private class Line(
        val x: Iterable<Number>,
        val y: Iterable<Number>,
        val color: String,
        val name: String,
    )
}
