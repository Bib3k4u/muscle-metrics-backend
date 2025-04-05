# MuscleMetrics API Documentation
MuscleMetrics is a comprehensive workout tracking application built with Spring Boot and MongoDB. It allows users to manage their workouts, track progress, and optimize their fitness journey.

## Features

- **User Authentication**: Secure signup and login using JWT token authentication
- **User Profile Management**: Update username, email, password, weight, and height
- **Exercise Templates**: Over 20 exercises for each major muscle group
- **Workout Management**: Create, update, and delete workouts
- **Exercise Tracking**: Log sets, reps, and weights for each exercise
- **Volume Calculation**: Automatically calculate total volume for each exercise
- **Workout Copying**: Copy previous workouts to quickly create new ones

This document provides examples for each API endpoint in the MuscleMetrics application.

## Authentication

### Register a new user

```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": "60c72b2f5c8r9e0012345678",
  "username": "johndoe",
  "email": "john@example.com",
  "roles": ["USER"]
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": "60c72b2f5c8r9e0012345678",
  "username": "johndoe",
  "email": "john@example.com",
  "roles": ["USER"]
}
```

## User Profile Management

### Get Current User Profile

```http
GET /api/users/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345678",
  "username": "johndoe",
  "email": "john@example.com",
  "weight": 75.5,
  "height": 180.0,
  "roles": ["USER"]
}
```

### Update User Profile

```http
PUT /api/users/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "weight": 74.8,
  "height": 180.0
}
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345678",
  "username": "johndoe",
  "email": "john@example.com",
  "weight": 74.8,
  "height": 180.0,
  "roles": ["USER"]
}
```

### Change Password

```http
POST /api/users/change-password
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "currentPassword": "password123",
  "newPassword": "newPassword456"
}
```

Response:

```json
{
  "message": "Password changed successfully"
}
```

## Muscle Groups

### Get All Muscle Groups

```http
GET /api/muscle-groups
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```json
[
  {
    "id": "60c72b2f5c8r9e0012345679",
    "name": "Chest"
  },
  {
    "id": "60c72b2f5c8r9e0012345680",
    "name": "Back"
  },
  {
    "id": "60c72b2f5c8r9e0012345681",
    "name": "Legs"
  }
]
```

### Get Muscle Group by ID

```http
GET /api/muscle-groups/60c72b2f5c8r9e0012345679
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345679",
  "name": "Chest"
}
```

### Create Muscle Group (Admin Only)

```http
POST /api/muscle-groups
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "Forearms"
}
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345682",
  "name": "Forearms"
}
```

### Update Muscle Group (Admin Only)

```http
PUT /api/muscle-groups/60c72b2f5c8r9e0012345682
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "Forearms & Grip"
}
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345682",
  "name": "Forearms & Grip"
}
```

### Delete Muscle Group (Admin Only)

```http
DELETE /api/muscle-groups/60c72b2f5c8r9e0012345682
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```
200 OK
```

## Exercise Templates

### Get All Exercise Templates

```http
GET /api/exercise-templates
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```json
[
  {
    "id": "60c72b2f5c8r9e0012345683",
    "name": "Bench Press",
    "primaryMuscleGroup": {
      "id": "60c72b2f5c8r9e0012345679",
      "name": "Chest"
    },
    "secondaryMuscleGroup": {
      "id": "60c72b2f5c8r9e0012345684",
      "name": "Triceps"
    },
    "description": "Lie on a flat bench and press the weight upward",
    "requiresWeight": true
  },
  {
    "id": "60c72b2f5c8r9e0012345685",
    "name": "Pull-Up",
    "primaryMuscleGroup": {
      "id": "60c72b2f5c8r9e0012345680",
      "name": "Back"
    },
    "secondaryMuscleGroup": {
      "id": "60c72b2f5c8r9e0012345686",
      "name": "Biceps"
    },
    "description": "Pull your body upward by gripping an overhead bar",
    "requiresWeight": false
  }
]
```

### Get Exercise Template by ID

```http
GET /api/exercise-templates/60c72b2f5c8r9e0012345683
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345683",
  "name": "Bench Press",
  "primaryMuscleGroup": {
    "id": "60c72b2f5c8r9e0012345679",
    "name": "Chest"
  },
  "secondaryMuscleGroup": {
    "id": "60c72b2f5c8r9e0012345684",
    "name": "Triceps"
  },
  "description": "Lie on a flat bench and press the weight upward",
  "requiresWeight": true
}
```

### Get Exercise Templates by Muscle Group

```http
GET /api/exercise-templates/by-muscle-group/60c72b2f5c8r9e0012345679
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```json
[
  {
    "id": "60c72b2f5c8r9e0012345683",
    "name": "Bench Press",
    "primaryMuscleGroup": {
      "id": "60c72b2f5c8r9e0012345679",
      "name": "Chest"
    },
    "secondaryMuscleGroup": {
      "id": "60c72b2f5c8r9e0012345684",
      "name": "Triceps"
    },
    "description": "Lie on a flat bench and press the weight upward",
    "requiresWeight": true
  },
  {
    "id": "60c72b2f5c8r9e0012345687",
    "name": "Dumbbell Fly",
    "primaryMuscleGroup": {
      "id": "60c72b2f5c8r9e0012345679",
      "name": "Chest"
    },
    "secondaryMuscleGroup": null,
    "description": "Lie on a bench with dumbbells extended to the sides, then bring them together in an arc",
    "requiresWeight": true
  }
]
```

