# ShopSphere Use Case Diagram

## Customer
- Register / Login / Logout
- Browse products
- Search / filter / sort
- View product details
- Add / remove from cart
- Apply coupon
- Checkout (Stripe / COD)
- View order history
- Manage wishlist
- Write review
- Manage addresses

## Admin
- Admin login
- Dashboard (analytics)
- Product CRUD
- Category CRUD
- View orders, update status
- View customers
- View low-stock inventory (Swing)

## System actors
- Payment gateway (Stripe)
- MySQL database
- RMI server (inventory lookup)
- Socket server (inventory lookup)

```

+---------+
| Customer|<------+
+----+----+       |
|            |
+---------+---------+  |
|         |         |  |
browse     cart     checkout
|         |         |
v         v         v
+-------+ +-------+ +-------+
|Product| |Cart   | |Order  |
+-------+ +-------+ +-------+
|         |
v         v
+--------+ +--------+
|Coupon  | |Payment |
+--------+ +--------+

+-------+
| Admin |<---+
+---+---+    |
|        |
+-------+------+ |
|       |      | |
product  order  customer
CRUD   status    list

```

(Full use-case diagram for the report can be drawn in draw.io / StarUML from this structure.)
