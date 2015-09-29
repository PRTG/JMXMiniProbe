REM Copyright (c) 2014-2015, Paessler AG <support@paessler.com>
REM All rights reserved.
REM Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
REM  following conditions are met:
REM 1. Redistributions of source code must retain the above copyright notice, this list of conditions
REM  and the following disclaimer.
REM 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
REM  and the following disclaimer in the documentation and/or other materials provided with the distribution.
REM 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
REM  or promote products derived from this software without specific prior written permission.

REM THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
REM  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
REM  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
REM  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
REM  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
REM  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
REM  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
REM  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

SET WEBINFLIB=web\WEB-INF\lib
SET CLASS_LIBS=%WEBINFLIB%\commons-cli-1.2.jar;%WEBINFLIB%\commons-codec-1.6.jar;%WEBINFLIB%\commons-logging-1.1.1.jar;%WEBINFLIB%\gson-2.2.2.jar;%WEBINFLIB%\httpclient-4.2.5.jar;%WEBINFLIB%\httpclient-cache-4.2.5.jar;%WEBINFLIB%\httpcore-4.2.4.jar;%WEBINFLIB%\httpmime-4.2.5.jar;%WEBINFLIB%\javax.serverlet-api-3.0.1.jar
SET OTHERLIBS=%WEBINFLIB%\jna-3.5.1.jar;%WEBINFLIB%\platform-3.5.1.jar;%WEBINFLIB%\icmp4j.jar;%WEBINFLIB%\snmp4j-2.3.1.jar;%WEBINFLIB%\sblim-cim-client2-2.2.5.jar
REM java.exe -cp ,\;.\bin;%CLASS_LIBS%;%OTHERLIBS% -Dcom.sun.management.config.file=data\management.properties -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false com.paessler.prtg.jmx.DaemonMain -c data/prtgjmx.properties
java.exe -jar target\JMXMiniProbe.jar -cp .\;%WEBINFLIB% -Dcom.sun.management.config.file=data\management.properties -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false com.paessler.prtg.jmx.DaemonMain -c data/prtgjmx.properties
