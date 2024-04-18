// We import the data schema from openApi
@file:ImportDataSchema(
    name = "GoTrain",
    path = "https://raw.githubusercontent.com/rijdendetreinen/gotrain/main/openapi.yaml",
)

package nl.jolanrensen.dataFrameAndNotebooks

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import nl.jolanrensen.dataFrameAndNotebooks.GoTrain.Departure.Companion.convertToDeparture
import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.ColumnName
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.annotations.ImportDataSchema
import org.jetbrains.kotlinx.dataframe.api.add
import org.jetbrains.kotlinx.dataframe.api.colsOf
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.convertTo
import org.jetbrains.kotlinx.dataframe.api.first
import org.jetbrains.kotlinx.dataframe.api.getFrameColumn
import org.jetbrains.kotlinx.dataframe.api.mapIndexed
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.renameToCamelCase
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.api.sortBy
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.readJson
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@DataSchema
interface Departure {
    val cancelled: Boolean
    val company: String
    val delay: Int
    val departureTime: Instant
    val destinationActual: String
    val destinationActualCodes: List<String>
    val destinationPlanned: String
    val lineNumber: Instant?
    val name: String?
    val platformActual: String
    val platformChanged: Boolean
    val platformPlanned: String
    val remarks: List<String>
    val serviceDate: LocalDate
    val serviceId: String
    val serviceNumber: String
    val station: String
    val status: Int
    val timestamp: Instant
    val tips: List<String>
    val type: String
    val typeCode: String
    val via: String?
    val destinationAndType: String
    val departureTimeWithDelay: Instant
    val wings: DataFrame<GoTrain.DepartureWing?>
}

/**
 * Retrieves a [DataFrame]<[Departure]> of given [stationCode] and [address].
 */
fun getDeparturesForStation(stationCode: String, address: String): DataFrame<Departure> {
    val df = DataFrame.readJson("$address/v2/departures/station/$stationCode")
        .getFrameColumn("departures")
        .first()
        .convertToDeparture {
            convert<List<Any>>().with { it.map { it.toString() } } // convert (empty) List<Any> to List<String>
        }
        .add {
            "destinationAndType" from {
                "$destinationPlanned ($type)" // type safe accessors!
            }
            "departureTimeWithDelay" from {
                departureTime!! + delay!!.minutes
            }
        }

        // times are in utc, we want Instants and to be in Amsterdam time for the plotting
        .convert { colsOf<LocalDateTime?>() }.with {
            it?.toInstant(TimeZone.UTC)?.plus(
                TimeZone.of("Europe/Amsterdam")
                    .offsetAt(Clock.System.now())
                    .totalSeconds
                    .seconds
            )
        }
        .sortBy { departureTime }
        .renameToCamelCase()
        .convertTo<Departure>()

    df.schema().print()

    return df
}


inline fun <reified T> DataColumn<T>.setToNullsWhere(maskColumn: DataColumn<Boolean>): DataColumn<T?> =
    mapIndexed { i, it -> if (maskColumn[i]) null else it }

fun currentTimeInAmsterdam(): Instant =
    Clock.System.now() +
            TimeZone.of("Europe/Amsterdam")
                .offsetAt(Clock.System.now())
                .totalSeconds
                .seconds

operator fun LocalDateTime.plus(duration: Duration): LocalDateTime =
    toInstant(TimeZone.UTC)
        .plus(duration)
        .toLocalDateTime(TimeZone.UTC)

fun main() {
    // my google cloud server running the server
    val address = "http://34.141.147.240:8080"
    val stationCode = "ASD" // Amsterdam Central

    getDeparturesForStation(stationCode, address).print(borders = true)

}

