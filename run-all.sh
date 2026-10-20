#!/usr/bin/env bash
# ShopSphere — one-shot launcher for the demo (Linux / macOS)
set -e

echo "== 1. Load database =="
read -p "MySQL root password: " -s MYPW; echo
mysql -u root -p"$MYPW" < database/shopsphere.sql

echo "== 2. Patch db.properties =="
sed -i.bak "s/^db.password=.*/db.password=$MYPW/" src/main/resources/db.properties
sed -i.bak "s/^db.password=.*/db.password=$MYPW/" swing-admin/src/main/resources/db.properties 2>/dev/null || true

echo "== 3. Build web app =="
mvn clean package -DskipTests

echo "== 4. Deploy to Tomcat =="
if [ -z "$CATALINA_HOME" ]; then
  echo "ERROR: set CATALINA_HOME to your Tomcat 11 install directory"
  exit 1
fi
cp target/shopsphere.war "$CATALINA_HOME/webapps/"

echo "== 5. Build Swing admin =="
( cd swing-admin && mvn clean package -q )

echo "== 6. Build RMI & network modules =="
( cd rmi && mvn -q package )
( cd network && mvn -q package )

echo ""
echo "All builds complete."
echo "Start Tomcat:  \$CATALINA_HOME/bin/startup.sh"
echo "Open browser:  http://localhost:8080/shopsphere/"
echo "Swing admin:   java -jar swing-admin/target/swing-admin-jar-with-dependencies.jar"
echo ""
echo "RMI:    rmiregistry 1099 &  ; (cd rmi && mvn -q exec:java -Dexec.mainClass=com.shopsphere.rmi.server.InventoryServerMain) &  ; (cd rmi && mvn -q exec:java -Dexec.mainClass=com.shopsphere.rmi.client.InventoryClientMain)"
echo "Socket: (cd network && mvn -q exec:java -Dexec.mainClass=com.shopsphere.network.InventoryServer) &  ; (cd network && mvn -q exec:java -Dexec.mainClass=com.shopsphere.network.InventoryClient)"
