# Bitesize Android KitKat

## Easy animation with the Transitions framework

### Introduction

One of the most exciting additions in Android KitKat is the new transitions
framework, which enables complex animations to be easily created, and specified
in a declarative manner.

In this final installment of the Bitesize Android KitKat series, we'll take a
look at the constituent parts of the transitions framework, and learn how to
build a sample app which uses custom transitions.

The code is available in github at
[github.com/ShinobiControls/bitesize-kitkat](https://github.com/ShinobiControls/bitesize-kitkat)
- feel free to grab it and give it a try. It'd be great to see a pull request
with any fixes if you find any issues. The code has been tested on Android
Studio 0.5.1. Versions < 0.5 don't have support for the XML transition
resources.



### Scenes and the TransitionManager

The fundamental concept of the Transition framework is that the current state
of a view (i.e. the UI) is captured in a `Scene` object, and then `Transition`
objects determine animations between these scenes. A `TransitionManager` is
used to run the transitions, and maintains the ruleset of which transitions
should be used when moving between particular scenes.

A `Scene` is constructed from a `ViewGroup`, so let's create a couple of layout
resources which we'll use for the two scenes. The first is a simple
`LinearLayout` which contains an image, a title label and a larger content text
view:

  <LinearLayout
      xmlns:android="http://schemas.android.com/apk/res/android"
      android:orientation="vertical"
      android:layout_width="fill_parent"
      android:layout_height="wrap_content"
      android:id="@+id/linearLayout" >
      <ImageView
          android:layout_width="300dp"
          android:layout_height="300dp"
          android:id="@+id/story_image"
          android:layout_gravity="center_horizontal"
          android:src="@drawable/sample1"/>
      <TextView
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:textAppearance="?android:attr/textAppearanceLarge"
          android:text="Title Text"
          android:id="@+id/story_title" />
      <TextView
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:id="@+id/story_content"
          android:text="Content Text" />
  </LinearLayout> 

And the second has just an image and the same text view for content:

  <LinearLayout
      xmlns:android="http://schemas.android.com/apk/res/android"
      android:orientation="vertical"
      android:layout_width="fill_parent"
      android:layout_height="wrap_content"
      android:id="@+id/linearLayout" >
      <ImageView
          android:layout_width="fill_parent"
          android:layout_height="150dp"
          android:scaleType="centerCrop"
          android:id="@+id/story_image"
          android:layout_gravity="center_horizontal"
          android:src="@drawable/sample1"/>
      <TextView
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:id="@+id/story_content"
          android:text="Content Text"
          android:textAlignment="center" />
  </LinearLayout>

Notice that the ids match between the two layouts. The transition framework
will automatically animate views which are present in both scenes, and these
are identified via their ID.

We can now create `Scene` objects from these layouts using the static
`Scene.getSceneForLayout()` method. It's worth mentioning that this method
caches scenes, which will be useful later on when using XML to declare
transitions.

The following code creates an `ArrayList` of `Scene` objects:

    // Get hold of some relevant content
    final ViewGroup container = (ViewGroup)findViewById(R.id.container);

    // What are the layouts we should be able to transition between
    List<Integer> sceneLayouts = Arrays.asList(R.layout.content_scene_00,
                                               R.layout.content_scene_01);
    // Create the scenes
    sceneList = new ArrayList<Scene>();
    for(int layout : sceneLayouts) {
        // Create the scene
        Scene scene = Scene.getSceneForLayout(container, layout, this);
        // Save the scene into
        sceneList.add(scene);
    }

The `getSceneForLayout()` method takes 3 arguments:

- `sceneRoot` is a `ViewGroup` which represents the container within which all
the transitions will occur.
- `layoutId` is the ID of the layout from which the `Scene` will be created
- `context` is a `Context`, which will be used to get hold of a
`LayoutInflator` to inflate the aforementioned layout.


The `TransitionManager` is used to actually perform the transition:

    TransitionManager.go(sceneList.get(tab.getPosition()));

Here the static `go()` method is being used. It has one argument, and it takes
a scene object. We're pulling the scene out of the `ArrayList` we made
previously.

If you run the app up in this state, and flip between the tabs then you'll see
that moving between tabs involves the content automatically animating:

PICTURE OF SIMPLE CASE HERE

You haven't specified anything about how the transitions should animate yet,
and in this case then the `TransitionManager` will use the default
`AutoTransition`. We'll take a look at building custom transitions in the next
section, but not before we've addressed the fact that there is a noticeable
lack of content.


#### Adding content

You'll notice here that the layout has been re-inflated, but there has been no
chance to add any content. You could add the content into the layout xml file,
but this isn't a very sensible approach - a layout should be content
independent, both for reuse reasons, and internationalization. Therefore, we've
defined the following strings in __values/strings.xml__:

    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        ...
        <string name="sample_story_1_content">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus et leo pulvinar, egestas est sit amet, fringilla nisl....</string>
        <string name="sample_story_2_content">Suspendisse sapien metus, ornare ac metus a, ultrices semper risus. Suspendisse auctor adipiscing tortor. Nunc luctus,...</string>
        ...
    </resources>

And have added images __sample1.jpeg__, __sample2.jpeg__ etc to the appropriate
resources directory.

The following method will apply the appropriate content to the `ViewGroup`
created from the layout:

    private void addContentToViewGroup(ViewGroup viewGroup)
    {
        if (mItem != null) {
            TextView contentTextView = (TextView) viewGroup.findViewById(R.id.story_content);
            if(contentTextView != null) {
                contentTextView.setText(getResources().getText(mItem.contentResourceId));
            }
            TextView titleTextView = (TextView) viewGroup.findViewById(R.id.story_title);
            if(titleTextView != null) {
                titleTextView.setText(mItem.title);
            }
            ImageView imageView = (ImageView) viewGroup.findViewById(R.id.story_image);
            if(imageView != null) {
                imageView.setImageResource(mItem.imageResourceId);
            }
        }
    }

where `mItem` is an instance of `StoryItem`:

    public static class StoryItem {
        public String id;
        public String title;
        public int contentResourceId;
        public int imageResourceId;

        public StoryItem(String id, String title, int contentResourceId, int imageResourceId)
        {
            this.id = id;
            this.title = title;
            this.contentResourceId = contentResourceId;
            this.imageResourceId = imageResourceId;
        }

        @Override
        public String toString() {
            return this.title;
        }
    }

This is a very standard android pattern for finding views and setting their
content dynamically.

In order to insert the content into the layout as it is used by the transition
manager, we make use of a property on the `Scene` object - `EnterAction`. This
is a `Runnable` which will be run after the layout has been inflated, but
before the transition is performed - ideal for loading content.

    // Just before the transition starts, ensure that the content has been loaded
    scene.setEnterAction(new Runnable() {
                             @Override
                             public void run() {
                                 addContentToViewGroup(container);
                             }
                         });

Adding this to the loop in which we create the `Scene` objects will ensure that
the content is loaded before the transition takes place, and if you tun the app
up now you'll see that the content is fully loaded:

PICTURE OF CONTENT LOADED


### Custom transitions with TransitionSets

Currently, the transition is entirely automatic - we haven't specified anything
about how the transition should animate, which is obviously something we would
like to have control over.

To start out we'll re-create the `AutoTransition` so that you can see what it
is constructed from:

    private void performTransitionToScene(Scene scene) {
        Fade fadeOut = new Fade(Fade.OUT);
        ChangeBounds changeBounds = new ChangeBounds();
        Fade fadeIn = new Fade(Fade.IN);
        
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        transitionSet.addTransition(fadeOut)
                .addTransition(changeBounds)
                .addTransition(fadeIn);
        
        TransitionManager.go(scene, transitionSet);
    }

This method will transition from the current scene to the new one, using a
`TransitionSet` which comprises fade-out, bounds change and fade-in
transitions. A transition set is an ordered collection of transitions, and you
can set whether the transitions should occur simultaneously or sequentially
using the `setOrdering()` method. In order to recreate the `AutoTransition`, we
set that the ordering should be sequential.

The `go()` static method on `TransitionManager` can take two arguments - where,
in addition to the `Scene`, a `Transition` can be supplied as well.

In order to actually use this new method replace:

    TransitionManager.go(sceneList.get(tab.getPosition()));

with:

    performTransitionToScene(sceneList.get(tab.getPosition()));


If you run the app up now, then you shouldn't notice any difference - we've
just recreated the existing automatic transition ourselves. The advantage of
doing this is that you now have a lot more control over the transition itself.
For example, we can update the transition method to set a duration on the
individual segments of the transition:

    private void performTransitionToScene(Scene scene) {
        Fade fadeOut = new Fade(Fade.OUT);
        ChangeBounds changeBounds = new ChangeBounds();
        Fade fadeIn = new Fade(Fade.IN);

        fadeOut.setDuration(1000);
        changeBounds.setDuration(1000);
        fadeIn.setDuration(1000);

        changeBounds.setInterpolator(new BounceInterpolator());

        TransitionSet transitionSet = new TransitionSet();
        transitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        transitionSet.addTransition(fadeOut)
                     .addTransition(changeBounds)
                     .addTransition(fadeIn);

        TransitionManager.go(scene, transitionSet);
    }

The code snippet also introduces the concept of an `Interpolator`. This is an
object which maps the temporal dimension of the transition into the spatial
domain. There are a selection of different interpolators provided as part of
the framework, including `AccelerateDecelerateInterpolator` and
`AnticipateOvershootInterpolator`; here we're using a `BounceInterpolator`:

IMAGE DEMONSTRATING THE BOUNCE INTERPOLATOR


### Using XML resources for transitions


### Conclusion
