# 🎓 College Inventory Management System

A full-stack inventory management system for colleges, built using **Spring Boot**, **MySQL**, and **HTML + Bootstrap + JavaScript** frontend.

---

## 🛠 Technologies Used

- Backend: Spring Boot (3.x)
- Frontend: HTML, Bootstrap 5, Vanilla JavaScript
- Database: MySQL
- Security: Spring Security with Basic Auth
- Reporting: Apache POI (Excel Import), iText/PDFBox (PDF Export)

---

## ✅ Features

### 🔐 Authentication
- Admin / Sub-admin login
- Role-based access control

### 📦 Inventory (Items)
- Add, Edit, Delete Items
- Increase / Decrease quantity
- Prevent delete if item is issued
- Filter by category
- Highlight low-stock items

### 📁 Import / Export
- Import items via `.xlsx` file
- Export full inventory to PDF
- Export by category, location, or low-stock to PDF

### 🔄 Issue / Return System
- Issue items to users
- Mark item as returned
- Prevent over-issue if quantity is insufficient

### 📜 Movement History
- Track all item issues & returns
- View detailed issue/return log
- Filter movement by item

📂 Folder Structure
For Backend
src/
 └── main/
     ├── java/com/collage/inventory/
     │   ├── controller/
     │   ├── service/
     │   ├── entity/
     │   ├── repository/
     │   └── security/
     └── resources/
         ├── application.properties
         └── templates/

For FrontEnd         
/
 ├── index.html
 ├── items.html
 ├── issues.html
 └── js/
     ├── items.js
     └── issues.js



🧑‍💼 Login Details

| Role          | Username                         | Password   |
| ------------- | -------------------------------- | ---------- |
| **Admin**     | `admin`                          | `admin123` |
| **Sub-Admin** | `subadmin1` *(created by admin)* | `pass123`  |

📥 API Endpoints
🧑 User Management (/api/users)
| Endpoint           | Method | Role  | Description      |
| ------------------ | ------ | ----- | ---------------- |
| `/create-subadmin` | POST   | ADMIN | Create sub-admin |
| `/all`             | GET    | ADMIN | Get all users    |

📦 Item Management (/api/items)
| Endpoint               | Method     | Role              | Description           |
| ---------------------- | ---------- | ----------------- | --------------------- |
| `/`                    | GET/POST   | ADMIN, SUB\_ADMIN | Get all / Add item    |
| `/{id}`                | PUT/DELETE | ADMIN, SUB\_ADMIN | Update or delete item |
| `/increase/{id}`       | POST       | ADMIN, SUB\_ADMIN | Increase quantity     |
| `/decrease/{id}`       | POST       | ADMIN, SUB\_ADMIN | Decrease quantity     |
| `/import`              | POST       | ADMIN, SUB\_ADMIN | Import Excel          |
| `/category/{category}` | GET        | ADMIN, SUB\_ADMIN | Filter by category    |
| `/report/low-stock`    | GET        | ADMIN, SUB\_ADMIN | Get low-stock items   |

📄 PDF Reports (/api/items/export/pdf)
| Endpoint                 | Method | Role  | Description              |
| ------------------------ | ------ | ----- | ------------------------ |
| `/`                      | GET    | ADMIN | Full inventory PDF       |
| `/category/{category}`   | GET    | ADMIN | PDF filtered by category |
| `/location/{location}`   | GET    | ADMIN | PDF filtered by location |
| `/low-stock?threshold=5` | GET    | ADMIN | PDF of low-stock items   |

🔄 Issue/Return (/api/issues)
| Endpoint       | Method | Role              | Description             |
| -------------- | ------ | ----------------- | ----------------------- |
| `/issue`       | POST   | ADMIN, SUB\_ADMIN | Issue an item           |
| `/return/{id}` | POST   | ADMIN, SUB\_ADMIN | Return an item          |
| `/`            | GET    | ADMIN, SUB\_ADMIN | List all issued records |


---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/college-inventory-system.git
cd college-inventory-system

✅ 2. Import MySQL DB
CREATE DATABASE Nrt;

✅ 3. Configure application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/Nrt
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

✅ 4. Build and run the backend
./mvnw spring-boot:run

✅ 5. Open Frontend
Open index.html in your browser (served via local static server or direct file).

📎 Sample Excel Format
Your Excel file should contain the following columns:
| Name    | Category    | Location | Condition | Quantity |
| ------- | ----------- | -------- | --------- | -------- |
| Monitor | Electronics | Lab 2    | Good      | 10       |


