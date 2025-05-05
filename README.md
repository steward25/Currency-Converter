# Currency Converter

A simple Android app for converting currencies using real-time exchange rates. This app fetches data from an exchange rate API, allowing users to convert between different currencies and view supported currency codes.

## Features

- **Currency Conversion**: Converts an amount of money from one currency to another using the latest exchange rates.
- **Supported Currencies**: Displays a list of supported currencies and their codes.
- **Offline Support**: Caches the conversion data locally using Room database, allowing offline usage of previously fetched data.
- **Clean Architecture**: Implements a repository pattern for data fetching, with ViewModel and LiveData for UI updates.
- **Edge-to-Edge UI**: Utilizes modern Android UI principles, offering an immersive full-screen experience.

## Technologies Used

- **Kotlin**: Programming language.
- **Jetpack Compose**: For building the UI.
- **Room**: For local data storage.
- **Ktor**: For making HTTP requests to the exchange rate API.
- **Coroutines**: For managing background tasks.
- **MVVM (Model-View-ViewModel)**: For separation of concerns.

## Setup

### Prerequisites

- Android Studio with the latest version installed.
- Kotlin 1.5+.
- An API key from [ExchangeRate-API](https://www.exchangerate-api.com/) (for currency conversion).

### Steps to Get Started

1. **Clone the Repository**:

    ```bash
    git clone https://github.com/yourusername/currency-converter.git
    cd currency-converter
    ```

2. **Open the Project in Android Studio**:
    - Launch Android Studio and open the project.

3. **Add Your API Key**:
    - Replace the placeholder API key in the `ApiEndpoints` class with your own ExchangeRate-API key.

4. **Build and Run the App**:
    - Select a device or emulator, then click on the Run button.

### Dependencies

- `Ktor`: For network operations.
- `Room`: For local database management.
- `Jetpack Compose`: For the UI components.
- `Kotlinx Serialization`: For JSON parsing.

To install all dependencies, run:

```bash
./gradlew build
