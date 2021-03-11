import kscience.plotly.*
import kscience.plotly.models.XAnchor
import kscience.plotly.models.YAnchor
import kscience.plotly.palettes.Xkcd

fun plotDFT(n: Int, wMax: Int, num: Int, normed: Boolean = false) {
    val s = Signal(n, wMax, num)
    val dft = s.dft(normed)

    Plot("w", "A").apply {
        addLine(dft.indices, dft, Xkcd.BLUE, "DFT")
    }.draw()
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
