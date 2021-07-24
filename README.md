


# **Jet Gps Share**

⚡️ Loaction Tracking App.


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
          <td><img src = "https://user-images.githubusercontent.com/68738102/126861514-a3b64eb0-d56a-4946-b496-8417122aff6e.png" ></td>
          <td><img src = "https://user-images.githubusercontent.com/68738102/126861538-d3bfaa40-ef4b-44ab-95fd-75878828425c.png" ></td>
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
          <td><img src = "https://user-images.githubusercontent.com/68738102/126862550-6e9ac469-23a0-4323-9f0c-edcc53220558.png" ></td>
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
        

### Attendence Manager
The app has an integrated **Attendance Manager** which uses Room Library and SQLite Database to store and retrieve data and display it in a graphical manner which looks appealing and convenient to track your attendance.
  
<img src = "/Images/attendance_light.PNG" width="235" height="500"/>

### Notes  


The Notes tab provides notes uploaded by students. The recycler view shows the name of the author with a total number of downloads and relevant tags.




c = "/Images/download_notes_light.PNG" width="235" height="500"/>

#### _Upload Notes and My Files_  

ad notes area provide for easy upload of notes by users. It takes the authors name to give credit to the author.

The number of downloads will encourage students to make more impressive notes and create a sense of healthy competition.

The My Files section enables easy management of the notes uploaded by the users as well as the notes downloaded by the user.

<img src = "/Images/notes_light.PNG" width="235" height="500"/>  <img src = "/Images/myfiles_light.PNG" width="235" height="500"/>


### Timetable
This feature allows the user to store their timetable in an ingenious and orderly fashion. It also indicates the current ongoing class.
It requires the user to enter the class details consisting of subject name, start time, end time and the room number. We're constantly working on to make this feature more interactive and seamless.

<img src = "/Images/timetable_light.PNG" width="235" height="500"/>

### Tools
 * _Room Locator_ <br>
The room locator tab implements the room locator build by the DSC BVP team. Currently, it only works for BVCOE, New Delhi but we plan to expand it using the help of a wide network of DSCs present all over India  
 * _Upcoming Events_ <br>
It shows the upcoming events around you and their details along with an option to register from within the application.
 * _Project Collaboration_ <br>
This tool help to procreate a platform where different developers can come and work together on any projects. Just list your project and get collaborators. This feature is currently under development.

<img src = "/Images/tools_light.PNG" width="235" height="500"/>

### Settings
 * View and edit personal profile details  
 * View downloaded files  
 * View uploaded files
 * View and edit professional profile  

<img src = "/Images/settings_light.PNG" width="235" height="500"/>

## Contributions Best Practices


### For first time Contributors

First-time contributors can read [CONTRIBUTING.md](/CONTRIBUTING.md) file for help regarding creating issues and sending pull requests.

### Branch Policy

We have the following branches

 * **development**<br>All development goes on in this branch. If you're making a contribution, you are supposed to make a pull request to _development_. For PRs to be accepted to the development branch they must pass a build check and a unit-test check following which an apk will be generated under [action artifacts](https://github.com/sakshampruthi/CollegeConnect/actions) for testing.
 * **master**<br>This contains shipped code. After significant features/bugfixes are accumulated on development, we make a version update and make a release.
 	
### Code practices

Please help us follow the best practices to make it easy for the reviewer as well as the contributor. We want to focus on the code quality more than on managing pull request ethics.

 * Single commit per pull request
 * Reference the issue numbers in the commit message. Follow the pattern Fixes #<issue number> <commit message>
 * Follow uniform design practices. The design language must be consistent throughout the app.
 * The pull request will not get merged until and unless the commits are squashed. In case there are multiple commits on the PR, the commit author needs to squash them and not the maintainers cherrypicking and merging squashes.
 * If the PR is related to any front end change, please attach relevant screenshots in the pull request description.

### Join the development

* Before you join the development, please set up the project on your local machine, run it and go through the application completely. Press on any button you can find and see where it leads to. Explore. (Don't worry ... Nothing will happen to the app or to you due to the exploring :wink: You'll be more familiar with what is where and might even get some cool ideas on how to improve various aspects of the app.)
* If you would like to work on an issue, drop in a comment at the issue. If it is already assigned to someone, but there is no sign of any work being done, please free to drop in a comment so that the issue can be assigned to you if the previous assignee has dropped it entirely.

## License

This project is currently licensed under the Apache License Version 2.0. A copy of [LICENSE](LICENSE) should be present along with the source code.

## Maintainers and Developers
This repository is owned and maintained by 
 * [Sajal Jain](https://github.com/sjain30)
 * [Saksham Pruthi](https://github.com/sakshampruthi)
