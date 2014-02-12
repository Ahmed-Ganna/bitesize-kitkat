# Android KitKat: finger-by-finger 

## Full screen apps in KitKat

### Introduction

Full-screen apps allow the user to get fully engrossed with whatever content they
are viewing or interacting with. Examples include e-book readers, video players,
games and drawing apps. Before KitKat, although there was support for a 
fullscreen mode, KitKat introduces 2 new immersive modes.

In this post we'll take a look at 3 different full-screen modes, and discuss
times when you might want to use them. There's an accompanying app which
demonstrates how to use the different modes and what they might be used for. The
source code is available on Github GITHUB_LINK_HERE, and is a gradle project which
has been tested on Android Studio 0.4.4.

### The different full-screen experiences

We're going to take a look at 3 different full-screen modes:

- __Leanback__ This is the mode that was available pre-KitKat and is suited to
video players.
- __Immersive__ New to KitKat. Suitable for ???
- __Sticky Immersive__ New to KitKat. Suitable for ???

First up, leanback mode.

#### Leanback

In leanback mode, the chrome such as the status bar, the activity bar and any
UI controls will disappear after a period without user interaction. Whilst they
are not visible then any interaction with the screen will cause them to reappear,
and the touches won't be passed on to the underlying view. This means that any
user interaction can only occur whilst the UI chrome is visible. This behavior
is exactly the behavior you might expect from a video player - the user doesn't
need to be able to interact with the content, instead they will expect some
controls to appear when they touch the screen.




#### Immersive mode

#### Sticky Immersive mode


### Conclusion
