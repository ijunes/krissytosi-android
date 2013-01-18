krissytosi-android
==================

If you download this project & want to run it, be sure to update the submodules for the project. Execute the following commands:

git submodule init
git submodule update

If you're running builds from ant, remember that you'll need to execute the following in each 'library' folder:

android update project -p .

To prepare a release build execute:

ant kt-release -Dproguard.config=proguard-project.txt -verbose > out.txt