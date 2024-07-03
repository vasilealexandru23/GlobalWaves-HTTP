Copyright 2024 Vasile Alexandru-Gabriel (vasilealexandru37@gmail.com)

# GlobalWaves - Audio Player x Http

## Design Patterns
1. `Singleton Pattern`   
This pattern is used in creating the `LibraryInput` with all the audio files and inital users.        
Because it remains the same throughout the program execution, the choice of using a Singleton pattern was obvious.

2. `Command Pattern`    
This pattern was used to encapsulate each command with its own class and execution method.      
It is used in `commands/` where we have a common inteface to implement and a super class for inheritance.

3. `Template Pattern`     
This pattern was used in the class `pages/UserPage`. Because in the next stage of project   
we might display more content of a user this pattern is convenient for later implementation of some methods.

4. `Strategy Pattern`   
This pattern was used in the class `recommendations/`. Because the platform can perform    
different types of recommendations for a specific user, to handle it easily, Strategy pattern was obvious.

5. `Observer Pattern`
This pattern was used in the class `notifications/`. Because subscriber notifications    
depend on the artist or host they've subscribed to, to easily update every notifications, Observer pattern was best for it.

## `Structure Of The Project ~com.globalwaves.httpserver`
  * `audiocollection/` - Directory that contains all types of tracks
    * `AudioCollection` - Template class that all tracks will respect in their structure
    * `Album` - Extended class that contains all fields an album can have and it's particular methods for player
    * `Song` - Extended class that contains all fields of a song from input and more particular fields & methods
    * `Playlist` - Extended class that contains all data for a playlist & particular methods for the requests of users
    * `Podcast` - Extended class that contains data for a podcast(Name, Owner Episodes) and and more particular fields & methods
    * `Episode` - Extended class that contains data for an episode(Name, Duration, Description) and more particular fields & methods
  * `commands/` - Directory that contains all possible commands a user can request from platform and their action
  * `config/` - Directory that contains the configuration manager and other related files for the `resources/http.json`
  * `core/` - Directory that contains the core of the project with the database class and server threads (connection and communication).
  * `fileio.input/`- Directory that contains basic classes for reading the commands and tracks from json format.
  * `musicplayer/` - Directory that contains the implementation for our player. 
    * `Playback` - Class that contains the implementation for our running playback in player.
    * `MusicPlayer` - Class that contains data for a player's musicplayer and their requests.
  * `pages/` - Directory that contains all types of pages implementation.
    * `UserPage` - General abstract class used to define all users involved in platform.
    * `HomePage` - Subclass that will always corespond to a normal user and some of his data.
    * `HostPage` - Subclass that will always corespond to a host and contains it's data.
    * `ArtistPage` - Subclass that will always corespond to an artist and contains it's data.
    * `LikedContentPage` - Subclass that will always corespond to a normal user. 
  * `parser/` - Directory that contains all the parsing methods of requests from the browser client (NOT FINISHED).
  * `searchbar/` - Directory that contains implementation for SearchBar commands (search track & select track).
    * `Search` - Contains general class for implementing the search command.
    * `Filters` - Contains all possible filters for a song, podcast, playlist or album.
    * `SearchSong` - Subclass which contains the implementation for searching a song with given filters.
    * `SearchHost` - Subclass which contains the implementation for searching a host with given filters.
    * `SearchAlbum` - Subclass which contains the implementation for searching an album with given filters.
    * `SearchArtist` - Subclass which contains the implementation for searching an artist with given filters.
    * `SearchPodcast` - Subclass which contains the implementation for searching a podcast with given filters.
    * `SearchPlaylist` - Subclass which contains the implementation for searching a playlist with given filters.
  * `users/` - Directory that contains all types of users.
    * `UserHost` - Subclass that defines a host with specific fields and methods.
    * `UserTypes` - General abstract class used to define all types of users.
    * `UserArtist` - Subclass that defines an artist with specific fields and methods.
    * `UserNormal` - Subclass that defines a normal user with specific fields and methods.
  * `HttpServer` - Where the execution starts (start of the server).
 
## Program Flow

The project uses for the interaction between clients and server the HTTP protocol.
This project uses the stage 2 of the audioplayer implemented here : *https://github.com/vasilealexandru23/GlobalWaves*.

**NOTE: For more details about implementation check JavaDoc.**
