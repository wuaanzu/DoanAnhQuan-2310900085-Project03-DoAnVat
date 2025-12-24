# Hướng dẫn Migration: Xóa bảng Customer

## Tổng quan
Migration này sẽ xóa bảng `customer` và sử dụng bảng `staff` cho cả nhân viên và khách hàng thông qua trường `role`.

## Bước 1: Chạy script SQL

### Cách 1: Sử dụng phpMyAdmin
1. Mở phpMyAdmin: `http://localhost/phpmyadmin`
2. Chọn database `shop_do_an_vat`
3. Click tab "SQL"
4. Copy toàn bộ nội dung file `migrate_remove_customer.sql`
5. Paste vào SQL editor và click "Go"

### Cách 2: Sử dụng MySQL Command Line
```bash
mysql -u root -p -P 3307 shop_do_an_vat < src/main/resources/db/migration/migrate_remove_customer.sql
```

## Bước 2: Kiểm tra kết quả

Sau khi chạy script, kiểm tra:

```sql
-- Kiểm tra bảng staff có email và address
DESCRIBE staff;

-- Kiểm tra dữ liệu staff (phải có cả khách hàng)
SELECT * FROM staff;

-- Kiểm tra bảng orders có staff_id
DESCRIBE orders;

-- Kiểm tra orders đã link đúng với staff
SELECT o.*, s.staff_name, s.role 
FROM orders o 
JOIN staff s ON o.staff_id = s.staff_id;

-- Kiểm tra bảng customer đã bị xóa (phải báo lỗi)
SELECT * FROM customer;  -- Error: Table doesn't exist
```

## Bước 3: Restart ứng dụng Spring Boot

Sau khi migrate database, restart ứng dụng để Hibernate cập nhật schema:

1. Stop ứng dụng hiện tại (Ctrl+C trong terminal)
2. Chạy lại: `./mvnw spring-boot:run`

## Thay đổi trong Code

### Entities đã cập nhật:

#### DaqStaff.java
- ✅ Thêm field `email`
- ✅ Thêm field `address`
- ✅ Thêm relationship `@OneToMany` với `DaqOrder`

#### DaqOrder.java
- ✅ Thay `DaqCustomer customer` → `DaqStaff staff`
- ✅ Thay `@JoinColumn(name = "customer_id")` → `@JoinColumn(name = "staff_id")`

### Files đã xóa:
- ❌ `DaqCustomer.java` (Entity)
- ❌ `DaqCustomerRepository.java` (Repository)
- ❌ `DaqCustomerService.java` (Service)
- ❌ `DaqCustomerController.java` (Controller)

### Navigation Menu:
- ❌ Xóa menu "Khách hàng"
- ✅ Đổi "Nhân viên" → "Nhân viên & Khách hàng"

## Cấu trúc Staff mới

Bảng `staff` bây giờ quản lý cả nhân viên và khách hàng:

| Field | Type | Description |
|-------|------|-------------|
| staff_id | INT | Primary Key |
| staff_name | VARCHAR(150) | Tên người dùng |
| phone | VARCHAR(15) | Số điện thoại |
| email | VARCHAR(100) | Email (mới) |
| address | VARCHAR(255) | Địa chỉ (mới) |
| role | VARCHAR(50) | Vai trò: "Admin", "Nhân viên", "Khách hàng" |

## Roles được sử dụng:

- **Admin**: Quản trị viên hệ thống
- **Nhân viên**: Nhân viên bán hàng
- **Khách hàng**: Khách hàng mua hàng

## Lưu ý quan trọng

⚠️ **Backup database trước khi migrate!**

```sql
-- Backup database
mysqldump -u root -p -P 3307 shop_do_an_vat > backup_before_migration.sql
```

⚠️ **Script migration sẽ:**
1. Migrate tất cả dữ liệu từ `customer` sang `staff` với role "Khách hàng"
2. Update tất cả `orders` để link với `staff_id` thay vì `customer_id`
3. Xóa bảng `customer` hoàn toàn

⚠️ **Không thể rollback sau khi xóa bảng customer!**

## Kiểm tra sau migration

1. ✅ Tất cả khách hàng cũ đã được chuyển sang bảng staff
2. ✅ Tất cả đơn hàng đã được link đúng với staff
3. ✅ Ứng dụng Spring Boot khởi động không lỗi
4. ✅ Có thể truy cập `/admin/daqStaff` và thấy cả nhân viên và khách hàng
5. ✅ Có thể tạo đơn hàng mới với staff_id

## Troubleshooting

### Lỗi: Foreign key constraint fails
- Kiểm tra xem có orders nào đang reference customer_id không tồn tại
- Đảm bảo tất cả customer_id trong orders đều có trong bảng customer

### Lỗi: Duplicate entry
- Kiểm tra xem có email trùng lặp giữa customer và staff không
- Có thể cần update email trước khi migrate

### Lỗi: Spring Boot không khởi động
- Kiểm tra console log để xem lỗi cụ thể
- Đảm bảo đã xóa tất cả file DaqCustomer*
- Clean và rebuild project: `./mvnw clean install`
