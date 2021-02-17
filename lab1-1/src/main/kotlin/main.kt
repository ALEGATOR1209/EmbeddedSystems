
fun main() {
    plotSignal(14, 1700, 64)
    benchmark(
        nMin = 1,
        nMax = 50_001,
        nStep = 5000,
        wMax = 1700,
        numMin = 1,
        numMax = 50_001,
        "res/out.csv"
    )
}
