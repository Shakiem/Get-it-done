# Pre-work - *Get It Done*

**Get It Done** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Shakiem Saunders**

Time spent: **8** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can **successfully add and remove items** from the todo list
- [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
- [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Sort the todo items by date, priority, or status.
* [x] Filter the todo items by date, priority, or status.
* [x] Refresh the todo items in the listview with results stored in the database.
* [x] Animated splash screen.

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/82l1fOy.gif' title='Demo of Get It Done App' width='402' height='522' alt='Demo of Get It Done App' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

The main challenge was trying to support older versions of Android to widen the "reach" of the app to most users.
I found that a lot of the time API's I was trying to use were not supported in older versions, and those that were
supported in older versions have been deprecated in later versions.

## License

    Copyright [2016] [Shakiem Saunders]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
