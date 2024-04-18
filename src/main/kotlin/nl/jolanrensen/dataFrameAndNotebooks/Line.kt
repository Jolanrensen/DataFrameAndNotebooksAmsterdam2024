package nl.jolanrensen.dataFrameAndNotebooks

/**
 * A line consists of two stations where firstStation
 * is always alphabetically first.
 */
data class Line private constructor(
    val firstStation: String,
    val secondStation: String,
) {
    companion object {
        operator fun invoke(station: String, otherStation: String): Line {
            val (a, b) = listOf(station, otherStation).sorted()
            return Line(a, b)
        }

        fun parse(rdtString: String): Line {
            val (a, b) = rdtString.split(" - ")
            return invoke(a, b)
        }
    }
}