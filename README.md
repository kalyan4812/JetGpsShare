


# **Jet Gps Share**
<img src="https://www.seekpng.com/png/detail/11-114649_location-map-pin-icon-location-emoji-png.png"> Loaction Tracking App.


![Api Level](https://img.shields.io/badge/Min%20API%20Level-21-important)

Application is available here:

<a href="https://play.google.com/store/apps/details?id=com.saikalyandaroju.jetgpsshare"><img alt="Get it on Google Play" height="80" src="https://user-images.githubusercontent.com/68738102/126859366-c344986c-b527-4151-8d4c-e91c92eda716.png"></a>

Jet Gps Share is an android application makes you to find the location of your friends using their mobile number.All you need is to ask your friend to add you in his friends list. Indirectly you can access a person location only if he/she allows you.This makes our app legal.

### The following libraries/Tools were used in this project:-

 - [MapBoxSdk](https://docs.mapbox.com/android/maps/guides/)
 - [Room](https://developer.android.com/jetpack/androidx/releases/room)
 - [Picasso](https://github.com/square/picasso)
 - [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
 - [Volley](https://developer.android.com/training/volley)
 - [Lottie](https://lottiefiles.com/)
 - [Navigation component](https://developer.android.com/guide/navigation)

The app is developed using native android(Java) and PHP,MySql as backend. It is tightly coupled as it was developed as a project to learn Android Development.

### Splash and OnboardingScreens

Before you use the app,you have to accept the privacy policy,and auto start permission is optional for the app, it allows you to upadte your loaction even if you are not using the app.

<table>
        <tr>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126861392-84d45057-ccb1-4b1c-980b-7d4ab8a85838.png" ></td>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126861459-cb4d3b37-6715-40d5-9628-0b6eb9604bbb.png" ></td>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126861430-9a6a7200-eee0-4a94-a750-0c396d60882a.png" ></td>
        </tr>
</table>


### Authentication Screens

Signup uses Firebase OTP ,to verify the unique user,although there is a private backend to store users info.

<table>
        <tr>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126861514-a3b64eb0-d56a-4946-b496-8417122aff6e.png" height="560"></td>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126861538-d3bfaa40-ef4b-44ab-95fd-75878828425c.png" height="560"></td>
        </tr>
</table>


### Home Screen

* MapBox sdk is used to display the map and to draw the path between two points.You can get path between your and any random loaction by clikcing on the map.And other thing is you can find  a search button ,where you can find your friend using his/her name or number.
* You can share your/any destination location to anybody using share option in the app.
* Clicking on start navigation navigate to google navigation where you can get directions from your location to destination.

 <table>
        <tr>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126862037-e8fcc62a-f9f5-4f44-908f-0fd76164712a.png" ></td>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126862059-4176e1e0-65a0-4241-875f-db1616ba5212.png" ></td>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126862090-5e9ca828-436e-4d6c-a477-bdd8ab321f10.png" ></td>
        </tr>
       <tr>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126862178-1a57e3f3-ad4a-4013-a6b1-79d38c7d2fe3.png" ></td>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126862535-b7913d34-37bd-4e90-9db6-d585e2c2cf31.png" ></td>
        </tr>
</table>


### Add Friends(who can track your loaction)

* Room database is used to store user friends locally.Adding friends is made simple by accessing the persons from user contacts.If your friend is using the app you van directly
add him/her to your friendslist,other wise you have to invite him to use the app.
* You can users from your friends list whenever you want.

<img src = "https://user-images.githubusercontent.com/68738102/126862921-4c46d2dd-6f70-40be-965c-432058980c2a.png" width="300" height="600" align="center" >
        

### Set Activity Status

* Activity status should be enabled so that your friends can find your recent location.

* Whenver you want to stop upadting your loaction you can disable your activity status.

<img src = "https://user-images.githubusercontent.com/68738102/126863007-fb4614e6-3167-43a2-a6db-4c5c06f6cd29.png" width="300" height="600" align="center" >



### Profile Screen and Settings

* You can change the style of the map in the settings.
<table>
        <tr>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126863061-7a7e7fd3-5bf1-463e-8682-2eae9d756ea2.png" height="560"></td>
            <td><img src = "https://user-images.githubusercontent.com/68738102/126863299-fb48d124-8cba-4042-ae6f-caa748f12fcf.png" height="560" ></td>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126863334-74787515-35cb-4b30-a26f-ece5eea4a274.png" height="560" ></td>
        </tr>
</table>




### Remove Account and Logout.

* You can remove your account ,so that all your data will be deleted from database,.
<table>
        <tr>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126863160-6a161a05-f0da-4145-aba4-6e2d427a7308.png" height="560"></td>
            <td><img src = "https://user-images.githubusercontent.com/68738102/126862550-6e9ac469-23a0-4323-9f0c-edcc53220558.png" height="560" ></td>
        </tr>
</table>

## Maintainers and Developers
This repository is owned and maintained by 
 * [Sai Kalyan](https://github.com/kalyan4812)

