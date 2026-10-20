# ShopSphere — RTU Advanced Java E-Commerce Project

A complete Java E-Commerce system built to cover the RTU Advanced Java syllabus:
**Swing, MVC, JDBC, java.net, Client-Server, RMI, Serialization, J2EE, HTTP, Servlets, Sessions, Filters, Listeners, JSP, EL, JSTL, Tag Files, N-tier architecture** — all demonstrated inside one coherent application.

---

## Modules

| Module | Folder | RTU Topics covered |
|---|---|---|
| Web storefront | `src/main/webapp` + `src/main/java/com/shopsphere/controller` | Servlet, JSP, EL, JSTL, Sessions, Filters, Listeners, MVC, N-tier |
| Desktop admin | `swing-admin/` | Swing, Swing MVC, JDBC |
| RMI inventory | `rmi/` | RMI, RMI Registry, Naming, Serialization |
| Socket inventory | `network/` | java.net, Socket/ServerSocket, Client-Server, Serialization |

---

## Prerequisites

- **JDK 17+**
- **Maven 3.9+**
- **MySQL 8.0+** (Workbench optional but recommended)
- **Apache Tomcat 11**

---

## Setup

### 1. Database
```bash
mysql -u root -p < database/shopsphere.sql
```

Or in Workbench: **File → Open SQL Script → run**.

### 2. Configure DB credentials

Edit `src/main/resources/db.properties`:

```
db.username=root
db.password=YOUR_PASSWORD
```

### 3. (Optional) Stripe

Edit `src/main/resources/stripe.properties`:

```
stripe.secret.key=sk_test_...
stripe.publishable.key=pk_test_...
```

If left blank, the app runs in **MOCK payment mode** for the demo.

### 4. Build & deploy the web app

```
mvn clean package -DskipTests
cp target/shopsphere.war $TOMCAT/webapps/
$TOMCAT/bin/startup.sh
```

Open **http://localhost:8080/shopsphere/**

### 5. Run the Swing admin app

```
cd swing-admin
mvn clean package
java -jar target/swing-admin-jar-with-dependencies.jar
```

### 6. Run the RMI module

```
# Terminal 1
rmiregistry 1099
# Terminal 2
cd rmi
mvn -q exec:java -Dexec.mainClass="com.shopsphere.rmi.server.InventoryServerMain"
# Terminal 3
mvn -q exec:java -Dexec.mainClass="com.shopsphere.rmi.client.InventoryClientMain"
```

### 7. Run the Socket module

```
# Terminal 1
cd network
mvn -q exec:java -Dexec.mainClass="com.shopsphere.network.InventoryServer"
# Terminal 2
mvn -q exec:java -Dexec.mainClass="com.shopsphere.network.InventoryClient"
```

---

## Default credentials

| Role ↕▾ | Email ↕▾ | Password ↕▾ |
|---|---|---|
| −Admin | admin@shopsphere.com | admin123 |
| −Customer | john@example.com | customer123 |
⚙

---

## Features

### Storefront

- Product catalog with **search**, **category filter**, **sort**
- **Product reviews** and star ratings
- Shopping cart with quantity controls
- **Wishlist**
- **Coupons** (`WELCOME10`, `ELEC500`)
- Checkout with **Stripe (test mode)** or **mock fallback**
- Order history
- Profile with saved addresses
- Session-based authentication

### Admin (web)

- Dashboard with analytics: revenue, counts, low stock, recent orders
- Full product / category CRUD
- Order management with status updates
- Customer list

### Desktop (Swing)

- Login with BCrypt verification
- Product CRUD, Category, Customer, Order, Inventory tables

### Distributed (RMI + Socket)

- Remote inventory lookups
- Serialized `Product` transfers
- Line-based Socket protocol (`GET`, `STOCK`, `QUIT`)

---

## Security

- **BCrypt** password hashing
- **PreparedStatement** everywhere (no SQL injection)
- **HttpOnly** session cookies
- `AuthenticationFilter` blocks protected customer pages
- `AdminFilter` guards `/admin/*`
- Server-side total calculation on checkout
- No plaintext passwords in cookies

