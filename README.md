# Squeeze My Brain

## Overview

Squeeze My Brain is an Android app built with Jetpack Compose that lets users test their knowledge with various quiz questions.
The app fetches quiz questions from an external API and stores them locally using Room database for offline access.
It also provides different difficulty levels for users to choose from.

## Demo Video

![Watch the video]((https://drive.google.com/file/d/1SG1VuAnEZnlFWw-aWGeuLyGfMM7XDRVq/view?usp=drive_link
)

## Technologies Used

- **Jetpack Compose**: Jetpack Compose is used for building the app's UI in a declarative and modern way.
- **Kotlin**: The entire app is written in Kotlin, the official language for Android development.
- **Android Architecture Components**: Components like ViewModel, LiveData, and Room are used for data management and database operations.
- **Retrofit**: Retrofit is used to make API calls and fetch quiz questions from the server.
- **Room Database**: Room is used to store quiz questions locally for offline access.
- **Coroutines**: Coroutines are used for asynchronous and non-blocking programming.
- **Material Design Components**: Material Components are used for creating a beautiful and consistent UI.
- **Navigation Component**: Jetpack Navigation Component is used for handling app navigation.

## Getting Started

1. Clone the repository: `git clone https://github.com/DK-UK/Squeeze-My-Brain.git`
2. Open the project in Android Studio.
3. Run the app on an Android emulator or physical device.

## API Used

The app fetches quiz questions from the [TRIVIA API]([https://api.example.com/questions](https://opentdb.com/api_config.php)) to provide quiz content.
The API returns questions in JSON format, which are then parsed and stored locally in the app's database for offline access.

## Contributing

Contributions are welcome! If you find any bugs, have suggestions, or want to add new features, feel free to open an issue or submit a pull request.

<!-- ## License

This project is licensed under the [MIT License](LICENSE).
-->
## Acknowledgments

Special thanks to [API Provider]([https://api.example.com](https://opentdb.com/api_config.php)) for providing the quiz questions API.

---

Happy quizzing! If you have any questions or feedback, feel free to contact us.

