# 💰 Currency Converter

A simple Android app for converting currencies using real-time exchange rates. This app fetches data from an exchange rate API, allowing users to convert between different currencies and view supported currency codes.

## ✨ Features

- **Currency Conversion**: Converts an amount of money from one currency to another using the latest exchange rates.
- **Supported Currencies**: Displays a list of supported currencies and their codes.
- **Offline Support**: Caches the conversion data locally using Room database, allowing offline usage of previously fetched data.
- **Clean Architecture**: Implements a repository pattern for data fetching, with ViewModel  and State/Flow for UI updates.
- **Edge-to-Edge UI**: Utilizes modern Android UI principles, offering an immersive full-screen experience.

## 🛠️ Technologies Used

- **Kotlin**: Programming language.
- **Jetpack Compose**: For building the UI.
- **Room**: For local data storage.
- **Ktor**: For making HTTP requests to the exchange rate API.
- **Coroutines**: For managing background tasks.
- **MVVM (Model-View-ViewModel)**: For separation of concerns.

## 📦 Project Structure

```
- data/
  - api/
    - ApiEndpoints.kt          // Defines API endpoints for currency conversion
    - ConversionResultDao.kt   // DAO for conversion results
    - CurrencyCodeDao.kt       // DAO for currency codes
    - DefaultKlient.kt         // Default Ktor HTTP client implementation
    - Klient.kt                // Interface for HTTP client
  - db/
    - CurrencyConverterDatabase.kt // Room database setup
    - CurrencyConverterTransaction.kt // Handles database transactions
  - model/
    - CurrencyEntities.kt      // Defines data entities for the database
  - repository/
    - Repository.kt            // Handles data access from API and DB
- ui/
  - activity/
    - MainActivity.kt          // Main activity of the application
  - theme/
    // Contains UI theme configurations
  - viewmodel/
    - CurrencyConversionViewModel.kt // ViewModel for currency conversion logic
- util/
  - ConversionRatesConverter.kt // Converter for handling conversion rates
  - CurrencyCodeListSerializer.kt // Serializer for currency code lists
  - networkBoundResource.kt    // Handles data fetching and caching logic
  - Resource.kt              // Wrapper for resource state (loading)
```

## 🚀 Setup

### Prerequisites

- Android Studio with the latest version installed.
- Kotlin 1.5+.
- An API key from [ExchangeRate-API](https://www.exchangerate-api.com/) (for currency conversion).

### Steps to Get Started

1. **Clone the Repository**:

   ```bash
   git clone [https://github.com/yourusername/currency-converter.git](https://github.com/yourusername/currency-converter.git)
   cd currency-converter
