
fun main() {
    plotSignal(14, 1700, 64)
    benchmark(
        nMin = 1,
        nMax = 1_000_001,
        nStep = 5000,
        wMax = 1700,
        numMin = 64,
        numMax = 64,
        "lab1-1/res/out.csv"
    )
}
