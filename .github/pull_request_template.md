# Checklist before creating a PR

- [ ] I installed the pre-commit hook running Ktlint
- [ ] I wrote unit tests for my core components and at least 1 UI test to run the scenario as a User
  would do
- [ ] I compared with the iOS version, so that the two apps don't have major differences

# Description of the feature/bug (describe the context too)

example: This app is about reading comics. Some episodes are free, and others are not free. Users
pay with coins (internal money), and buy coins with in-app purchases.

This new feature is about offering some kind of magazines, which are updated weekly. Episodes list
in these magazines will be free, whether we read them from the magazine or the classic catalog list.

We can comment the episodes as well as the magazines. Comments on magazine will stay even after an
update of the magazine on the next week.

We can also follow a magazine, and we will get a notification when the magazine gets updated.

See
[Notion space](https://www.notion.so/myorg/Epic-User-Stories-72a58536b5f342c0a773c9b4c6d17498)
for more details.

# List of tests added/modified

example:

Unit tests:

- viewmodel: display a loader and then a list of magazine
- viewmodel: display a loader and then an error
- viewmodel: display a loader and then a list, and then an error on refresh
- magazine list fragment: display a list when list is given. check for readability of long titles.

UI tests:

- Read 2 episodes from a magazine : open app, go to tab magazines, click on a magazine, go to
  episodes, read an episode, click on next episode.

# List of tests executed (manual or automated)

example :

- all of the above of course
- manual: check that we don't display "next" arrow when on last episode of magazine
- manual: check that the description is still readable on small screens (4.6")

# Phones used to test this feature (brand, model and Android version)

- Google Pixel 4a Android 11
- Sony Z3C Android 6

# Credentials to use, if any

example:
⚠️ Feature not deployed on dev yet, please use mock

- account with money toto@email.com / password
- account with NO money tata@email.com / password
- account that read all episodes from the magazine 'love' love@email.com / password

# Remaining problems, things to pay attention to

example:

- Scrolling the list of episodes of a magazine seems slow on the Pixel.
- Sometimes the back arrow disappears, I am working on it.
- not sure of the way I wrote error handling on network requests, could you give some insight?
