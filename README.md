# presto-query-logger

presto-query-logger is a Presto([prestosql/presto](https://github.com/prestosql/presto)) Event Listener Plugin implementation for logging all queries to a file.

## Usage

### 1. Build

```
mvn clean package
```

### 2. Deploy plugin and dependency jars

Put the following files to Presto plugin directory(`<path-to-presto>/plugin/presto-query-logger/`).

- `target/presto-query-logger.jar`
- `target/dependency/*`


### 3. Create configuration files

Create 2 configuration files:
- `event-listener.properties`
- `presto-query-logger-log4j2.xml`

There are sample configuration files in `sample_config/` .

#### `event-listener.properties`

Create a properties file named `event-listener.properties` with the following properties:
```
event-listener.name=query-file-logging-event-listener
presto-query-logger.log4j2.config-location=<path-to-presto>/etc/presto-query-logger-log4j2.xml
```

Sample configuration: `sample_config/event-listener.properties`


#### `presto-query-logger-log4j2.xml`

Create a log4j2 configuration file named `presto-query-logger-log4j2.xml` like the following:
```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN" name="presto-query-logger">
    <properties>
        <Property name="log-dir-path">/usr/lib/presto/logs</Property>
    </properties>
    <Appenders>
        <RollingFile name="JsonRollingQueryFile">
            <FileName>${log-dir-path}/presto-query.log</FileName>
            <FilePattern>${log-dir-path}/presto-query-%d{yyyy-MM-dd}-%i.log</FilePattern>
            <JsonLayout charset="UTF-8" compact="true" eventEol="true" includeStacktrace="false" objectMessageAsJsonObject="true"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
                <SizeBasedTriggeringPolicy size="10 MB"/>
            </Policies>
            <DefaultRolloverStrategy max="30"/>
        </RollingFile>
    </Appenders>
    <Loggers>
        <Root level="INFO">
            <AppenderRef ref="JsonRollingQueryFile"/>
        </Root>
    </Loggers>
</Configuration>
```

Sample configuration: `sample_config/presto-query-logger-log4j2.xml`


### 4. Deploy configuration files

Put the above 2 configuration files(`event-listener.properties` and `presto-query-logger-log4j2.xml`) to Presto config directory(`<path-to-presto>/etc/`).

### 5. Check Presto directory

```
<path-to-presto>/
  |- etc/
     |- event-listener.properties
     |- presto-query-logger-log4j2.xml
  |- plugin/
     |- presto-query-logger/
        |- presto-query-logger.jar
        |- ... (dependency jars)
```

### 6. Run Presto

```
<path-to-presto>/bin/launcher run
```
