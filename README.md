JMXMiniProbe Proof-of-Concept
============
With the current version of PRTG, you can use the Mini Probe interface with your custom code to implement solutions to special scenarios that you might have in your network. Please note that there are major changes planned to the underlying API in PRTG. Therefore, any code you write now will likely need to be changed later, so it can be used for future versions of PRTG. Further news about changing interfaces will be provided here as soon as there are more detailed plans available.

Current Status: BETA

A PRTG MiniProbe for monitoring Java applications using JMX.

## Please note: we do not offer support for the JMXMiniProbe. It is a proof-of-concept that you can adapt to your needs.

Installation Instructions
=========================

Preparing your PRTG installation
--------------------------------

- set up your PRTG server to use HTTPS (other connection methods not allowed at the moment)
- allow MiniProbes to connect (**Setup** -> **System Administration** -> **Core&Probes** -> *Allow MiniProbes to connect*)
- add an appropriate allow ip filter (**Setup** -> **System Administration** -> **Core&Probes** -> *Allow IPs* , e.g. any)
- define a new accesskey for the MiniProbes (**Setup** -> **System Administration** -> **Core&Probes** -> *Access Keys*)
- make sure you can reach the PRTG web interface from the machine the mini probe should run on (e.g. wget https://YOUR_PRTG_SERVER)

Getting the mini probe
------------------

- You can download a pre-built WAR, which should work both in standalone-mode and as part of an application server, from
[the releases page](https://github.com/PaesslerAG/JMXMiniProbe/releases)
- A gradle and/or ant config is on the TODO list

Starting the probe in standalone-mode
-------------------------------------

Standalone mode causes the probe to run with it's own threadpool, so you don't need to have an application server installed.

For Windows and *nix using the Java Service Wrapper(JSW) Start it with:
    ```JMXMiniProbe {-c prtgjmx.properties}```

For platforms not supported by JSW use:
    ```runJMXMiniProbe {-c prtgjmx.properties}```

the ```prtgjmx.properties``` file should be writable by the UID running the probe and include the following parameters:

```
    key=prtg_probe_key
    host=ww.xx.yy.zz
```
You can copy the file ```prtgjmx-sample.properties``` as a starting point.

Now you can approve the new MiniProbe in the PRTG web UI and start adding new sensors to it.

Starting the probe in an application server
-------------------------------------------

* Create a config file, **writable by the application server**, containing the following parameters:
```
    key=prtg_probe_key
    host=10.0.2.160
    #port=8080
    #webprotocol=https
    #webprotocol=http
    #Debug Level
    #debug=1
    #workerthreads=10
    #baseinterval=30


* If your config file is called prtgjmx.properties and is in the root of the "webapp\JMXProbe" directory, no additional config is needed
  If you have a different config, add **com.paessler.jmxprobe.config=/path/to/config/file** to the server's Java options or classpath


Changelog
=========

2016-03-15
----------
-- Fixed: Enables use of JMX Attribute Variables like: "[attribute].[Variable]"

2015-09-28
----------
-- Added: Limit Channels to 25

2015-09-20
----------
-- Added: SNMP Profile

2015-08-25
----------
-- Fixed Bug: Used host as GID
-- Fixed Bug: Prefer Device IP address

2015-06-09
----------
- Added profile based sensor creating to JMX and Port
- Added JMX Profiles for Tomcat and Miniprobe (selfie)
- Added Port Profiles for Active Directory Critical
- Added Executable script generation using appassembler
- Added utility to generate JMX Profile fragment to list all MBeans in the JVM


2015-04-25
----------
- Added HTTP(s) Sensor
- Added Port List Sensor
- Fixed issues with pom
- Implemented MBean to monitor the Probe itself.


2015-04-25
----------
- Added Worker Threads as parameter
- Added SNMP Vector functionality
- Added SNMP OID functionality
- Added functional maven build file (pom.xml)
