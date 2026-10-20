@echo off
REM ShopSphere — one-shot launcher for the demo (Windows)
setlocal

echo == 1. Load database ==
set /p MYPW=MySQL root password: 
mysql -u root -p%MYPW% < database\shopsphere.sql

echo == 2. Patch db.properties ==
powershell -Command "(Get-Content src\main\resources\db.properties) -replace '^db.password=.*','db.password=%MYPW%' | Set-Content src\main\resources\db.properties"
powershell -Command "(Get-Content swing-admin\src\main\resources\db.properties) -replace '^db.password=.*','db.password=%MYPW%' | Set-Content swing-admin\src\main\resources\db.properties"

echo == 3. Build web app ==
call mvn clean package -DskipTests

echo == 4. Deploy to Tomcat ==
if "%CATALINA_HOME%"=="" (
  echo ERROR: set CATALINA_HOME to your Tomcat 11 install directory
  exit /b 1
)
copy /Y target\shopsphere.war "%CATALINA_HOME%\webapps\"

echo == 5. Build Swing admin ==
pushd swing-admin
call mvn clean package -q
popd

echo == 6. Build RMI & network modules ==
pushd rmi
call mvn -q package
popd
pushd network
call mvn -q package
popd

echo.
echo All builds complete.
echo Start Tomcat:  "%CATALINA_HOME%\bin\startup.bat"
echo Open browser:  http://localhost:8080/shopsphere/
echo Swing admin:   java -jar swing-admin\target\swing-admin-jar-with-dependencies.jar
echo.
echo RMI:    rmiregistry 1099  then run rmi server & client
echo Socket: run network server & client
endlocal
