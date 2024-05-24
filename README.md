# Jawwy Weather App

Jawwy is a weather application that provides current weather information and a 7-day forecast for your location or any location you pick. The app includes an integrated map for location selection and a search bar for convenience. Additionally, users can set alerts to notify them of any climate disturbances.

## Features

- **Current Weather Information**: Real-time weather data for your current location or any selected location.
- **7-Day Weather Forecast**: Detailed weather forecast for the next seven days.
- **Location Selection**: Choose locations using an integrated map or a search bar.
- **Weather Alerts**: Notifications for significant weather disturbances or climate changes.

## Architecture

- **MVVM (Model-View-ViewModel)**: Ensures a clear separation of concerns, making the app more maintainable and testable.

## Language

- **Kotlin**: Developed using Kotlin, a modern, expressive, and type-safe programming language.

## Technologies and Libraries Used

- **Maps**: For location selection and display.
- **Services**: To handle background tasks and network operations.
- **Unit Testing**: Ensures the reliability and correctness of the codebase.
- **Kotlin Coroutines**: Facilitates asynchronous programming for smooth and efficient operations.
- **Room (SQLite)**: Local database management for storing weather data and user preferences.
- **Notifications**: To alert users about weather changes and disturbances.
- **Retrofit**: For making HTTP requests to fetch weather data from APIs.
- **Work Manager**: Manages background tasks, such as fetching updated weather data and scheduling notifications.
