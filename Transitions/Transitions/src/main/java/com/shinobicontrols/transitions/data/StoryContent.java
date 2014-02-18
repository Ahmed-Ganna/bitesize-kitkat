/*
Copyright 2014 Scott Logic Ltd

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

package com.shinobicontrols.transitions.data;


import com.shinobicontrols.transitions.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pseudo data-store for the story content to display in the app
 */
public class StoryContent {

    /**
     * Array of stories to display
     */
    public static List<StoryItem> STORIES = new ArrayList<StoryItem>();

    /**
     * Map of stories to refernce the story items by ID
     */
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
