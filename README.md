# NavGO

**NavGO** is an indoor navigation application designed to help users navigate within closed spaces, such as buildings, where traditional GPS-based solutions like Google Maps do not work effectively. This app was developed as part of a college project and is currently optimized for use within the college campus premises. However, NavGO has been designed to be scalable, with the potential for application in other buildings or indoor environments in the future.

## Table of Contents
1. [Features](#features)
2. [How It Works](#how-it-works)
3. [Demo Video](#demo-video)
4. [Skills Used](#skills-used)
5. [Installation](#installation)
6. [Usage](#usage)
7. [Technical Details](#technical-details)
8. [Future Enhancements](#future-enhancements)
9. [Contributing](#contributing)
10. [License](#license)

---

## Features

- **Offline Indoor Navigation**: Navigate within buildings without an internet connection.
- **Optimized for College Premises**: Currently built and optimized for navigating inside our college campus.
- **Shortest Path Mapping**: Automatically calculates and displays the shortest route between any two locations within the mapped area.
- **Scalability**: Built with scalability in mind, so new maps can be added for other buildings or premises in the future.

## How It Works

NavGO uses predefined floor plans and mapping data to guide users inside the college campus without relying on GPS. It allows the user to select a starting point and an endpoint within the building. Using shortest-path algorithms, NavGO then displays the shortest possible path on the map, which the user can follow visually to reach their destination.

## Demo Video

To see **NavGO** in action, watch the demo video here:

[Watch NavGO Demo](<https://youtu.be/g5nJ01_lgLQ?si=D8-IesddO3wMgNG6>)

*This video provides a walkthrough of the app's features and how to navigate within the college campus using the application.*

## Skills Used

The development of **NavGO** involved applying a range of technical and analytical skills, which are detailed below:

- **Android Development**: Core Android skills like activity management, fragment handling, and XML-based UI design were essential for building the app interface and navigation.
- **Java/Kotlin Programming**: Used to implement backend functionality, UI interactivity, and logic for data handling within the application.
- **Data Structures & Algorithms**: Graph theory, shortest-path algorithms (like Dijkstra’s or A*), and matrix representation were employed to calculate optimal routes.
- **Map Design and Image Processing**: Designing a clear, interactive floor map and overlaying it within the app required skills in image processing and UI layout techniques.
- **File Handling**: To store and load maps offline, efficient file handling for loading the campus map and accessing local resources was used.

## Installation

1. **Download the APK**: (Currently not available)
2. **Install the App**:
   - On your Android device, go to Settings > Security > Unknown Sources, and enable this option to allow installations from sources other than the Google Play Store.
   - Open the downloaded APK file and tap **Install**.
3. **Open NavGO**: Once installed, open the app from your home screen or app drawer.

> **Note**: NavGO currently only works with the college campus map, which is preloaded with the app.

## Usage

1. **Launch the App**: Open NavGO on your Android device.
2. **Select Your Map**: As of now, only the college campus map is available and loads by default.
3. **Enter Source and Destination**:
   - Type in the names of your starting location and your desired destination within the building.
4. **View the Route**: The app will calculate and display the shortest path between the two points.
5. **Navigate**: Follow the highlighted path on the map to reach your destination.

## Technical Details

- **Platform**: Android
- **Language**: Java/Kotlin (choose based on the primary language used)
- **Mapping and Pathfinding**:
  - Uses a digital floor plan image of the college campus.
  - Implements a shortest-path algorithm (such as Dijkstra's or A*) for route calculation.
  - Visualizes the calculated path using overlays on the map image.
- **Offline Capabilities**: All data is stored locally on the device, so no internet connection is required after installation.

## Future Enhancements

NavGO was initially developed as a prototype focused on the college premises, but there are several potential areas for expansion and improvement:

1. **Dynamic Map Downloads**: Allow users to download maps of various locations and load them within the app.
2. **Multiple Building Support**: Enable navigation across multiple buildings in a large campus or facility.
3. **Enhanced User Interface**: Improve the visuals and interactivity of the map to provide a more immersive experience.
4. **Location Tracking (Optional)**: Add support for indoor positioning techniques (e.g., WiFi triangulation, Bluetooth beacons) to provide real-time tracking.
5. **Accessibility Improvements**: Add features to improve accessibility, such as voice guidance and larger font options.
6. **Admin Panel for Map Management**: Allow authorized users to upload and configure maps for different locations within the app.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**NavGO** - Indoor Navigation Made Easy

This README is intended to guide both developers and users in understanding and using NavGO effectively. Feel free to reach out if you have questions or feedback!
