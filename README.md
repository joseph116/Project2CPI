# BachaSlide


[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://qalamars.com)

Bachaslide is a modern media management solution.

# New Features!
You can also:
  - Import and save files from GitHub, Dropbox, Google Drive and One Drive
  - Drag and drop markdown and HTML files into BachaSlide
  - Export documents as Markdown, HTML and PDF

Markdown is a lightweight markup language based on the formatting conventions that people naturally use in email.

> The overriding design goal for Markdown's
> formatting syntax is to make it as readable
> as possible.



### Tech

The app uses a number of open source projects to work properly:

* [AngularJS] - HTML enhanced for web apps!


And of course Dillinger itself is open source with a [public repository][dill]
 on GitHub.

### Git

For anybody facing problems with git; follow these steps:


```sh
// Add the remote, call it "upstream":
git remote add upstream https://github.com/MrRedaM/Project2CPI.git
```
```sh
// Fetch all the branches of that remote into remote-tracking branches, such as upstream/master:
git fetch upstream
```
```sh
// Make sure that you're on your master branch:
git checkout master
```
```sh
// Rewrite your master branch so that commits of yours are replayed on top of that other branch:
git rebase upstream/master
```


### Libraries

Dillinger is currently extended with the following plugins. Instructions on how to use them in your own application are linked below.

| Plugin | README |
| ------ | ------ |
| Glide | [plugins/dropbox/README.md][PlDb] |
| AndroidX Artifacts | [plugins/github/README.md][PlGh] |
| Google Material Library | [plugins/googledrive/README.md][PlGd] |




### Todos

 - Write MORE Tests
 - Add Night Mode

License
----

MIT




