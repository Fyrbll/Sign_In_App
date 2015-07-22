# Sign_In_App
This is an app implements a one-time sign-in using the Citrus SDK.
It uses the AccountManager class and creates an Authenticator for Citrus accounts.

When the app is opened, the user sees two buttons: Login and Get Authtoken.
'Login' stores the details of a certain Citrus account (the username and password for which are in Authenticator.java for now)
on the device. 
'Get Authtoken' requests the server for an auth token using a Citrus account on the phone (currently the oldest Citrus account),
and displays it using a Toast.

This app is actually made to be used with another, similar app (i.e. an app using the same authenticator)
on the device, so that the user will be able to get a different auth token for each app. An example of such an app is
Sign_In_App_Two, which uses a different set of merchant credentials with essentially the same code as Sign_In_App and gets
a different auth token from Sign_In_App.

Possible improvements:
1. Using EditText objects in the layout, allowing the app's user to enter his/her own Citrus account details
2. Clearing the stored password for the Citrus account after a certain number of days of inactivity
3. Encrypting the Citrus password in some way before storing it on the device
4. Supporting multiple Citrus accounts by introducing a layout object that allows a user to choose one Citrus account
  from all the Citrus accounts on the device.
