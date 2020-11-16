# BaseEngine

A complete rewrite of the game engine myself and [David S.](https://github.com/Daves1245) wrote around 2016.

The basic main features of this engine are:
* Java Swing based windows using Graphics2D objects for primitive/image drawing
* Variable or locked frame rate that will ensure 60 UPS (Updates per second)
* Engine/Game separation
    * The engine is completely independent of any game
    * The engine provides an API that the game implements and uses
    * Any games are scanned for at runtime, and are expected to follow the API
    * This allows for one engine install to support many games with (possibly) fast switching

## Status

Currently, this Engine is deprecated, and will probably no longer receive updates or improvements. This is mostly due to Java 14's (and some of Java 1.8 U262's) lack of ability to disable/prevent application side scaling of Swing user space, preventing the Engine from properly being able to take input and display to any high DPI screen that has system scaling enabled. For example, on a 3840x2160 (4k) monitor with Windows 10's screen scalling set to 200%, Java will recognize mouse input at the bottom-right edge of the screen as pixel cord (1919, 1079), instead of the expected (3839, 2159). This completely breaks how the engine is designed and meant to function.

This Engine will be superseded by a 3D based engine (rather than 2D) that uses LWJGL and OpenGL, which will allow greater functionality and game quality. It has not yet been decided which core features of this engine will be transferred over, if at all. However, this engine *may* at some point receive updates in the future, no longer rendering it deprecated.