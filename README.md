# StudyTrack – Study Tracking & Personalized Roadmap Management System

A full-stack, enterprise-grade web application designed for students to track daily study progress, follow personalized learning roadmaps, maintain study streaks, and for administrators to manage student cohorts, monitor institutional analytics, and configure curriculum tracks.

---

## 📁 Project Directory Structure

```
stud-perf/
├── backend/                  # Spring Boot 3.3.5 Backend (Java 25 LTS)
│   ├── pom.xml               # Maven configuration (Spring Security, JWT JJWT, Spring Data JPA, MySQL)
│   └── src/
│       └── main/
│           ├── java/com/studytrack/
│           │   ├── config/   # SecurityConfig, WebMvcConfig (CORS), DataSeeder, JwtTokenProvider
│           │   ├── controller/ # Auth, Admin, Roadmap, Task, Progress, StudyLog, Notes, Announcements
│           │   ├── dto/      # Data Transfer Objects (Auth, Admin, Roadmap, Task, Progress, Notes)
│           │   ├── entity/   # JPA Entities (User, Student, Roadmap, Subject, Module, Topic, Task, etc.)
│           │   ├── repository/ # Spring Data JPA Repositories
│           │   └── service/  # Business logic layer
│           └── resources/
│               └── application.properties # MySQL datasource & JWT configuration
│
├── frontend/                 # React 18 + Vite + Tailwind CSS Frontend
│   ├── package.json          # Vite, Tailwind, Lucide React, Recharts, Canvas-Confetti, Axios
│   ├── vite.config.js        # Vite dev server proxying `/api` to `http://localhost:8086`
│   ├── tailwind.config.js    # Styling design system
│   └── src/
│       ├── components/       # Common UI: StatCard, ProgressBar, Modal, Navbar, Sidebar
│       ├── context/          # AuthContext (JWT persistence), NotificationContext
│       ├── pages/
│       │   ├── auth/         # Clean Login page
│       │   ├── student/      # Student Dashboard, My Roadmap, Tasks, Study Log, Calendar, Notes, Progress
│       │   └── admin/        # Admin Dashboard, Students, Detail Drilldown, Roadmaps, Tasks, Analytics
│       ├── routes/           # AppRoutes with role-based ProtectedRoute
│       └── services/         # Axios instance with JWT Authorization interceptor
│
└── README.md                 # Project documentation
```

---

## 🚀 How to Run the Application

### Prerequisites
- **Java JDK 25** (or JDK 17+)
- **Maven 3.8+**
- **Node.js 18+** & **npm**
- **MySQL 8.0** running on `localhost:3306`

### 1. Database Setup
MySQL configuration in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/studytrack_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Archana@123
```
*Note: The database starts clean without any dummy students or sample roadmaps.*

### 2. Start Backend Server
```bash
cd backend
mvn spring-boot:run
```
Backend will be available at: **`http://localhost:8086`**

### 3. Start Frontend Development Server
```bash
cd frontend
npm install
npm run dev
```
Frontend will be available at: **`http://localhost:5173`**

---

## 🔑 Initial Administrator Account

The system is configured with a default master administrator account so you can immediately log in and begin creating your own customized roadmaps and enrolling real students:

| Role | Email | Password | Access |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@studytrack.com` | `Admin@123` | Master Admin Console |

*Once logged in as Admin, you can:*
1. **Create Roadmaps**: Navigate to **Roadmaps** and create learning tracks with custom Subjects, Modules, and Topics.
2. **Enroll Students**: Navigate to **Students** and click **Enroll New Student**.
3. **Assign Roadmaps**: Assign newly created tracks directly to enrolled students.
4. **Assign Tasks**: Schedule and assign daily learning tasks with deadlines and priorities.

---

## 🌟 Key Features

### Student Portal
- **Dashboard**: Live study streak calculation, interactive daily task checklist with confetti celebration, today's progress bar, subject breakdown.
- **My Roadmap**: Dynamic hierarchical tree (**Roadmap → Subject → Module → Topic**) with status indicators.
- **Daily Tasks**: Filter by Today's Focus vs All, priority and status filters, mark tasks as in progress or completed.
- **Study Log**: Log focused study sessions with duration, topics, and notes; track today, weekly, and all-time totals.
- **Study Calendar**: Monthly activity heat map based on study minutes; click any day for daily details.
- **Study Notes**: Create, edit, and filter study notes with tags and markdown support.
- **Analytics**: Recharts graphs showing weekly study hours and task completion velocity.

### Admin Portal
- **Command Center**: Real-time KPI cards, progress distribution chart, weekly study trend, task status breakdown, roadmap comparison, students falling behind alerts.
- **Student Management**: Full CRUD (add, edit, cascade delete), search, filter, and assign/switch roadmaps.
- **Student Drill-Down**: Deep dive into individual student progress, task history, and study logs.
- **Roadmap Management**: Build curriculum catalog, view hierarchy structures, and assign to students.
- **Task Assignment**: Assign custom tasks directly to specific students with due dates and priorities.
- **Institutional Analytics**: Leaderboards, at-risk student monitoring, and curriculum efficacy comparison.
- **Announcements**: Real-time broadcast system with priority levels (`LOW`, `MEDIUM`, `HIGH`).
