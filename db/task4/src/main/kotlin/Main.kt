import kotlinx.coroutines.*
import java.sql.DriverManager
import java.sql.Statement
import kotlin.system.measureNanoTime
import kotlin.time.ExperimentalTime

data class Plane(val aircraftCode: String, val fareCondition: String)
data class Flight(val code: Int, val aircraftCode: String)

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    Class.forName("org.postgresql.Driver")
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://127.0.0.1:5432/demo",
        "root",
        "root"
    )


    val time = measureNanoTime {
        val spaceInAircraftByCondition = getAvailablePlanesAndConditions(connection.createStatement())
        println(spaceInAircraftByCondition)
        val flights = getFlights(connection.createStatement())
//    println(flights)
        val data =
            getInformationAboutFlightAndSeatPrice(flights, spaceInAircraftByCondition, connection.createStatement())
        println("migration")
        val realSeatInformation = getRealSeatInformation(spaceInAircraftByCondition, data)
//        println(realSeatInformation)
        for (p in realSeatInformation.keys) {
            val (planeCode, fareCondition) = p
            if (realSeatInformation[p]!!.keys.size > 1) {
                connection.createStatement().use {
                    for(seatNo in realSeatInformation[p]!![1]!!){
                        val query = "UPDATE bookings.seats SET extra_space=true WHERE aircraft_code = '$planeCode' " +
                                "AND fare_conditions = '$fareCondition' " +
                                "AND seat_no = '$seatNo'"
                        it.execute(query)
                    }
                }
            }
        }
        connection.close()
    }
    println(time / 1_000_000_000)
}

fun getAvailablePlanesAndConditions(statement: Statement)
        : MutableList<Plane> {
    println("GET INFO ABOUT AIRPLANES")
    val sqlRequest = "SELECT aircraft_code, fare_conditions FROM bookings.seats " +
            "GROUP BY aircraft_code, fare_conditions "
    val res = mutableListOf<Plane>()

    statement.use { st ->
        val resSet = st.executeQuery(sqlRequest)
        while (resSet.next()) {
            val model = resSet.getString("aircraft_code")
            val condition = resSet.getString("fare_conditions")
            res.add(Plane(model, condition))
        }

    }
    return res
}


fun getFlights(statement: Statement): List<Flight> {
    val sql = "SELECT flight_id, aircraft_code FROM bookings.flights ORDER BY flight_id"
    val res = mutableListOf<Flight>()
    statement.use {
        val set = statement.executeQuery(sql)
        while (set.next()) {
            val flightId = set.getInt("flight_id")
            val aircraftCode = set.getString("aircraft_code")
            res.add(Flight(flightId, aircraftCode))
        }
    }
    return res
}

data class PlaneFlightSeatCost(
    val flightId: Int,
    val planeCode: String,
    val fareCondition: String,
    val priceToSeat: Map<Float, Set<String>>
)

fun getInformationAboutFlightAndSeatPrice(
    flights: List<Flight>,
    planes: List<Plane>,
    statement: Statement
): List<PlaneFlightSeatCost> {
    val t = mutableListOf<PlaneFlightSeatCost>()
    runBlocking {
        val coroutines = mutableListOf<Deferred<PlaneFlightSeatCost>>()
        for (f in flights) {
            planes
                .filter { it.aircraftCode == f.aircraftCode }
                .forEach { planeFare ->
                    val query = "SELECT bp.flight_id," +
                            "       fare_conditions, " +
                            "       seat_no, " +
                            "       amount " +
                            "FROM bookings.ticket_flights " +
                            "         INNER JOIN bookings.boarding_passes bp " +
                            "                    on ticket_flights.ticket_no = bp.ticket_no " +
                            "                       and ticket_flights.flight_id = bp.flight_id " +
                            "WHERE bp.flight_id = ${f.code} " +
                            "AND fare_conditions = '${planeFare.fareCondition}'"
                    val c = async(start = CoroutineStart.DEFAULT) {
                        val ans = mutableMapOf<Float, MutableSet<String>>()
                        val t = statement.executeQuery(query)
                        while (t.next()) {
                            val seatNo = t.getString("seat_no")
                            val amount = t.getFloat("amount")
                            if (amount !in ans.keys) {
                                ans[amount] = mutableSetOf()
                            }
                            ans[amount]!!.add(seatNo)
                        }
                        PlaneFlightSeatCost(
                            f.code,
                            f.aircraftCode,
                            planeFare.fareCondition,
                            ans
                        )
                    }
                    coroutines.add(c)
                }
        }
        println("all threads created")
        coroutines
            .awaitAll()
            .forEach {
                t.add(it)
            }
    }
    return t
}

data class G(val planeCode: String, val fareCondition: String, val price: Map<Int, Set<String>>)

fun getRealSeatInformation(planes: List<Plane>, data: List<PlaneFlightSeatCost>): Map<
        Pair<String, String>,
        Map<Int, Set<String>>
        > {
    val res: MutableMap<Pair<String, String>, MutableMap<Int, MutableSet<String>>> = mutableMapOf()
    for ((plane, farecondition) in planes) {
        res[Pair(plane, farecondition)] = mutableMapOf()
    }
    for (i in data) {
        val key = Pair(i.planeCode, i.fareCondition)
        i.priceToSeat.keys.sorted().forEachIndexed { index, fl ->
            if (index !in res[key]!!.keys) {
                res[key]!![index] = mutableSetOf()
            }
            i.priceToSeat[fl]!!.forEach {
                if (!res[key]!![index]!!.contains(it)) {
                    res[key]!![index]!!.add(it)
                }
            }
        }
    }
    return res
}
