fun main() {
    plotSelfCorrelation(14, 1700, 1024, 0, 128, normed = true, "lab1-2/res/self.csv")
    plotCloneCorrelation(14, 1700, 1024, 0, 128, normed = true, "lab1-2/res/corr.csv")
}
