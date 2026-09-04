# E-commerce — Backend API

## English

### Task: Workintech Full-Stack E-Commerce Backend Project

### Task Description

As the backend component of the **Workintech Full-Stack Bootcamp** capstone project, I developed a production-ready, RESTful Web API using**Spring Boot 3** 
and **PostgreSQL**. This backend is specifically architected to support the React frontend, mirroring its required request payloads, response shapes, 
and business logic (authentication, role management, product listings, user addresses, credit card storage, and checkout processing).

### Technical Requirements & Architecture Highlights:

- **Java 17 & Spring Boot 3.3.4** for high-performance enterprise backend architecture
- **Spring Data JPA & Hibernate** for ORM and database management (`ddl-auto: update`)
- **PostgreSQL 14+** for relational data persistence
- **Spring Security & JJWT (0.12.6)** for stateless JWT-based authentication and role authorization
- **Maven Wrapper (`mvnw`)** integrated for environment-independent build and deployment execution
- **Spring Starter Mail** for automated account activation emails
- **Lombok** for boilerplate code reduction
- **Data & Product Seeders** (`DataSeeder`, `ProductSeeder`, `seed-data.json`) for automated database initialization

### Key Design Decisions & Features:

- **Flexible JWT Auth Filter:** Handles both raw JWT tokens and `Bearer <token>` formatted headers seamlessly to ensure compatibility with the React frontend.
- **PCI-DSS Compliance for Credit Cards:** Card details are stored safely for user reuse, but **CVV is never stored** in the database (`orders` table omits `card_ccv`). It is verified strictly at checkout time.
- **REST Payload Matching:** Address and credit card update (`PUT`) endpoints expect the entity `id` inside the request body payload (matching the frontend's Redux action signatures).
- **Price Snapshotting:** Product unit prices are snapshotted inside `OrderItem.unitPrice` during checkout, isolating past orders from future price modifications.
- **Inventory & Sales Management:** Automatic stock control during checkout. Insufficient stock yields a `400 Bad Request`, whereas successful orders decrement `stock` and increment `sell_count`.
- **Account Activation Flow:** User registrations create inactive accounts (`is_active=false`) requiring code activation via `GET /activate/{code}`.

### My Learning Journey & Reflection

**Security & JWT Architecture:** Implementing custom authentication filters and security chains with Spring Security 6 deepened my understanding of token lifecycle management, stateless sessions, and CORS handling across frontend-backend boundaries.

**Data Modeling & ORM Best Practices:** Designing relational tables with JPA annotations (OneToMany, ManyToOne, ManyToMany) and handling cascading operations reinforced the importance of database integrity and efficient query performance.

**Matching Frontend Specs (API Contracts):** Tailoring Spring Controllers to respond with identical field names and data structures expected by the React frontend highlighted the real-world necessity of tight API contract compliance in full-stack web development.

**DevOps & Deployment:** Configuring Maven Wrappers and deploying Spring Boot + PostgreSQL instances to cloud platforms like Render provided practical experience in environment variable configuration, build optimization, and cloud database linking.

### Tech Stack

- Java 17
- Spring Boot 3.3.4
- Spring Data JPA (Hibernate)
- Spring Security
- JSON Web Tokens (JJWT 0.12.6)
- PostgreSQL
- Lombok
- Spring Boot Starter Mail
- Maven Wrapper (`mvnw`)

### Project Structure

src/main/
├── java/com/bandage/ecommerce/
│   ├── config/             # Security & CORS configuration
│   ├── controller/         # REST Controllers (Auth, Product, Order, User)
│   ├── dto/                # Request & Response DTOs
│   ├── entity/             # JPA Entities (User, Role, Product, Order, Address, Card, etc.)
│   ├── exception/          # Global exception handling & custom errors
│   ├── repository/         # Spring Data JPA Repositories
│   ├── security/           # JwtAuthFilter, JwtUtils, CustomUserDetailsService
│   ├── seeder/             # DataSeeder & ProductSeeder (seed-data.json runner)
│   └── service/            # Core business logic services
└── resources/
├── application.properties # Environment configuration & database connection
├── schema-reference.sql   # Database schema documentation
└── seed-data.json         # Initial categories & products data

### Deployment (Render & Live Environment)

This project is configured for seamless automated deployment on **Render**:

- **Build Command:** `./mvnw clean package -DskipTests`
- **Start Command:** `java -jar target/ecommerce-0.0.1-SNAPSHOT.jar`
- **Environment Variables:** `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `APP_JWT_SECRET`

### API Endpoints (Frontend Contract)

| Endpoint | Method | Auth | Request Body | Response |
|---|---|---|---|---|
| `/signup` | POST | ❌ | `{name, email, password, role_id, store?}` | `{message}` |
| `/activate/{code}` | GET | ❌ | - | `{message}` |
| `/login` | POST | ❌ | `{email, password}` | `{token, name, email, role_id}` |
| `/verify` | GET | ✅ | - | `{token, name, email, role_id}` |
| `/roles` | GET | ❌ | - | `[{id, name, code}]` |
| `/categories` | GET | ❌ | - | `[{id, title, img, gender, rating, code}]` |
| `/products` | GET | ❌ | query: `category, sort, filter, limit, offset` | `{products: [...], total}` |
| `/products/{id}` | GET | ❌ | - | `{id, name, description, price, stock, ...}` |
| `/user/address` | GET | ✅ | - | `[{id, title, name, surname, phone, city, ...}]` |
| `/user/address` | POST | ✅ | `{title, name, surname, phone, city, district, neighborhood, address}` | Created address |
| `/user/address` | PUT | ✅ | same + `id` | Updated address |
| `/user/address/{id}` | DELETE | ✅ | - | `{message}` |
| `/user/card` | GET | ✅ | - | `[{id, card_no, expire_month, expire_year, name_on_card}]` |
| `/user/card` | POST | ✅ | `{card_no, expire_month, expire_year, name_on_card}` | Created card |
| `/user/card` | PUT | ✅ | same + `id` | Updated card |
| `/user/card/{id}` | DELETE | ✅ | - | `{message}` |
| `/order` | GET | ✅ | - | `[{id, order_date, price, products: [...], ...}]` |
| `/order` | POST | ✅ | `{address_id, card_no, card_name, card_expire_month, card_expire_year, card_ccv, price, products: [{product_id, count, detail}]}` | Created order |

---

## Türkçe

### Görev: Workintech Full-Stack E-Ticaret Backend Projesi

### Görev Tanımı

**Workintech Full-Stack Bootcamp** bitirme projesinin backend bileşeni olarak, **Spring Boot 3** ve **PostgreSQL** kullanarak yayına hazır, 
RESTful bir Web API geliştirdim. Bu backend; kullanıcı kimlik doğrulama, rol yönetimi, ürün listeleme/filtreleme, adres ve kredi kartı saklama ile 
sipariş tamamlama süreçlerinde React frontend uygulamasının beklediği tüm istek gövdelerini (payload), yanıt formatlarını ve iş mantığını 
bire bir destekleyecek şekilde mimarileştirilmiştir.

### Teknik Gereksinimler ve Mimari Öne Çıkanlar:

- **Java 17 & Spring Boot 3.3.4** ile yüksek performanslı kurumsal backend mimarisi
- **Spring Data JPA & Hibernate** ile ORM ve veritabanı yönetimi (`ddl-auto: update`)
- **PostgreSQL 14+** ilişkisel veritabanı
- **Spring Security & JJWT (0.12.6)** ile durumsuz (stateless) JWT tabanlı kimlik doğrulama ve rol yetkilendirme
- **Maven Wrapper (`mvnw`)** ile ortamlardan bağımsız derleme ve çalıştırma desteği
- **Spring Starter Mail** ile otomatik hesap aktivasyon e-postaları
- **Lombok** ile temiz ve okunabilir kod yapısı
- **Data & Product Seeders** (`DataSeeder`, `ProductSeeder`, `seed-data.json`) ile otomatik başlangıç verisi yükleme

### Tamamlanan Özellikler ve Çekirdek Mantık:

- **Esnek JWT Filtresi:** Frontend uyumluluğu için hem ham JWT token hem de `Bearer <token>` formatındaki Authorization header'larını kabul eder.
- **Kredi Kartında PCI-DSS Güvenliği:** Kart bilgileri tekrar kullanım için saklanırken **CVV bilgisi veritabanına asla kaydedilmez** (`orders` tablosunda `card_ccv` kolonu yoktur). CVV sadece sipariş anında doğrulanır.
- **Frontend ile Bire Bir REST Uyumu:** Adres ve kart güncelleme (`PUT`) endpoint'leri, güncellenecek veri kimliğini (`id`) URL path'i yerine istek gövdesinden (body) okur.
- **Fiyat Anlık Görüntüsü (Snapshot):** Sipariş anında ürün birim fiyatı `OrderItem.unitPrice` alanına kaydedilir; böylece ürünün gelecekteki fiyat değişimleri geçmiş siparişleri etkilemez.
- **Stok ve Satış Yönetimi:** Sipariş anında otomatik stok kontrolü yapılır. Yetersiz stok durumunda `400 Bad Request` döner; başarılı siparişlerde ürün stoğu düşürülüp `sell_count` artırılır.
- **E-posta ile Hesap Aktivasyonu:** Kayıt olan kullanıcılar pasif (`is_active=false`) olarak oluşturulur ve `GET /activate/{code}` bağlantısına tıklayarak hesaplarını aktifleştirir.

### Gelişim Süreci ve Notlarım

**Güvenlik ve JWT Mimarisi:** Spring Security 6 ile özel kimlik doğrulama filtreleri ve güvenlik zincirleri oluşturmak; token yaşam döngüsü, durumsuz oturumlar ve CORS yönetimi konularında derinlemesine tecrübe kazandırdı.

**Veri Modelleme ve ORM İlkeleri:** JPA ilişki anatasyonları (OneToMany, ManyToOne, ManyToMany) ile ilişkisel tablolar tasarlamak ve cascading mantığını kurgulamak, veritabanı bütünlüğü ve sorgu performansı açısından kritik bir deneyim oldu.

**Frontend Sözleşmelerine Uyum (API Contracts):** Spring Controller yapısını React tarafındaki Redux action'larının beklediği alan adları ve veri yapılarıyla bire bir örtüştürmek, full-stack geliştirmede API sözleşmelerine sadık kalmanın önemini gösterdi.

**DevOps ve Canlıya Alma (Deployment):** Maven Wrapper entegrasyonu ile Spring Boot ve PostgreSQL projelerini Render gibi bulut platformlarına canlıya almak; ortam değişkenleri, derleme adımları ve bulut veritabanı bağlantıları konularında pratik becerilerimi geliştirdi.

### Kullanılan Teknolojiler

- Java 17
- Spring Boot 3.3.4
- Spring Data JPA (Hibernate)
- Spring Security
- JSON Web Tokens (JJWT 0.12.6)
- PostgreSQL
- Lombok
- Spring Boot Starter Mail
- Maven Wrapper (`mvnw`)

### Proje Yapısı

src/main/
├── java/com/bandage/ecommerce/
│   ├── config/             # Güvenlik ve CORS yapılandırmaları
│   ├── controller/         # REST Controller sınıfları (Auth, Product, Order, User)
│   ├── dto/                # İstek ve Yanıt DTO'ları
│   ├── entity/             # JPA Entity sınıfları (User, Role, Product, Order vb.)
│   ├── exception/          # Global hata yönetimi
│   ├── repository/         # Spring Data JPA Repository arayüzleri
│   ├── security/           # JwtAuthFilter, JwtUtils, CustomUserDetailsService
│   ├── seeder/             # Başlangıç verisi yükleyicileri (DataSeeder & ProductSeeder)
│   └── service/            # İş mantığı (Business Logic) servisleri
└── resources/
├── application.properties # Ortam değişkenleri ve veritabanı bağlantı ayarları
├── schema-reference.sql   # Veritabanı şema dokümantasyonu
└── seed-data.json         # Başlangıç kategori ve ürün verileri


### Canlı Ortam Kurulumu (Render Deployment)

Bu proje **Render** platformu üzerinde canlıya alınacak şekilde yapılandırılmıştır:

- **Build Command:** `./mvnw clean package -DskipTests`
- **Start Command:** `java -jar target/ecommerce-0.0.1-SNAPSHOT.jar`
- **Environment Variables:** `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `APP_JWT_SECRET`

### API Sözleşmesi (Endpoint Listesi)

| Endpoint             | Method | Auth | Request Body                                                                                                                      | Response                                                   |
|----------------------|--------|------|-----------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------|
| `/signup`            | POST   | ❌    | `{name, email, password, role_id, store?}`                                                                                        | `{message}`                                                |
| `/activate/{code}`   | GET    | ❌    | -                                                                                                                                 | `{message}`                                                |
| `/login`             | POST   | ❌    | `{email, password}`                                                                                                               | `{token, name, email, role_id}`                            |
| `/verify`            | GET    | ✅    | -                                                                                                                                 | `{token, name, email, role_id}`                            |
| `/roles`             | GET    | ❌    | -                                                                                                                                 | `[{id, name, code}]`                                       |
| `/categories`        | GET    | ❌    | -                                                                                                                                 | `[{id, title, img, gender, rating, code}]`                 |
| `/products`          | GET    | ❌    | query: `category, sort, filter, limit, offset`                                                                                    | `{products: [...], total}`                                 |
| `/products/{id}`     | GET    | ❌    | -                                                                                                                                 | `{id, name, description, price, stock, ...}`               |
| `/user/address`      | GET    | ✅    | -                                                                                                                                 | `[{id, title, name, surname, phone, city, ...}]`           |
| `/user/address`      | POST   | ✅    | `{title, name, surname, phone, city, district, neighborhood, address}`                                                            | Created address                                            |
| `/user/address`      | PUT    | ✅    | same + `id`                                                                                                                       | Updated address                                            |
| `/user/address/{id}` | DELETE | ✅    | -                                                                                                                                 | `{message}`                                                |
| `/user/card`         | GET    | ✅    | -                                                                                                                                 | `[{id, card_no, expire_month, expire_year, name_on_card}]` |
| `/user/card`         | POST   | ✅    | `{card_no, expire_month, expire_year, name_on_card}`                                                                              | Created card                                               |
| `/user/card`         | PUT    | ✅    | same + `id`                                                                                                                       | Updated card                                               |
| `/user/card/{id}`    | DELETE | ✅    | -                                                                                                                                 | `{message}`                                                |
| `/order`             | GET    | ✅    | -                                                                                                                                 | `[{id, order_date, price, products: [...], ...}]`          |
| `/order`             | POST   | ✅    | `{address_id, card_no, card_name, card_expire_month, card_expire_year, card_ccv, price, products: [{product_id, count, detail}]}` | Created order                                              |