### How to create a Scala project from scratch

Setup folders:
```
mkdir spark-scala-skeleton
cd spark-scala-skeleton
mkdir -p src/main/scala
mkdir -p src/main/resources
mkdir -p src/test/scala
mkdir project
```

Create file for entry point for the app
```
touch src/main/scala/Boot.scala
```
with just an object extending `App` and a `println` statement (to first verify that general setup without Spark works). 

Configure sbt version:
```
echo "sbt.version=0.13.11" > project/build.properties
```

Configure sbt to build the app:
```
vim project/Build.scala
```
In `Build.scala` set the following things:
* `commonSettings` including `scalaVersion`, `organization`, `scalacOptions` and `libraryDependencies`
* `coreDependencies` including dependencies for Spark (incl. testing classes), logging, json reading, and Scala test
* the `Project` to use the `commonSettings`

Configure sbt plugins for convenience. 
Here I only included [sbt-revolver](https://github.com/spray/sbt-revolver) to be able to use `reStart` in sbt:
```
vim project/plugins.sbt
```

Configure logback to reduce noise in Spark output logs:
```
touch src/main/resources/logback.xml
```

Now type `sbt` and type `update` to download dependencies, 
`compile` to verify that there are no compilation errors and finally
`run` (or `reStart`) to run the app.
This should print the statement of the `println` defined in `Boot.scala`.

### Now add Spark into the mix

Create an external file for the appliction config
```
touch src/main/resources/application.conf
```
which includes a setting of how many cores Spark should (locally) use.
Set the value to less than your machine has, to ensure that you'll always
have control over your machine even if Spark is doing heavy computations.
```
spark {
  cores = 4
  cores = ${?SPARK_CORES}
}
```

Add Spark context to the app in `Boot.scala` with the cores setting read from the application configuration:
```
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object Boot extends App {
  val conf = ConfigFactory.load()

  val sparkConf =
    new SparkConf()
      .setAppName("Skeleton")
      .setMaster(s"local[${conf.getString("spark.cores")}]")

  implicit val sc = new SparkContext(sparkConf)
  implicit val sqlContext: SQLContext = new org.apache.spark.sql.SQLContext(sc)
  
  sc.stop()
}
```

Define the created Spark Context and the SparkSQL context as implicit vals so that you don't need to pass them later
to the functions that need them.

Now check in sbt that everything is set up correctly:
```
sbt
compile
reStart
```

Note: Since `reStart` forks a jvm, the logging in sbt is indicating errors, when in fact 
the messages are just on stdout of the forked process. 

When that worked as expected, you can now start implementing the Spark job(s) you want. To run them just do again 
```
sbt
compile
reStart
```

While the Spark job runs you can check it at [http://localhost:4040](http://localhost:4040).


### Packaging the job 

Using the plugin [sbt-native-packager](https://github.com/sbt/sbt-native-packager) you can run the command 
`stage` in sbt to create a packaged jar and a shell script to run the job outside sbt (and without the messages
indicated on ERROR level): 
```
target/universal/stage/bin/skeleton
```