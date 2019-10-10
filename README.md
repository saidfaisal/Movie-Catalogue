---
Platforms: Android (Java)
Author: saidfaisal
---

# Movie-Catalogue
This repository contains a final project "Menjadi Android Developer Expert (MADE)" of Dicoding.

## Prerequisites
To get an API Key, please follow the tutorial on the following link:
https://blog.dicoding.com/registrasi-testing-themoviedb-api/

## Resources
* Using the following endpoint to get Movies data:
https://api.themoviedb.org/3/discover/movie?api_key={API KEY}&language=en-US
* Using the following endpoint to get Tv Shows data:
https://api.themoviedb.org/3/discover/tv?api_key={API KEY}&language=en-US
* Using the following endpoint to get Movie poster:
https://image.tmdb.org/t/p/{POSTER SIZE}{POSTER FILENAME}
* Using the following endpoint to get Release Today Movie data:
https://api.themoviedb.org/3/discover/movie?api_key={API KEY}&primary_release_date.gte={TODAY DATE}&primary_release_date.lte={TODAY DATE}

POSTER SIZE above is the size of the poster you want to get. There are several sizes that can be used w92, w154, w185, w342, w500, w780, and original. Whereas POSTER FILENAME is the poster path that can be obtained from the JSON response with the poster_path key.

## Features
App has some features:
* There are 2 (two) pages that display a list of shows (Movie and Tv Show).
* Using Fragment to hold Movie and Tv Show pages.
* Using RecyclerView to display movie list.
* Using BottomNavigationView, TabLayout as navigation between Movie and Tv Show pages.
* Displaying the loading indicator when data is being loaded.
* Displaying poster and movie information on the detail page of film.
* Using ConstraintLayout to arrange layouts.
* Able to save shows to favorite database.
* Able to delete shows of favorite database.
* Applications must support Indonesian and English language.
* Users are able to search Movies.
* Users are able to search Tv Shows.
* Users are able to search Movie/ Tv Show Favorites.
* Users can display stack widget on the main page of the smartphone from Movie/ Tv Show Favorites.
* The app has Daily Reminder, sending notifications to users to return to the Movie Catalogue app. Daily reminder always run every 7 o'clock in the morning.
* The app has Release Today Reminder, sending notifications to users of all films released today using Stack Notification with Inbox Style. Release reminder must always run at 8 o'clock in the morning.
* There is a setting page for activating and deactivating the reminder.
* Using Content Provider as a mechanism to access data from one application to another.
