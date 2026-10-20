# ShopSphere Test Cases

| # | Test | Steps | Expected |
|---|------|-------|----------|
| 1 | Valid registration | `/register` → fill → submit | Redirect to home, session created |
| 2 | Duplicate email | Register with existing email | "Email already registered" error |
| 3 | Valid login | Login with john@example.com / customer123 | Redirect to home |
| 4 | Invalid login | Wrong password | "Invalid email or password" |
| 5 | Product search | `/products?q=iPhone` | 1 matching iPhone |
| 6 | Category filter | `/products?category=1` | Only Laptops |
| 7 | Sort by price | `/products?sort=price_asc` | Cheapest first |
| 8 | Add to cart | Click Add on product | Cart count badge updates |
| 9 | Update cart qty | Change to 3 → Update | Subtotal recalculated |
| 10 | Remove from cart | Click trash icon | Item removed |
| 11 | Apply coupon WELCOME10 | Type WELCOME10 → Apply | 10% discount shown |
| 12 | Place order (mock) | Checkout → Place | Order # visible in /orders |
| 13 | Stock decrement | Check DB after order | Stock reduced |
| 14 | Place order out of stock | Manually set stock=0 | Error message |
| 15 | Profile address add | /profile → Add address | Shows in list |
| 16 | Write review | Product page → rating + submit | Review visible |
| 17 | Unauthenticated /cart | Logout, go to /cart | Redirect to /login?next=/cart |
| 18 | Customer opens /admin | Open /admin/dashboard as customer | Redirect to /admin/login |
| 19 | Admin login | admin@shopsphere.com / admin123 | Dashboard opens |
| 20 | Admin creates product | /admin/products?action=new | Product appears in storefront |
| 21 | Admin updates order status | Order detail → change status | Customer sees status in /orders |
| 22 | Admin deletes product | Click Del → confirm | Product removed |
| 23 | Swing admin login | Run jar → login as admin | Dashboard opens |
| 24 | Swing product edit | Select row → Edit → Save | DB updated |
| 25 | Swing order status | Select order → Update Status | DB updated |
| 26 | Socket server GET | `GET 1` | Product data returned |
| 27 | Socket server STOCK | `STOCK 3` | Returns 30 (initial stock) |
| 28 | Socket QUIT | `QUIT` | `BYE` returned |
| 29 | RMI getProduct | Run client | RemoteProduct printed |
| 30 | RMI updateStock | Run client | Stock changes by delta |
