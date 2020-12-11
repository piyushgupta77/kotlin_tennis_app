# 2020-DEV-095

![](media/tennis_app.webm "Application Recording")

Tennis 'Coding Kata' application
------------------------------

This application simulates tennis game between 2 players. It is created on MVVM architecture using Android architectural components. It uses
1. Google Hilt - for Dependency Injection
2. Circle Ci - for continuos integration
3. Visual studio app center - for continuos delivery of Android APK

Github configuration
--------------------
Any user cannot directly push to develop or master branches. develop branch will be treated as base branch. develop and master branches are made protected. User has to create a pull request with their new code changes and point it to develop as base branch. PR merging will require following conditions to pass
1. Unit test and build creation task to complete successfully
2. Atleast 1 reviwer to approve pull request

In absence of any of above checks user will not be able to merge pull request

Circle CI usage
---------------
Circle CI is set to build code on every new pull request creation on Github repository. Also CircleCI will run same checks when a pull request is merged to default branch develop. Upon successfull completion on above 2 checks CircleCI will deploy created application to Visual Studio appcenter.

Visual studio appcenter usage
-----------------------------
To ease download of application, there is a temporary option added to download the application publicly without login to appcenter. Anyone can download 
application from public url 
https://install.appcenter.ms/users/coolpiyush777/apps/tennis_app/distribution_groups/app%20testers

Running application
-------------------
To run application download application from above public URL and use following command. 
	adb install <Complete-path-to-application-apk>
This command will need Android adb to be installed on your machine. To know steps to install adb please follow link https://developer.android.com/studio/command-line/adb