---

## RTU Syllabus → Implementation Map

| RTU Topic ↕▾ | Where ↕▾ |
|---|---|
| −Swing | `swing-admin/` (8 frames) |
| −MVC | Controller → Service → DAO in both web + Swing |
| −JDBC | `dao/` package (10 DAO classes) |
| −java.net Socket | `network/InventoryServer.java` + client |
| −RMI | `rmi/` (interface + impl + registry) |
| −Serialization | `Product`, `RemoteProduct` implement `Serializable` |
| −J2EE | `pom.xml` `<packaging>war</packaging>` on Tomcat 11 |
| −N-tier | Presentation → Controller → Service → DAO → DB |
| −HTTP | Servlet `doGet` / `doPost` |
| −Web container | Apache Tomcat 11 |
| −Servlet lifecycle | All controllers extend `HttpServlet` |
| −ServletContext | `AppListener.contextInitialized` sets `appName` |
| −Sessions | `SessionUtil.currentUser` + `session.setAttribute("user")` |
| −Cookies | Session cookie (`JSESSIONID`) config in `web.xml` |
| −Listeners | `AppListener` (context + session) |
| −Filters | `AuthenticationFilter`, `AdminFilter` |
| −JSP | 20+ JSPs in `WEB-INF/views/` |
| −EL | `${product.name}`, `${sessionScope.user.name}` |
| −JSTL | `<c:forEach>`, `<c:if>`, `<c:choose>`, `<fmt:formatNumber>` |
| −Tag files | `WEB-INF/tags/productCard.tag`, `rating.tag` |
| −JSP fragments | `fragments/header.jsp`, `navbar.jsp`, `footer.jsp` |
| −Internationalization | Prices via `<fmt:formatNumber>` with locale context |
⚙

---

## Viva demo checklist

1. Show `mvn clean package` → WAR
2. Deploy to Tomcat 11, open storefront
3. Register → Login → Browse → Search → Filter → Sort
4. Add to cart → apply coupon `WELCOME10` → checkout with Stripe test card `4242 4242 4242 4242` (or mock)
5. View order history
6. Admin login → dashboard → product/category CRUD → update order status
7. Run Swing admin — BCrypt login, product CRUD
8. Terminal: `rmiregistry 1099` → RMI server → RMI client
9. Terminal: Socket server → Socket client → `GET 1`
10. Explain each RTU topic and where it appears

---

## Project structure

```
ShopSphere/
├── pom.xml
├── database/shopsphere.sql
├── src/main/
│   ├── java/com/shopsphere/
│   │   ├── controller/         (14 servlets + 4 admin)
│   │   ├── service/            (6 services)
│   │   ├── dao/                (10 DAOs)
│   │   ├── model/              (12 POJOs)
│   │   ├── filter/             (AuthenticationFilter, AdminFilter)
│   │   ├── listener/           (AppListener)
│   │   └── util/               (DB, Password, Session, Json)
│   ├── resources/
│   │   ├── db.properties
│   │   └── stripe.properties
│   └── webapp/
│       ├── index.jsp
│       ├── css/ js/
│       └── WEB-INF/
│           ├── web.xml
│           ├── tags/
│           └── views/ (all JSPs)
├── swing-admin/                (desktop module)
├── rmi/                        (RMI module)
└── network/                    (Socket module)
```

---

## Test cases

| Test ↕▾ | Expected ↕▾ |
|---|---|
| −Register new user | Redirect to login |
| −Login as customer | Session created |
| −Browse `/products` | 12 electronics |
| −Filter "Laptops" | 2 results |
| −Search "iPhone" | 1 result |
| −Add to cart | Count increments |
| −Apply `WELCOME10` | 10% off |
| −Place order | Order created, stock reduced |
| −Customer opens `/admin/dashboard` | Redirected to admin login |
| −Admin login → product CRUD | Works |
| −Socket `GET 1` | Product returned |
| −RMI `getStock(1)` | Returns 12 |
⚙

---

## License

Educational project — RTU Advanced Java syllabus.

