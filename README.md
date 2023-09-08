# ToastedAFK

[![](https://jitpack.io/v/SerbanHiro/ToastedAFK.svg)](https://jitpack.io/#SerbanHiro/ToastedAFK)

# How to use

### Setting up ToastedAFK

<details>
  <summary>Setting up ToastedAFK with Maven</summary>
Open your project's pom.xml file.

Add the JitPack repository to your repositories section:
```xml
<repositories>
    <!-- Add the JitPack repository -->
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add ToastedAFK as a dependency:
```xml
<dependencies>
    <!-- Add ToastedAFK as a dependency -->
    <dependency>
        <groupId>com.github.SerbanHiro</groupId>
        <artifactId>ToastedAFK</artifactId>
        <version>VERSION</version> <!-- Replace with the desired version -->
    </dependency>
</dependencies>
```
</details>
<details>
<summary>Setting up ToastedAFK with Gradle</summary>
Open your project's build.gradle file.
  
Add the JitPack repository to your repositories block:
```groovy
repositories {
    // Add the JitPack repository
    maven { url 'https://jitpack.io' }
}
```
Add ToastedAFK as a dependency:
```groovy
dependencies {
    // Add ClickableHeads as a dependency
    implementation 'com.github.SerbanHiro:ToastedAFK:VERSION' // Replace with the desired version
}
```
</details>

### Retrieving stats from an AFK player

Getting the stats from an AFK player is straightforward. Here's an example of how to do it:
```java
import me.serbob.toastedafk.Classes.PlayerStats;

Player target=...
if(playerStats.get(player)==null) { return; } 
PlayerStats playerStatsPlayer = playerStats.get(player);

// After this you can retrieve the following things:

playerStatsPlayer.getDefaultAfkTime() --> this will return how many seconds the player has to wait in total
playerStatsPlayer.getAfkTimer() --> this will return how many seconds the player has left until he gets a reward
playerStatsPlayer.setAfkTimer() --> with this you can set the current seconds the player has left
playerStatsPlayer.getLevelTimer() --> this will return the xp levels (those lines ----)
playerStatsPlayer.getExpTimer() --> this will return the EXP
playerStatsPlayer.isXpEnabled() --> this returns whether the player has been using the xp feature
playerStatsPlayer.getTimeoutTimes() --> this will return how many times the player has received the rewards until he will be executed some timeout commands
playerStatsPlayer.setTimeoutTimes() --> this is how you can set how many times the player can receive rewards until some commands will be executed
```

### Events

There are 2 events, OnRegionEnteredEvent and OnRegionLeftEvent
```java
import me.serbob.toastedafk.API.Events.OnRegionEnteredEvent;
import me.serbob.toastedafk.API.Events.OnRegionLeftEvent;

// other classes

public class PlayerRegionHandler implements Listener {
    @EventHandler
    public void onRegionEnter(OnRegionEnteredEvent event) {
        Player player = event.getPlayer();
        // do something here
    }

    @EventHandler
    public void onRegionLeft(OnRegionLeftEvent event) {
        Player player = event.getPlayer();
        // do something here
    }
}
```
