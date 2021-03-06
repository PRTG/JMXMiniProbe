#Copyright (c) 2014-2015, Paessler AG <support@paessler.com>
#All rights reserved.
#Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
# following conditions are met:
#1. Redistributions of source code must retain the above copyright notice, this list of conditions
# and the following disclaimer.
#2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
# and the following disclaimer in the documentation and/or other materials provided with the distribution.
#3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
# or promote products derived from this software without specific prior written permission.

#THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
# INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
# A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
# INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
# PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
# HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
# OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
# EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

#set WEBINFLIB=web/WEB-INF/lib
#set CLASS_LIBS=$WEBINFLIB/commons-cli-1.2.jar;$WEBINFLIB/commons-codec-1.6.jar;$WEBINFLIB/commons-logging-1.1.1.jar;$WEBINFLIB/gson-2.2.2.jar;$WEBINFLIB/httpclient-4.2.5.jar;$WEBINFLIB/httpclient-cache-4.2.5.jar;$WEBINFLIB/httpcore-4.2.4.jar;$WEBINFLIB/httpmime-4.2.5.jar;lib/serverlet-api-2.5.jar
#set OTHERLIBS=lib/jna-3.5.1.jar;lib/platform-3.5.1.jar;lib/shortpasta-icmp2.jar;lib/snmp4j-2.3.1.jar
#java -cp ,/;./bin;$CLASS_LIBS;$OTHERLIBS -Dcom.sun.management.config.file=management.properties -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false com.paessler.prtg.jmx.DaemonMain -c data/prtgjmx.properties

java -cp ./:./bin:WEB-INF/lib/JMXMiniProbe-1.0.jar:\
WEB-INF/lib/commons-cli-1.2.jar:WEB-INF/lib/commons-codec-1.6.jar:WEB-INF/lib/commons-logging-1.1.1.jar:\
WEB-INF/lib/gson-2.2.2.jar:WEB-INF/lib/httpclient-4.2.5.jar:WEB-INF/lib/httpclient-cache-4.2.5.jar:\
WEB-INF/lib/httpcore-4.2.4.jar:WEB-INF/lib/httpmime-4.2.5.jar:lib/serverlet-api-2.5.jar:lib/sblim-cim-client2-2.2.5.jar:\
WEB-INF/lib/jna-3.5.1.jar:WEB-INF/lib/platform-3.5.1.jar:WEB-INF/lib/icmp4j-16.0.1017.jar:WEB-INF/lib/snmp4j-2.3.1.jar \
com.paessler.prtg.jmx.DaemonMain -c prtgjmx.properties

# This would enable remote management on the local vm, there is a bug that may make it throw an error.
#-Dcom.sun.management.config.file=management.properties -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false \
