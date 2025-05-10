# Blood Donation Management System

## 1. Clone Project

```bash
git clone [REPO_URL]
cd pdm-final-project-main
```

## 2. Backend Setup (Java Spring Boot)

### Prerequisites
- Java 8 trở lên
- Maven
- SQL Server (hoặc tương thích)
- Thư viện JDBC (đã có sẵn trong `lib/`)

### Cài đặt dependencies
```bash
mvn clean install
```

### Cấu hình Database
- Mở file: `src/main/resources/application.properties` **hoặc** `src/main/resources/dbconfig.json`
- Chỉnh sửa các thông tin kết nối database cho phù hợp với máy của bạn (username, password, url, v.v.)

**Ví dụ (application.properties):**
```
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=BloodDonation
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### Tạo Database & Dữ liệu mẫu
- Chạy file SQL sau trên SQL Server:
  - `src/main/resources/database.sql` (tạo bảng)
  - `src/main/resources/sample_data.sql` (dữ liệu mẫu)

### Chạy backend
```bash
mvn spring-boot:run
```

## 3. Frontend Setup (Next.js/React)

### Prerequisites
- Node.js (khuyến nghị >= 18)
- npm hoặc yarn

### Cài đặt dependencies
```bash
cd src/frontend
npm install
```

### Chạy frontend
```bash
npm run dev
```
Ứng dụng sẽ chạy tại [http://localhost:3000](http://localhost:3000)

## 4. Tổng hợp các lệnh cần thiết

```bash
# Clone project
git clone [REPO_URL]
cd pdm-final-project-main

# Backend
mvn clean install
mvn spring-boot:run

# Frontend
cd src/frontend
npm install
npm run dev
```

## 5. Chỉnh sửa thông tin database

- File cấu hình: `src/main/resources/application.properties` **hoặc** `src/main/resources/dbconfig.json`
- Đảm bảo sửa các trường liên quan đến kết nối SQL Server (host, port, username, password, database name) cho phù hợp với môi trường của bạn.

## 6. Thư viện JDBC

- Đã có sẵn trong thư mục `lib/`. Nếu cần cập nhật, tải file `.jar` mới và thay thế tại đây.

---

**Liên hệ:** Nếu gặp lỗi kết nối database, kiểm tra lại thông tin trong file cấu hình và đảm bảo SQL Server đang chạy.
