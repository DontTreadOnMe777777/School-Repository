Alysha Armstrong & Brandon Walters

PS8:
Howdy, and thanks for reading our README! There are a few distinctions between our program and the example client provided to us. 
The first is the use of a gray background instead of black, as we felt that it matched better between the buttons/fields at the top and the game view at the bottom.
The next major difference is that once connected to the server, opening the Help or About menus will hide the game view. 
This was done to combat an issue we were experiencing with the messages being hidden behind the main form, giving the impression that the client had frozen.
The last major difference of note is the use of a static image instead of an animated explosion upon tank death. This was an artistic decision,
as we felt that a picture of a ruined tank like the ones in the game was more interesting visually than a simple animation.
Other than these differences, our client runs the game the same as the example client, with only slight visual differences and changes.
Below is a list of some of our achievements throughout this development process. Thanks for reading!

PS9:
Howdy, and thanks for reading our README again! Our server is much more like the sample server provided to us this itme around, minus some functionality. Unfortunately, 
due to other work, we were not able to get the time we would have liked to be able to work on this project and make it the best it could be. Our program has some bugs that can affect player 
enjoyment and gameplay. These include some issues with the spawning system, the beam system, and the collision system. We were also not able to get the database system fully working, 
due to a lack of time once we had gotten to a point where we believed that the server was nearing completion. We did manage to get the games table working properly, however the players table still has some issues. 
Unfortunately, this means that we have to turn in a product
that neither of us are happy with in any way. We apologize for any bugs you may encounter in the testing of our program, and we hope you understand our frustration that we were not able
to produce the server that we wanted to. Thanks for reading.

11/19/19
Remove objects if the died flag set to true. Do not add objects if their died flag is set to true. Recieving and 
processing the world messages updated, trigger UpdateArrived event after new objects from the message added to the world. 

11/18/19 
Begun trying to recieve world messages from the server. Right now, continuously call the method to check for messages
(but this should somehow be triggered by an event loop). Once a message is recieved, another method checks 
what object the JSON message is holding and adds the new object to the world. Changed how the intial hand shake 
is processed because the tank with the players ID is sent from the server later. Therefore, there is no need 
to create a tank at the time of the handshake. As of now, just set the idRecieved flag to true when the first messge is 
recieved and set the world size and handshakecomplete flag to true when the second message is recieved.
There may be a better way to do this too.

11/17/19 
Got the client to connect to the server, with the correct player name. Can process the setup data and update the world
with the new tank (with name and id) and set the world size. May need to change how we are processing the information
for the setup. Should the same method work for processing the setup data and processing the world data? Should processing 
the world data be in a for loop? How do we know when the server sends more information? Do we need an event? 

11/15/19
Added the buttons and text boxes to the form app. Added properties with JSON label to 
classes in ModelProjects. Set up World with dictionaries for the game objects and will make methods 
to add to each. Started working on connecting to the server and writing the delegates for OnNetworkAction. 
Should the GameObjects be private classes or in a different namespace?

11/12/19
Created the .NET Standard project ModelProjects to contain a class for the World and the objects in it, 
the .NET Standard project GameController to contain controller code, and the Windows Form App (.NET Framework)
TankWarsGUI to contain the view code. Some classes for ModelProjects have been made, but have not been implemented. 
These may not be all the required classes in this project and some of them may be unecessary, but provide a starting
point. The Vector2D class contains the Vector2D.cs code provided. A reference to the JSON library was created for these
projects. 
In the Resources project, a Library folder was made to contain necessary .dll and .xml files. It has been set to not build.
So far, only those for the NetworkController have been copied in. Not sure if there are others required yet. NetworkController.dll 
is the solution provided in TankWarsLibraries (may change to ourPS7 solution after the grading tests come out) and the .xml came
from PS7. The images used in TankWarsLibraries have been added to this project in the Images folder. 

