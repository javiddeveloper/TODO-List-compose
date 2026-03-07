# TODO List App with Jetpack Compose & Clean Architecture

A modern Android application demonstrating **Clean Architecture**, **MVVM**, and **Jetpack Compose**. This project serves as a showcase for building robust, scalable, and maintainable Android apps using the latest tools and libraries.

It features a local Todo list with **Paging 3** for efficient list handling and **Room** for local persistence, ensuring a smooth user experience even with large datasets.

<a href="https://github.com/javiddeveloper/TODO-List-compose/blob/master/app-debug.apk">Download APK File for review</a>

## Screenshots

<table>
  <tr>
    <td><img src="1.jpg" alt="Home Screen" width="200"/></td>
    <td><img src="2.jpg" alt="Detail Screen" width="200"/></td>
    <td><img src="3.jpg" alt="Add Task" width="200"/></td>
    <td><img src="4.jpg" alt="Delete Task" width="200"/></td>
    <td><img src="6.jpg" alt="Empty State" width="200"/></td>
  </tr>
</table>

## Key Features

- **Clean Architecture**: Clear separation of concerns into **Data**, **Domain**, and **UI** layers.
- **Modern UI**: Built entirely with **Jetpack Compose**.
- **Efficient List Handling**: Implements **Paging 3** to load data efficiently from the local database.
- **Local Persistence**: Uses **Room Database** for storing tasks offline.
- **Dependency Injection**: Powered by **Hilt** for managing dependencies.
- **Reactive Programming**: Extensive use of **Coroutines** and **Flow** for asynchronous operations.
- **MVI Pattern**: Unidirectional data flow in the UI layer for predictable state management.

## Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: Clean Architecture + MVVM + MVI
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Pagination**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3)
- **Asynchronous**: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Navigation**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

## Project Structure

The project follows the principles of Clean Architecture:

- **`data`**: Contains the implementation of repositories, database (Room), and data sources.
- **`domain`**: Contains the business logic, including **UseCases** and **Repository Interfaces**. This layer is pure Kotlin and independent of Android frameworks.
- **`ui` (presentation)**: Contains the UI components (Screens, ViewModels) built with Jetpack Compose.

## Getting Started

To run this project, you'll need Android Studio Arctic Fox or newer.

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/javiddeveloper/TODO-List-compose.git
    ```
2.  **Open in Android Studio**:
    Open the project directory in Android Studio.
3.  **Build and Run**:
    Wait for Gradle sync to complete, then run the app on an emulator or physical device.

## License

This project is open-source and available under the MIT License.
