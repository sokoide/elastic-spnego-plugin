# Elasticsearch plug-in skeleton

## How to build

* Build and test

```sh
mvn test
mvn install
```

* Note
    * If you change `elasticsearch.version` from 6.2.1 to 6.3.2, `mvn test` fails with `Throwable #1: java.lang.RuntimeException: unable to install test security manager` (let me know how to fix it)
