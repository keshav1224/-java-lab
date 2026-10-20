# RTU Viva Mapping — ShopSphere

For every RTU Advanced Java topic, this document tells you **where it appears in the project** and **what to show during the viva**.

---

## 1. Swing
**Where:** `swing-admin/src/main/java/com/shopsphere/swing/view/*.java`  
**What to show:** Run the desktop app → LoginFrame → AdminDashboard → click "Manage Products" → ProductFrame opens with JTable → select a row → Edit → change price → Save → refresh (data is persisted in MySQL).

Components used:
- `JFrame`, `JPanel`, `JLabel`, `JTextField`, `JPasswordField`, `JButton`, `JCheckBox`, `JComboBox`, `JTable`, `JScrollPane`, `JOptionPane`, `GridBagLayout`, `GridLayout`, `BorderLayout`.

## 2. Swing MVC
- **Model:** `com.shopsphere.swing.model.AdminUser`, plus the DB rows loaded into `DefaultTableModel`
- **View:** `LoginFrame`, `AdminDashboard`, `ProductFrame`, `CategoryFrame`, `CustomerFrame`, `OrderFrame`, `InventoryFrame`
- **Controller:** Action listeners in each frame (e.g. `loginBtn.addActionListener`)
- **DAO-equivalent:** PreparedStatement calls inside each frame

## 3. MVC (Web)
- **Model:** `com.shopsphere.model.*` (12 POJOs)
- **View:** `WEB-INF/views/*.jsp` + `WEB-INF/tags/*.tag`
- **Controller:** `com.shopsphere.controller.*Servlet`
- **Service:** `com.shopsphere.service.*`
- **DAO:** `com.shopsphere.dao.*`

## 4. Applets
**Not used** — noted as historical/conceptual only in the guide. Mention this explicitly.

## 5. JDBC / java.sql
- **Where:** `com.shopsphere.dao` (10 DAO classes) + Swing frames
- **What to show:** Open `ProductDAO.search()` — show `PreparedStatement`, `ResultSet`, try-with-resources.
- **Connection pool:** HikariCP (`com.shopsphere.util.DBConnection`)

## 6. java.net Socket / ServerSocket
- **Where:** `network/src/main/java/com/shopsphere/network/`
- **Run:**
  ```bash
  cd network && mvn -q exec:java -Dexec.mainClass=com.shopsphere.network.InventoryServer
  # new terminal
  cd network && mvn -q exec:java -Dexec.mainClass=com.shopsphere.network.InventoryClient
```

- **Commands:** `GET 1`, `STOCK 3`, `QUIT`
- **What to show:** Server logs `[Socket] InventoryServer listening on 5555`; client sends a command; server replies with product data.

## 7. RMI / RMI Registry / Naming / JNDI

- **Where:** `rmi/src/main/java/com/shopsphere/rmi/`
- **Run:**

```
rmiregistry 1099
# new terminal
cd rmi && mvn -q exec:java -Dexec.mainClass=com.shopsphere.rmi.server.InventoryServerMain
# new terminal
cd rmi && mvn -q exec:java -Dexec.mainClass=com.shopsphere.rmi.client.InventoryClientMain
```
- **What to show:** Remote call to `getStock(1)` returns 12. Note `Naming.rebind` / `Naming.lookup`.

## 8. Serialization

- `Product implements Serializable`, `RemoteProduct implements Serializable`.
- **What to show:** In the Socket module, if we serialized a `Product` object (see `network/` package — can extend to object streams), or via RMI which serializes `RemoteProduct` across the wire automatically.

## 9. Internationalization

- **Where:** JSPs use `<fmt:formatNumber>` and `<fmt:formatDate>`.
- **What to show:** In `products.jsp` — price rendering uses `fmt:formatNumber` with locale from `Accept-Language`. Change browser language to Hindi/Japanese and refresh — currency formatting adapts.

## 10. J2EE / N-tier Architecture

- **Presentation layer:** JSPs
- **Controller layer:** Servlets
- **Service layer:** Service classes
- **DAO layer:** DAO classes
- **Database:** MySQL
- **Deployed on:** Apache Tomcat 11 (web container)

## 11. HTTP

- Every `@WebServlet` method is `doGet` (HTTP GET) or `doPost` (HTTP POST).
- **What to show:** Browser → page → DevTools → Network tab → see `GET /shopsphere/products`.

## 12. Web Container

- Apache Tomcat 11. `pom.xml` has `<packaging>war</packaging>` and `jakarta.servlet-api` with `provided` scope.
- **What to show:** Drop `shopsphere.war` into `$TOMCAT/webapps`, start Tomcat, WAR is auto-deployed.

## 13. Servlet lifecycle

- **Loading:** Tomcat loads `LoginServlet.class` on first request.
- **Instantiation:** Constructor called.
- **init():** inherited from `HttpServlet` (or overridable — not needed here).
- **service():** dispatches to `doGet`/`doPost`.
- **destroy():** called on Tomcat shutdown.
- **What to show:** Add a `System.out.println` in `doGet` and observe the sequence in logs.

## 14. ServletConfig / ServletContext

- `AppListener.contextInitialized` sets `context.setAttribute("appName", "ShopSphere")`.
- **What to show:** Add `${applicationScope.appName}` to any JSP — displays "ShopSphere".

