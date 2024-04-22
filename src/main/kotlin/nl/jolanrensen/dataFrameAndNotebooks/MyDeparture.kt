package nl.jolanrensen.dataFrameAndNotebooks

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema

@DataSchema
interface MyDeparture {
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