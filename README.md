# PremaCare 🚀

PremaCare is an Android application developed to help parents of premature babies track crucial health information. The app simplifies the process of logging key data such as feeding details (bottle, tube, or breastfeeding), weight, bowel movements, and other important health-related metrics. By providing an easy-to-use interface, PremaCare supports parents in managing and monitoring the health of their premature babies. 👶

## Features ✨

- **Feeding Tracking**: Parents can log the type of feeding (bottle, tube, or breastfeeding) and the quantity in milliliters using a simple slider.
- **Weight Tracking**: Keep track of the baby's weight over time.
- **Bowel Movements & Urination**: Track the baby's bowel movements and urination, ensuring essential health information is recorded.
- **Co-parent Registration**: A parent can register a co-parent by entering their login details, allowing both parents to share access to the child's health information.
- **User Authentication**: The app uses Firebase Authentication for secure login and registration of users.
- **Firebase Integration**: All data, including feeding records and user details, is securely stored in Firebase Firestore, ensuring that the data is synchronized across devices and available to both parents.

## Technologies Used 🛠️

- **Android Development**: The application is developed for Android using **Kotlin** and **Jetpack Compose** for building modern, responsive UIs.
- **Jetpack Libraries**: Utilizes **Jetpack** libraries such as:
  - **Jetpack Lifecycle** for managing UI-related data lifecycle and state.
  - **Jetpack Navigation** for managing app navigation in a composable-based environment.
  - **Jetpack Material3** for modern UI components.
  - **Jetpack LiveData** and **ViewModel** for managing UI-related data in a lifecycle-conscious way.
  - **Jetpack Core** and **Core KTX** for Kotlin extensions and utilities.
- **Firebase**: Integrated with **Firebase** for:
  - **Firebase Authentication** for secure user authentication and registration.
  - **Firebase Firestore** for cloud-based storage of user data and feeding records.
- **Lottie Animation**: **Lottie Compose** is used for rendering lightweight, high-quality vector animations to enhance the user interface.
- **Testing Libraries**: 
  - **JUnit** for unit testing.
  - **Espresso** for UI testing and interaction.
  - **Jetpack Test libraries** for testing composables and UI components.

### Libraries Included:
- **Jetpack Compose**: For building modern UIs in a declarative style.
- **Firebase SDKs**: Firebase Authentication and Firestore SDKs for handling user authentication and data storage.
- **Lottie Compose**: For integrating vector-based animations with Lottie.
- **Material Design Components**: For creating Material Design-inspired UI components with the latest Android libraries.

## Setup 🔧

1. **Clone the repository**:
   Open your terminal or Git Bash, then clone the repository with the following command:

   ```bash
   git clone <repository-url>
2.	Open the project in Android Studio:
Navigate to the cloned project folder, then open it in Android Studio.
	3.	Sync the project with Firebase:
	•	Go to the Firebase Setup Documentation.
	•	Follow the instructions to link your app with Firebase and set up Firebase Authentication and Firestore.
	4.	Build and run the app:
Once everything is set up and synced with Firebase, click “Build” in Android Studio to compile the app. Then, run it on either an emulator or a physical device.

## Usage 📱
	•	After logging in, parents can start logging their baby’s health data.
	•	Feeding details, weight, and bowel movements can be easily tracked using the provided interfaces.
	•	Co-parents can be added by entering their login credentials.
