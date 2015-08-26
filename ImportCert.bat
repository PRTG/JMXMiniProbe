SET PASSPHRASE=changeit
SET CERT=W:\Paessler\Certificates\prtg.crt
"%JAVA_HOME%\bin\keytool" -import -alias PRTG -file %CERT% -keystore "%JAVA_HOME%\jre\lib\security\cacerts" -storepass %PASSPHRASE%