### Get Public Exercise Templates by Muscle Group

```http
GET /api/exercise-templates/public/by-muscle-group/60c72b2f5c8r9e0012345679
```

Response: Same as above but doesn't require authentication.

### Create Exercise Template (Admin Only)

```http
POST /api/exercise-templates
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "Incline Dumbbell Press",
  "primaryMuscleGroupId": "60c72b2f5c8r9e0012345679",
  "secondaryMuscleGroupId": "60c72b2f5c8r9e0012345688",
  "description": "Lie on an inclined bench and press dumbbells upward",
  "requiresWeight": true
}
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345689",
  "name": "Incline Dumbbell Press",
  "primaryMuscleGroup": {
    "id": "60c72b2f5c8r9e0012345679",
    "name": "Chest"
  },
  "secondaryMuscleGroup": {
    "id": "60c72b2f5c8r9e0012345688",
    "name": "Shoulders"
  },
  "description": "Lie on an inclined bench and press dumbbells upward",
  "requiresWeight": true
}
```

### Update Exercise Template (Admin Only)

```http
PUT /api/exercise-templates/60c72b2f5c8r9e0012345689
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "Incline Dumbbell Press",
  "primaryMuscleGroupId": "60c72b2f5c8r9e0012345679",
  "secondaryMuscleGroupId": "60c72b2f5c8r9e0012345688",
  "description": "Lie on a 30-45 degree inclined bench and press dumbbells upward",
  "requiresWeight": true
}
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345689",
  "name": "Incline Dumbbell Press",
  "primaryMuscleGroup": {
    "id": "60c72b2f5c8r9e0012345679",
    "name": "Chest"
  },
  "secondaryMuscleGroup": {
    "id": "60c72b2f5c8r9e0012345688",
    "name": "Shoulders"
  },
  "description": "Lie on a 30-45 degree inclined bench and press dumbbells upward",
  "requiresWeight": true
}
```

### Delete Exercise Template (Admin Only)

```http
DELETE /api/exercise-templates/60c72b2f5c8r9e0012345689
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```
200 OK
```

## Workouts

### Get All Workouts for Current User

```http
GET /api/workouts
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```json
[
  {
    "id": "60c72b2f5c8r9e0012345690",
    "user": {
      "id": "60c72b2f5c8r9e0012345678",
      "username": "johndoe",
      "email": "john@example.com"
    },
    "date": "2023-06-12",
    "dayOfWeek": "MONDAY",
    "name": "Chest & Triceps",
    "targetMuscleGroups": [
      {
        "id": "60c72b2f5c8r9e0012345679",
        "name": "Chest"
      },
      {
        "id": "60c72b2f5c8r9e0012345684",
        "name": "Triceps"
      }
    ],
    "exercises": [
      {
        "id": "60c72b2f5c8r9e0012345691",
        "exerciseTemplate": {
          "id": "60c72b2f5c8r9e0012345683",
          "name": "Bench Press"
        },
        "sets": [
          {
            "reps": 12,
            "weight": 60.0
          },
          {
            "reps": 10,
            "weight": 70.0
          },
          {
            "reps": 8,
            "weight": 80.0
          }
        ],
        "totalVolume": 1720.0
      }
    ],
    "notes": "Feeling good today"
  }
]
```

### Get Workout by ID

