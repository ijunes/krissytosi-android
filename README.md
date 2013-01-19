krissytosi-android
==================

If you download this project & want to run it, be sure to update the submodules for the project. Execute the following commands:

    git submodule init
    git submodule update

If you're running builds from ant, remember that you'll need to execute the following in each 'library' folder:

    android update project -p .

If you want to import the full project into Eclipse, just execute the following command from the base of the checkout:

    ant kt-setup

This will take care of initializing and updating the git submodules, copy relevant .classpath & .project files into the appropriate submodule folders & let you import the projects into a workspace.

To prepare a release build execute:

    ant kt-release -Dproguard.config=proguard-project.txt