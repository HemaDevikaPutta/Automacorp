# Automacorp Android App

This is a Jetpack Compose-based Android application designed to manage and display a list of rooms, with the following key features:

## Features
- **Home Page**: Serves as the entry point of the application.
- **Menu Options**: Includes functionalities to:
  - Open a web page.
  - Send an email.
  - Open github link
- **Room List Page**: Displays a list of rooms fetched from a remote API.
- **Room Detail Page**: Displays details of a room when a specific room in the list is selected.

## Technology Stack
- **Android Jetpack Compose**: For building modern and declarative UI.
- **Kotlin**: Programming language used for app development.
- **Retrofit**: To interact with a remote API.
- **Coroutines**: For managing asynchronous operations.
- **Hilt**: For dependency injection.
- **Material Design 3**: Ensures a modern and visually appealing UI.

## Screens Overview
1. **Home Page**: Displays an introduction or overview of the app.
2. **Menu**:
   - A button to navigate to a web page.
   - A button to compose an email using the device's email client.
   - On below pop enter mail id and click send button automatically sends an email to particular user.
   - When user click github icon it is automatically navigate to github page.
3. **Room List Page**:
   - Displays a list of rooms retrieved from the remote API.
   - Each room is clickable to view more details.
4. **Room Detail Page**: Provides detailed information about the selected room.

## API Integration
The app communicates with a remote API to fetch the list of rooms and their details. Make sure to update the API base URL in the project as per your requirements.

## Prerequisites
 - Android Studio
 - Kotlin
 - An Android device or emulator for testing
## Setup and Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/HemaDevikaPutta/Automacorp.git
