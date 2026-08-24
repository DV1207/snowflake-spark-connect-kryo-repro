# Snowpark Connect: Encoders.kryo is unimplemented

Minimal repro proving `org.apache.spark.sql.Encoders.kryo` throws
`NoSuchMethodError` when run against Snowflake via `snowpark-submit`
(Snowpark Connect for Scala/Spark).

## Repro

`src/main/scala/KryoRepro.scala` calls only:

```scala
val enc: Encoder[Map[String, Int]] = Encoders.kryo[Map[String, Int]]
```

No custom classes, no aggregation, no fingerprinting logic — just the
bare encoder call.

## Build

```
sbt assembly
```

## Submit to Snowflake

```
snowpark-submit \
  --class KryoRepro \
  --compute-pool "<compute_pool>" \
  --snowflake-workload-name kryo_minimal_repro \
  --account "<account>" \
  --host "<account>.snowflakecomputing.com" \
  --user <user> \
  --password "<token>" \
  --wait-for-completion \
  target/scala-2.12/kryo_repro-assembly-0.1.0-SNAPSHOT.jar
```

## Result

```
FAILED: java.lang.NoSuchMethodError: 'org.apache.spark.sql.Encoder org.apache.spark.sql.Encoders$.kryo(scala.reflect.ClassTag)'
```

## Conclusion

Snowpark Connect's Scala `Encoders` implementation does not provide
`.kryo`. Any Spark `Aggregator`/UDAF that requires Kryo-serialized
buffer/output encoders cannot run under `snowpark-submit` today.
Matches Snowflake's own documentation that Java/Scala support for
Snowpark Connect is still in progress (Python-first as of this test).
