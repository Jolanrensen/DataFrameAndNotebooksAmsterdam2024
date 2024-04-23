// We import the data schema from openApi
// This can also be done with a Gradle task
@file:ImportDataSchema(
    name = "GoTrain",
    path = "https://raw.githubusercontent.com/rijdendetreinen/gotrain/main/openapi.yaml",
)

package nl.jolanrensen.dataFrameAndNotebooks

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import nl.jolanrensen.dataFrameAndNotebooks.GoTrain.Departure.Companion.convertToDeparture
import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.DataFrame
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

fun main() {

    // generated
    GoTrain.SystemStatus

    // my google cloud server running the server
    val address = "http://34.141.147.240:8080"
    val stationCode = "ASD" // Amsterdam Central
    getDeparturesForStation(stationCode, address).print(borders = true)
}


/**
 * Using the generated [GoTrain.Departure] data schema,
 * this function retrieves a preprocessed [DataFrame]<[MyDeparture]> of given [stationCode] and [address].
 *
 * We'll reuse this in the notebook!
 */
fun getDeparturesForStation(stationCode: String, address: String): DataFrame<MyDeparture> {
    val df = DataFrame.readJson("$address/v2/departures/station/$stationCode")
        .getFrameColumn("departures").first()

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

        .convertTo<MyDeparture>()

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

