# MuscleMetrics

MuscleMetrics is a comprehensive workout tracking application built with Spring Boot and MongoDB. It allows users to manage their workouts, track progress, and optimize their fitness journey.

## Features

- **User Authentication**: Secure signup and login using JWT token authentication
- **User Profile Management**: Update username, email, password, weight, and height
- **Exercise Templates**: Over 20 exercises for each major muscle group
- **Workout Management**: Create, update, and delete workouts
- **Exercise Tracking**: Log sets, reps, and weights for each exercise
- **Volume Calculation**: Automatically calculate total volume for each exercise
- **Workout Copying**: Copy previous workouts to quickly create new ones

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.0
- **Database**: MongoDB
- **Security**: Spring Security, JWT
- **Validation**: Jakarta Bean Validation

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher
- MongoDB (local installation or Atlas cloud database)

### Installation and Setup

1. Clone the repository:

   ```
   git clone https://github.com/yourusername/muscle-metrics.git
   cd muscle-metrics
   ```

2. Update the MongoDB connection in `src/main/resources/application.properties` if needed

3. Build the application:

   ```
   mvn clean install
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`.

### Default Admin User

- Email: admin@musclemetrics.com
- Password: admin1234

## API Endpoints

### Authentication

- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token

### User Management

- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update user profile
- `POST /api/users/change-password` - Change password

### Muscle Groups

- `GET /api/muscle-groups` - Get all muscle groups
- `GET /api/muscle-groups/{id}` - Get muscle group by ID

### Exercise Templates

- `GET /api/exercise-templates` - Get all exercise templates
- `GET /api/exercise-templates/{id}` - Get exercise template by ID
- `GET /api/exercise-templates/by-muscle-group/{muscleGroupId}` - Get exercise templates by muscle group

### Workouts

- `GET /api/workouts` - Get all workouts for current user
- `GET /api/workouts/{id}` - Get workout by ID
- `GET /api/workouts/by-date-range` - Get workouts in a date range
- `GET /api/workouts/by-muscle-group/{muscleGroupId}` - Get workouts by muscle group
- `POST /api/workouts` - Create a new workout
- `PUT /api/workouts/{id}` - Update a workout
- `DELETE /api/workouts/{id}` - Delete a workout
- `POST /api/workouts/{workoutId}/exercises` - Add exercise to a workout
- `PUT /api/workouts/{workoutId}/exercises/{exerciseId}` - Update exercise in a workout
- `DELETE /api/workouts/{workoutId}/exercises/{exerciseId}` - Remove exercise from a workout
- `POST /api/workouts/{workoutId}/copy` - Copy a workout to a new date


