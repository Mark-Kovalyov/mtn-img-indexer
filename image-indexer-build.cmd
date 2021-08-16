set JAVA_HOME=c:\jdk-11.0.3
set CLASSPATH=%JAVA_HOME%\lib
set GRADLE_HOME=c:\gradle\5.6.2
set MAVEN_HOME=c:\maven\3.6.0
set Path=%JAVA_HOME%\bin;%GRADLE_HOME%\bin;%MAVEN_HOME%\bin;%SBT_HOME%\bin

mkdir bin
call mvn package -P ImageIndexer
copy target\mtn-img-indexer-1.0-SNAPSHOT.jar .\bin
copy src\main\resources\mtn-img-indexer.cmd .\bin
copy src\main\resources\mtn-img-indexer.sh  .\bin
