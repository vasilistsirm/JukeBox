

It configures the compiler to use UTF-8, Java 11

It includes SLF4J as logging API, logback (with ISO8601 timestamps) as logging backend, JUnit 5 for testing, and AssertJ for test assertions.

It copies the dependencies to target/lib, and configures the JAR file with a main class and a classpath,
so that the resulting JAR can be run with `java -jar`.




## Building and run

`cd` `{path of the folder}` to go to the folder where the application is located.

Run into the folder which the application is located `mvn clean package` and check the `target` folder.

Now, run `java -jar target/jukebox-1.0-SNAPSHOT.jar ` and you will see the GUI of my application

In the report file, there are detailed instructions on how to use my application.








