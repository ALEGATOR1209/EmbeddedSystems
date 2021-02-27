
fun main() {
    plotSignal(14, 1700, 64)
    benchmark(
        nMin = 14000, // збільшено кількість гармонік, бо інакше сигнал рахується надто швидко
        nMax = 14000,
        wMax = 1700,
        numMin = 300,
        numMax = 3000,
        numStep = 300,
        "lab1-1/res/out.csv"
    )
}
