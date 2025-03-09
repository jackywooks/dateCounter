# DateCounter

DateCounter is an Android application that helps users track important dates and events. It calculates and displays the number of days until future events or days passed since past events.

## Features
- **Event Management**: Create, view, edit, and delete date-based events
- **Day Counting**: Automatically calculates days remaining/passed for each event
- **Home Screen Widgets**: Display event countdowns directly on the home screen
- **Starring System**: Mark important events with a star for emphasis
- **Dark Mode Support**: Optimized for both light and dark themes

## Screenshots

InApp:

![image](https://github.com/user-attachments/assets/694dbbbd-08f1-493e-b5b6-28c74aa2750e
![image](https://github.com/user-attachments/assets/b0b94e58-f3b5-4f31-8247-3fbc7e9d38fc)


Widget:
![image](https://github.com/user-attachments/assets/154641ba-114d-44c5-a8e4-8fab1a59fb13)


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
1. Download the APK [Here](https://github.com/jackywooks/dateCounter/tree/main/dateCounter)
2. Install it

## Usage

### Adding an Event
1. Tap the floating action button (+)
2. Enter event name, description, and date
3. ![image](https://github.com/user-attachments/assets/1439e077-e719-48d9-9f33-21a5816ea389)
4. Tap Save

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
- **AndroidX Core KTX**
- **AndroidX AppCompat** 
- **Material Components for Android**
- **ConstraintLayout**
### Room Persistence Library
- **Room Runtime**
- **Room Compiler** 
