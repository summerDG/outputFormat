import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types.StructType

case class Performance(
                   partitions: Int,
                   query: String,
                   minExecTime: Double,
                   maxExecTime: Double,
                   avgExecTime: Double,
                   execTimeStdDev: Double,
                   minOptTime: Double,
                   maxOptTime: Double,
                   avgOptTime: Double,
                   optTimeStdDev: Double
                 ) {
  def apply(partitions: Int, schema: StructType, row: Row): Performance = {
    val fields = schema.fields
    val q = fields.indexWhere(s => s.name == "query")
    val minExec = fields.indexWhere(s => s.name == "minExeTimeMs")
    val maxExec = fields.indexWhere(s => s.name == "maxExeTimeMs")
    val avgExec = fields.indexWhere(s => s.name == "avgExeTimeMs")
    val execDev = fields.indexWhere(s => s.name == "exeStdDev")
    val minOpt = fields.indexWhere(s => s.name == "minOptTimeMs")
    val maxOpt = fields.indexWhere(s => s.name == "maxOptTimeMs")
    val avgOpt = fields.indexWhere(s => s.name == "avgOptTimeMs")
    val optDev = fields.indexWhere(s => s.name == "optStdDev")
    new Performance(partitions,
      row.getString(q),
      row.getDouble(minExec),
      row.getDouble(maxExec),
      row.getDouble(avgExec),
      row.getDouble(execDev),
      row.getDouble(minOpt),
      row.getDouble(maxOpt),
      row.getDouble(avgOpt),
      row.getDouble(optDev)
    )
  }
}
