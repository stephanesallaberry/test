# Useful links

[Sonarcloud page](https://sonarcloud.io/project/overview?id=stephane-news-android)
[Bitrise page](https://app.bitrise.io/app/c73fa09410f8e1a7#/builds)

# Getting started

## Get Project on Android Studio

Open latest Android Studio (2020.3.1 on the 10th September 2021), and click "File > New > Project
from version control"
Enter the URL of the git project (git@github.com:username....).

## Ktlint

After automatic indexing and gradle sync an downloading the whole Internet, run the
task `setupProject` to add git hooks.

In the terminal at the root of this project, type in `./gradlew setupProject` or double click
on `setupProject` in the Gradle View.

This task install a pre-commit hook to run Ktlint linter (`ktlintCheck` task) before every commit.

This will not format your code automatically as it often breaks your files.

### What to do when commit fails ?

You can either:

- go to Git view, 'Console' tab, and click on the link to the report. As it's not really readable
  and clickable, consider using the next option;
- run task `ktlintCheck` and see what happens in "Run" view. Here the errors have a link to the
  source file. You can now fix the errors.

📝 You can setup a macro on your Android Studio to format&save all at once: Edit > Macros > Start
Macro Recording, and then click on Code > Reformat Code, Optimize Imports, AutoIndent Lines, File >
Save all.

Stop macro recording, give it a nice name, and associate the keyboard shortcut Cmd+S to this new
macro. Use it all the time to avoid ktlint warnings and fix errors automatically.

### I don't see the Gradle tasks you are talking about

This is likely due to an option being enabled by default on AS to speed up startup. Go to Android
Studio > Preferences > Experimental > uncheck "Do not build Gradle task list during Gradle sync".   
You should see the gradle task list on the next start of AS.

## Run project (DB, server)

This project is a template, made to demonstrate some nice architecture and set of libraries. No
backend has been developed for this project and so every "network" request is mocked. Everything
should work out of the box after a successful Gradle sync.

**This project is a template, so here are some advice to fill your Readme with useful advices**

/!\ List here any info needed to run dev server on your computer, necessity of running on mocks for
now (specify date), name of servers dev/preprod/prod...

Maybe say here that in order to test in app purchase you have to run your app locally on prodDebug
flavor, and to have your google account added to list of testers on google play console.

Explain how to test analytics: go to Firebase Debug View, run a command on your terminal to enable
debug mode, and wait for minimum 30seconds.

# Git workflow and Continous Integration (CI)

## Main structure

There are 3 main branches :

- `main` : production release
- `staging` : pre-production release (for testers usually)
- `develop` : development release

⚠️ There should *never* be any direct commit to those branches, only merges.

## Create a feature

For every new feature, a new branch called `feat/name-of-the-feature` has to be created. Once the
feature is ready for a pull request (see `create a pull request`), this branch will be merged into
the working branch `develop`

Once the current sprint is delivered in production, those working `feat/***` branches should be
deleted.

## Create a pull request

Once you have finished your feature on your `feat/***` branch, you are ready to send your pull
request:

- update your branch `feat/***` from the shared `develop` branch :
    - with a rebase (see a [tutorial](https://www.benmarshall.me/git-rebase/) if necessary)
    - or if you not enough familiar with git rebase, merge the `develop` branch into your `feat/***`
- go to github, and start a new PR, with the branch `develop` as the base
- fill out the PR description template
- once your got your review approved, ask your reviewer who's in charge to merge your branch

> congratulations, you finished you first PR !

## Creating a hotfix

If you have to create a hotfix (push a fix in production, but staging can't be sent to prod), there
is a way:

- create a branch from `main` called `hotfix/name-of-fix`
- once done (pull request if necessary), merge your branch into `main`
- merge your branch `hotfix/name-of-fix` into the shared `develop` branch
- delete your hotfix branch

## Git workflow and CI

- When you create a PR to branch develop -> bitrise CI will check your code with ktlint+detekt and
  run unit and UI tests.
- When PR is accepted, code gets merged to develop -> bitrise CI will run tests and send coverage to
  sonarcloud.io
- When develop is merged into staging -> bitrise CI will build an APK and upload it to firebase app
  distribution and send it to "testers" group.
- When staging is merged into main ->  bitrise CI will build a signed AAB and upload it to google
  play console on production track, but will not publish it (just to be safe). Someone will have to
  manually click on "publish". You can check the apk generated at the same time to test your app one
  last time.

# Architecture

## Core concepts: data/domain/transport or UI

The core concepts of this architecture are defined in
Notion: [Hexagonal Architecture](https://www.notion.so/myorg/Hexagonal-Architecture-k2ac384b004b459d8675e184e0bf6ed4)

## MVI architecture: debuggable and testable

MVI (Model-View-Intent) is an architecture inspired by MVVM (Model-View-ViewModel) and Redux from
web development. MVI is based on an unidirectional data flow, just like Redux.

Advantages:

- easy to debug
- easy to test

Inconvenient:

- lot of boilerplate

[insert link to MVI articles]

[How to write your own MVI system and why you shouldn't](https://www.youtube.com/watch?v=E6obYmkkdko)

We chose to implement a MVI architecture with the help of a library, to avoid boilerplate and ensure
some stability. This library is [Orbit MVI](https://github.com/orbit-mvi/orbit-mvi).

**This project is a template, so here are some advice to fill your Readme with useful advices**

List specific things about this project:

- we decided to send a broadcast from data layer when user is not authorized, and UI/transport layer
  receives this broadcast and displays login again
- a background service is running for bluetooth action. It is part of UI/transport layer even though
  there is no real UI associated to that.
- ...

# functional overview

This app is a template for other applications. It is made to demonstrate how to implement some very
common features:

- tab navigation with separate history
- authenticated network calls
- display of a list and detail page
- bitrise workflows
- integration of detekt and ktlint
- generation and uploading of code coverage
- data/domain/ui layers separation
- use of MVI library
- unit/UI tests