## 15. Request / Response

- `request.getParameter("q")` for search
- `response.sendRedirect` after POST
- **What to show:** `ProductServlet.doGet` handles both `q` and `category` query params.

## 16. Session Handling

- **Where:** `SessionUtil` + `session.setAttribute("user", u)` in `LoginServlet`
- **What to show:** Login → DevTools → Cookies → `JSESSIONID` set. Refresh → still logged in.

## 17. Event Handling / Listeners

- **Where:** `AppListener.java` implements both `ServletContextListener` and `HttpSessionListener`.
- **What to show:** Watch Tomcat log for `[ShopSphere] Session created: ...` after each fresh login.

## 18. Filters

- **Where:** `AuthenticationFilter`, `AdminFilter` (both `@WebFilter`).
- **What to show:** Visit `/cart` while logged out → redirected to `/login?next=/cart`.

## 19. JSP Architecture / Lifecycle

- JSPs are compiled to Servlets by Tomcat on first hit.
- **What to show:** Tomcat → `work/Catalina/localhost/shopsphere/org/apache/jsp/WEB_002dINF/views/…` — show the generated `.java` file.

## 20. EL

- **Where:** Every JSP — `${product.name}`, `${sessionScope.user.email}`, `${not empty cartCount}`.
- **What to show:** Open `product-details.jsp` — highlight EL expressions.

## 21. JSTL Core

- `<c:forEach>`, `<c:if>`, `<c:choose>`, `<c:when>`, `<c:otherwise>`, `<c:set>` used throughout.
- **What to show:** `products.jsp` uses `<c:forEach>` to loop products and `<c:choose>` for price/discount logic.

## 22. JSTL Functions

- **Where:** `admin/_sidebar.jsp` uses `${fn:contains(...)}` to highlight the active sidebar item.

## 23. Tag Extensions / Tag Files

- **Where:** `WEB-INF/tags/productCard.tag`, `WEB-INF/tags/rating.tag`.
- **What to show:** In `products.jsp`, `<t:productCard product="${p}"/>` reuses the card layout 12 times. In `product-details.jsp`, `<t:rating value="${avgRating}"/>` shows 5-star rating.

## 24. JSP Fragments

- **Where:** `fragments/header.jsp`, `navbar.jsp`, `footer.jsp`, `alerts.jsp`.
- **What to show:** Every view starts with `<jsp:include page="fragments/header.jsp"/>` — one place to edit the `<head>`.

## 25. Serialization in network/RMI

- See RMI module — `RemoteProduct` is serialized when the client calls `getProduct(1)`.

---

## Quick demo script (5 minutes)

1. `mvn clean package -DskipTests` → WAR built
2. Copy to Tomcat → start → `http://localhost:8080/shopsphere/`
3. Register + login as customer
4. Browse → search "iPhone" → add to cart
5. Apply coupon `WELCOME10`
6. Checkout with Stripe test card `4242 4242 4242 4242`
7. View order history
8. Logout → login as `admin@shopsphere.com` / `admin123`
9. Admin dashboard → product CRUD → update order status
10. Terminal: Swing admin → product CRUD
11. Terminal: `rmiregistry 1099` + RMI server + RMI client (see remote stock)
12. Terminal: Socket server + client → `GET 1` → product info returned
13. Show mapping doc to examiner

---

## Note on internationalization

The `<fmt:formatNumber>` tags use the default locale from the browser. To demonstrate explicitly, you can:

- Set browser language to `hi-IN` → prices render with Indian formatting
- Or add a `?lang=hi` parameter and use `<fmt:setLocale>` inside JSP

</BDS:create_file>
<BDS:create_file fileName="ShopSphere/docs/ER_DIAGRAM.md">

```markdown
# ShopSphere ER Diagram

+---------+
|  USERS  |
+---------+
| user_id |
| name    |
| email   |
| password|
| role    |
+----+----+
|
+-------------+----------------+------------------+
|             |                |                  |
+----v----+   +----v----+      +----v-----+      +-----v-----+
|ADDRESSES|   |  CART   |      | WISHLIST |      |  ORDERS   |
+---------+   +---------+      +----------+      +-----------+
| address |   | product |      | product  |      | address_id|
| city    |   | qty     |      | created  |      | total     |
| state   |   +----+----+      +----+-----+      | discount  |
| pincode |        |                |            | status    |
+---------+        |                |            +-----+-----+
|                |                  |
+-------v--------+       |           +------v------+
|   PRODUCTS     |<------+           | ORDER_ITEMS |
+----------------+                   +-------------+
| product_id     |                   | order_id    |
| category_id    |                   | product_id  |
| name, brand    |                   | qty, price  |
| price, discount|                   +------+------+
| stock          |                          |
+-------+--------+                          |
|                                   |
+-------v--------+                +---------v---------+
|  CATEGORIES    |                |     PAYMENTS      |
+----------------+                +-------------------+
| transaction_ref   |
| amount            |
| status            |
+-------------------+

+------------------+
|    COUPONS       |
+------------------+
| code, type       |
| value, min_order |
| expiry           |
+--------+---------+
|
+--------v---------+
|  COUPON_USAGE    |
+------------------+
| user_id          |
| order_id         |
+------------------+

```

