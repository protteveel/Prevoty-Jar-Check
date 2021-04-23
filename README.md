# Prevoty-Jar-Check

Application to verify and validate the deployed Prevoty JAR files.

# Introduction

This document describes how to validate the Prevoty JAR files, using the tool PrevotyJarVersion.jar.

# Preparations

1. Get a copy of the tool PrevotyJarVersion.jar from the Prevoty Customer Success Portal here
2. Copy PrevotyJarVersion.jar onto the server running Prevoty
3. Get a copy of the Prevoty JVM arguments from the same server
4. Get the value for -javaagent
  1. g. /opt/Apache/Tomcat-8.5.5/Prevoty/prevoty-agent.jar

# Execution

1. Log on to the server running Prevoty
2. Open a terminal
3. Go into the directory where you have copied PrevotyJarVersion.jar
  1. g.:
cd /tmp
4. Execute PrevotyJarVersion.jar with the value of -javaagent, from the aforementioned step
  1. g.:
java -jar ./PrevotyJarVersion.jar /opt/Apache/Tomcat-8.5.5/Prevoty/prevoty-agent.jar
5. If all is well, the tool reports the filename, component (agent or rasp), type (with version or version-less), and the version
  1. g.:
Filename: prevoty-agent.jar

Component: agent

Type: version-less

Version: 3.9.4

1. If there is something wrong with the JAR file, the tool reports filename, component (agent or rasp), and the JAR file needs to be replaced
  1. g.:
Filename: prevoty-agent.jar

Component: agent

ERROR: JAR has been tempered with; both Prevoty JAR files need to be replaced

RackMultipart20210423-4-1jvx9n5.docx September 23, 2018 Page 1 of 1
