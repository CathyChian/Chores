# Chore list (name tbd)

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Organize chores and simplifies when chores need to be done, and allows chores to be shared amongst roommates
### App Evaluation
- **Category:** Productivity
- **Mobile:** Designed for mobile to make use of notifications, but also works as a website/desktop app. The benefit of a mobile app is the portability and ease of access.
- **Story:** Will help organize what chores needs to be done and when
- **Market:** Could be useful to almost everyone
- **Habit:** Depends on how often the user does chores, from daily to weekly
- **Scope:** First just a to-do list of chores, then would add accounts to divide chores amongst roommates, then integrate into calendar.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* To do list of chores
* Createe, edit, delete chores
* Mark chores as done
* Detailed view of each chore
* Share chores with roommates - will require figuring out how to share data between specific accounts
    * Account login and logout

**Optional Nice-to-have Stories**

* Calendar of chores (Google Calendar integration?)
* Graph of when chores were finished in detailed view
* Sort chores by priority, date due, etc.
* Search chores

### 2. Screen Archetypes

* Chore list
   * To do list of chores
   * Delete chores
   * Mark chores as done
* Detailed chore view
   * Create, edit, and delete chores
   * Mark chores as done
   * Detailed view of each chore
* Create/edit chore
   * Create chore
   * Edit chore

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Chore list 
* Calendar (optional)
* Roommates
* Account


**Flow Navigation** (Screen to Screen)

* Chore list
   * Click on chore to go to detailed view

## Wireframes

![Wireframe](https://i.imgur.com/eP1cVOc.jpg)


## Schema 
### Models
#### Chore

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the chore (default field) |
   | title         | String   | name of chore |
   | description   | String   | longer explanation of chore if needed |
   | user          | Pointer to User| user who created chore |
   | sharedWith    | List of Pointers to User | users who also have this chore |
   | frequency     | int      | frequency that the user set for the chore |
   | reoccurring   | boolean  | true if chore is reoccurring |
   | priority      | int      | how important the user set the chore as |
   | lastCompleted | DateTime | when the chore was last completed |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |

#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the chore (default field) |
   | name          | String   | name of user |
   | roommates     | List of Pointers to User | user's roommates |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |

### Networking

#### List of network requests by screen
   - Home Feed Screen
      - (Read/GET) Query all posts where user is author or roommate
      - (Create/POST) Create a new chore
      - (Update/POST) Mark chore as done
      - (Update/POST) Change details of chore (e.g. frequency, priority)
      - (Delete) Delete chore
   - Roommates Screen
      - (Update/PUT) Update user's roommates
   - Account Screen
      - (Read/GET) Query logged in user object
