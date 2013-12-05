package com.shinobicontrols.transitions.data;


import com.shinobicontrols.transitions.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sdavies on 05/12/2013.
 */
public class StoryContent {

    /**
     * Array of stories to display
     */
    public static List<StoryItem> STORIES = new ArrayList<StoryItem>();


    public static Map<String, StoryItem> STORY_MAP = new HashMap<String, StoryItem>();

    /**
     * Add a story to the datastores
     * @param story
     */
    public static void addStory(StoryItem story)
    {
        STORIES.add(story);
        STORY_MAP.put(story.id, story);
    }

    /**
     * Create some sample stories
     */
    static {
        addStory(new StoryItem("story1", "Story 1", R.string.sample_story_1_content, R.drawable.sample1));
        addStory(new StoryItem("story2", "Story 2", R.string.sample_story_2_content, R.drawable.sample2));
        addStory(new StoryItem("story3", "Story 3", R.string.sample_story_3_content, R.drawable.sample3));
        addStory(new StoryItem("story4", "Story 4", R.string.sample_story_4_content, R.drawable.sample4));
        addStory(new StoryItem("story5", "Story 5", R.string.sample_story_5_content, R.drawable.sample5));
    }

    /**
     * Class to represent the story items themselves
     */
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
}
