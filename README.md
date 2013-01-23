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

## License

    Copyright 2012 - 2013 Sean O' Shea

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
