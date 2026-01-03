# 🚗 Car Rental API - System Architecture

Zamonaviy texnologiyalar (Spring Boot, Kafka, Redis) asosida qurilgan, avtomobillar ijarasini boshqarish uchun mo'ljallangan backend tizimi.

## 🛠 Texnologiyalar
* **Java 21 & Spring Boot 4**
* **PostgreSQL** - Asosiy ma'lumotlar bazasi.
* **Redis** - Rate limiting (urinishlarni cheklash) va kesh uchun.
* **Apache Kafka (KRaft mode)** - Asinxron xabar almashish.
* **Docker & Docker Compose** - Infratuzilmani konteynerizatsiya qilish.
* **Spring Security & JWT** - Avtorizatsiya va xavfsizlik.
* **Flyway** - Ma'lumotlar bazasi migratsiyasi.
* **Swagger/OpenAPI** - API hujjatlashtirish.



## 🏗 Arxitektura va Imkoniyatlar

* **Rate Limiting:** Redis yordamida foydalanuvchi OTP kodni noto'g'ri kiritsa, tizim urinishlarni sanaydi va ma'lum miqdordan oshganda foydalanuvchini vaqtincha bloklaydi.
* **Event-Driven Design:** Ro'yxatdan o'tish kabi jarayonlar tugagach, Kafka Producer orqali bildirishnomalar yuboriladi. Bu tizimning tezkorligini (Performance) oshiradi.
* **Status Management:** Foydalanuvchi profili qat'iy zanjir asosida ishlaydi: `REGISTRATION_PROGRESS` -> `PENDING_ADMIN_APPROVAL` -> `ACTIVE`.
* **File Storage:** Pasport va litsenziya rasmlari dinamik papkalar tuzilmasida saqlanadi.



## 🚀 Loyihani ishga tushirish

1.  **Repozitoriyani yuklab oling:**
    ```bash
    git clone [https://github.com/your-username/Car_Rental_API.git](https://github.com/your-username/Car_Rental_API.git)
    cd Car_Rental_API
    ```

2.  **Loyihani build qiling:**
    ```bash
    mvn clean package -DskipTests
    ```

3.  **Docker orqali ishga tushiring:**
    ```bash
    docker-compose up --build
    ```

---

# 🚗 Car Rental API - English Documentation

A high-performance backend system for Car Rental Management, focusing on scalability, security, and asynchronous event processing.

## 🛠 Tech Stack
* **Java 17 & Spring Boot 3**
* **PostgreSQL** - Primary database.
* **Redis** - For Rate Limiting (OTP verification control).
* **Apache Kafka (KRaft mode)** - Asynchronous messaging for notification services.
* **Docker & Docker Compose** - Infrastructure orchestration.
* **Spring Security & JWT** - Secure authentication flow.
* **Flyway** - Database versioning.



## 🏗 Key Features
* **Advanced Rate Limiting:** Prevents brute-force attacks on verification codes using Redis counters.
* **Asynchronous Processing:** User registration events are published to Kafka topics, allowing decoupled notification services to process them.
* **Multipart File Handling:** Robust system for managing user identity documents (Passport/License).
* **State Machine:** Managed profile lifecycle with admin approval workflows.



## 🚀 Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/Car_Rental_API.git](https://github.com/your-username/Car_Rental_API.git)
    ```

2.  **Build the JAR file:**
    ```bash
    mvn clean package -DskipTests
    ```

3.  **Run with Docker:**
    ```bash
    docker-compose up --build
    ```

## 📖 API Documentation
Once the application is started, Swagger UI is available at:
`http://localhost:8080/swagger-ui.html`