# Internship Management System – METU NCC 🎓

A web-based platform designed to streamline the summer internship process at METU Northern Cyprus Campus. This system simplifies every step for students, instructors, coordinators, companies, and student affairs – from applications to evaluations – all in one place.

---

## ✨ Features

- 🔔 Automatic email notifications for deadlines, approvals, and assignments
- 💡 Resume Recommendation (Checking in the system with appropriate intern and job via with specialized regex)
- 📄 CV upload and internship application portal
- 📊 Instructor grading and report evaluation interface
- 📥 Internship form management by students and coordinators
- 📈 Company evaluation and supervisor assignment
- 📁 Exportable Excel reports for Student Affairs
- 🔎 Internship browsing with filters (company, country, etc.)
- 🔐 Secure access for five distinct user roles:
  - Student
  - Coordinator
  - Instructor
  - Company
  - Student Affairs

---

## 🚀 Technologies Used

### Backend
- **Java** (Spring Boot 3.3.6)
- **PostgreSQL 15**
- **Hibernate / Spring Data JPA**
- **JUnit 5 & Mockito** for unit testing

### Frontend
- **Angular 19.0.4**
- **Bootstrap**
- **TailwindCSS**
- **Playwright** for end-to-end testing

---

## 🛠 Setup Instructions

> 💡 This project consists of **two parts**: `backend` and `frontend`. You need to run both separately.

### Prerequisites
- Java 17+
- Node.js & npm
- IntelliJ IDEA (or any IDE)
- PostgreSQL installed locally
- Git

---

### 🔧 Backend Setup (Spring Boot)

```bash
cd backend
```

1. Open the project in IntelliJ.
2. Ensure `application.properties` (or `.yml`) is configured for your PostgreSQL:
   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/YOUR_DB
   spring.datasource.username=YOUR_USER
   spring.datasource.password=YOUR_PASSWORD
   ```
3. Run the project using Maven:
   ```
   ./mvnw spring-boot:run
   ```

---

### 🎨 Frontend Setup (Angular)

```bash
cd frontend
```

1. Install dependencies:
   ```bash
   npm install
   ```
2. Start the Angular app:
   ```bash
   ng serve
   ```
3. Access the app at [http://localhost:4200](http://localhost:4200)

---

## 👨‍💻 Authors

This project was developed as a graduation project by:

- Umutcan Çelik  
- Ali Fırat Özdemir  
- Mert Damburacı  
- Efekan Uysal

> Supervised by: **İdil Candan**  
> Middle East Technical University – Northern Cyprus Campus

---

## 📌 Notes

- 📬 The platform integrates directly with Google Mail for sending automatic notifications.
- 🔐 User data is handled securely and aligns with GDPR-compliant practices.
- 📱 Responsive design – usable on desktop, tablet, and mobile devices.

---

## ⚠️ License

This project currently does **not have a license**. All rights reserved by the authors.

---

## 🙌 Future Improvements

- Deployment to a cloud provider (e.g., Heroku, Vercel, GCP)
- Role-based dashboards with charts
- Admin analytics panel
- Multilingual support

---

Thanks for visiting the repo! Feel free to clone, explore, or contribute if licensing is defined in the future.
