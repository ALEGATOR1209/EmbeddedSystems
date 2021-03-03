fun main() {
    plotSelfCorrelation(14, 1700, 64, 0, 30, normed = true, "lab1-2/res/self.csv")
    plotCloneCorrelation(14, 1700, 64, 0, 30, normed = true, "lab1-2/res/corr.csv")
}
