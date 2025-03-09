# DateCounter

DateCounter is an Android application that helps users track important dates and events. It calculates and displays the number of days until future events or days passed since past events.

## Features
- **Event Management**: Create, view, edit, and delete date-based events
- **Day Counting**: Automatically calculates days remaining/passed for each event
- **Home Screen Widgets**: Display event countdowns directly on the home screen
- **Starring System**: Mark important events with a star for emphasis
- **Dark Mode Support**: Optimized for both light and dark themes

## Screenshots

[Insert screenshots here]

## Technical Architecture

### Data Layer
- Room Database for persistent storage
- LiveData for reactive UI updates
- Event model with Room annotations

### UI Components
- Material Design components
- RecyclerView with custom adapter
- Custom drawables for visual presentation

### Widget Implementation
- AppWidgetProvider for home screen widgets
- 30-minute update intervals
- Widget configuration activity

## Package Structure
- `com.jacky.dateCounter`: Main package
  - `.adapter`: RecyclerView adapters
  - `.data`: Database implementations
  - `.model`: Data models
  - `.widget`: Widget components

## Requirements
- Android 8.0 (API level 24) or higher
- Android Studio Arctic Fox or newer

## Installation
###By Android Studio

1. Clone this repository
2. Open the project in Android Studio
3. Build and run the application on your device or emulator

### By APK
1. Download the APK [Here](https://github.com/jackywooks/dateCounter/blob/main/dateCounter.apk)
2. Install it

## Usage

### Adding an Event
1. Tap the floating action button (+)
2. Enter event name, description, and date
3. Tap Save

### Editing an Event
1. Long-press on an event
2. Modify details in the dialog
3. Tap Save

### Adding a Widget
1. Long-press on your home screen
2. Select Widgets
3. Find DateCounter widget
4. Place on home screen
5. Select an event to display

## License
This project is licensed under the MIT License.

## Third-Party Libraries and Licenses

This app uses the following open-source libraries:

### AndroidX Libraries
- **AndroidX Core KTX** - Apache 2.0 License
- **AndroidX AppCompat** - Apache 2.0 License
- **Material Components for Android** - Apache 2.0 License
- **ConstraintLayout** - Apache 2.0 License

### Room Persistence Library
- **Room Runtime** - Apache 2.0 License
- **Room Compiler** - Apache 2.0 License

### Apache License 2.0