```http
GET /api/workouts/60c72b2f5c8r9e0012345690
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345690",
  "user": {
    "id": "60c72b2f5c8r9e0012345678",
    "username": "johndoe",
    "email": "john@example.com"
  },
  "date": "2023-06-12",
  "dayOfWeek": "MONDAY",
  "name": "Chest & Triceps",
  "targetMuscleGroups": [
    {
      "id": "60c72b2f5c8r9e0012345679",
      "name": "Chest"
    },
    {
      "id": "60c72b2f5c8r9e0012345684",
      "name": "Triceps"
    }
  ],
  "exercises": [
    {
      "id": "60c72b2f5c8r9e0012345691",
      "exerciseTemplate": {
        "id": "60c72b2f5c8r9e0012345683",
        "name": "Bench Press"
      },
      "sets": [
        {
          "reps": 12,
          "weight": 60.0
        },
        {
          "reps": 10,
          "weight": 70.0
        },
        {
          "reps": 8,
          "weight": 80.0
        }
      ],
      "totalVolume": 1720.0
    }
  ],
  "notes": "Feeling good today"
}
```

### Get Workouts by Date Range

```http
GET /api/workouts/by-date-range?startDate=2023-06-01&endDate=2023-06-30
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response: Array of workouts within the date range.

### Get Workouts by Muscle Group

```http
GET /api/workouts/by-muscle-group/60c72b2f5c8r9e0012345679
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response: Array of workouts that target the specified muscle group.

### Create a New Workout

```http
POST /api/workouts
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "Back & Biceps",
  "date": "2023-06-14",
  "targetMuscleGroupIds": ["60c72b2f5c8r9e0012345680", "60c72b2f5c8r9e0012345686"],
  "notes": "Focus on form"
}
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345692",
  "user": {
    "id": "60c72b2f5c8r9e0012345678",
    "username": "johndoe",
    "email": "john@example.com"
  },
  "date": "2023-06-14",
  "dayOfWeek": "WEDNESDAY",
  "name": "Back & Biceps",
  "targetMuscleGroups": [
    {
      "id": "60c72b2f5c8r9e0012345680",
      "name": "Back"
    },
    {
      "id": "60c72b2f5c8r9e0012345686",
      "name": "Biceps"
    }
  ],
  "exercises": [],
  "notes": "Focus on form"
}
```

### Update a Workout

```http
PUT /api/workouts/60c72b2f5c8r9e0012345692
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "Back & Biceps",
  "date": "2023-06-15",
  "targetMuscleGroupIds": ["60c72b2f5c8r9e0012345680", "60c72b2f5c8r9e0012345686"],
  "notes": "Moved to Thursday due to schedule change"
}
```

Response: Updated workout JSON.

### Delete a Workout

```http
DELETE /api/workouts/60c72b2f5c8r9e0012345692
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```
200 OK
```

### Add Exercise to a Workout

```http
POST /api/workouts/60c72b2f5c8r9e0012345690/exercises
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "exerciseTemplateId": "60c72b2f5c8r9e0012345687",
  "sets": [
    {
      "reps": 15,
      "weight": 15.0
    },
    {
      "reps": 12,
      "weight": 17.5
    },
    {
      "reps": 10,
      "weight": 20.0
    }
  ]
}
```

Response:

```json
{
  "id": "60c72b2f5c8r9e0012345693",
  "exerciseTemplate": {
    "id": "60c72b2f5c8r9e0012345687",
    "name": "Dumbbell Fly"
  },
  "sets": [
    {
      "reps": 15,
      "weight": 15.0
    },
    {
      "reps": 12,
      "weight": 17.5
    },
    {
      "reps": 10,
      "weight": 20.0
    }
  ],
  "totalVolume": 620.0
}
```

### Update Exercise in a Workout

```http
PUT /api/workouts/60c72b2f5c8r9e0012345690/exercises/60c72b2f5c8r9e0012345693
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "exerciseTemplateId": "60c72b2f5c8r9e0012345687",
  "sets": [
    {
      "reps": 15,
      "weight": 15.0
    },
    {
      "reps": 12,
      "weight": 17.5
    },
    {
      "reps": 10,
      "weight": 20.0
    },
    {
      "reps": 8,
      "weight": 22.5
    }
  ]
}
```

Response: Updated exercise with the new set added.

### Remove Exercise from a Workout

```http
DELETE /api/workouts/60c72b2f5c8r9e0012345690/exercises/60c72b2f5c8r9e0012345693
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response:

```
200 OK
```

### Copy a Workout to a New Date

```http
POST /api/workouts/60c72b2f5c8r9e0012345690/copy?newDate=2023-06-19
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Response: New workout JSON with the same exercises and details but with the new date.
