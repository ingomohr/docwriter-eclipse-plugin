[![License](https://img.shields.io/badge/License-Apache%202.0-yellow.svg)](https://opensource.org/licenses/Apache-2.0)

⚠️ _Legacy: As discussed w/ the team on May 16th 2022, we won't update this plugin anymore._

# docwriter-eclipse-plugin

## What is this?
This is an Eclipse plugin to wrap the [docwriter](https://github.com/ingomohr/docwriter) Java API to create *.docx files - optionally based on templates.

## What is the Target Java / Eclipse Version?
- Java 8
- Eclipse 2020-06

### Why that old Java and Eclipse?
... because we need the plugin to run right now in an old Java JRE and a target platform of 2020-06. (Hopefully that will change in the future).

## Updatesite
https://raw.githubusercontent.com/ingomohr/docwriter-eclipse-plugin/updatesite/updatesite

(Don't worry about the "_404: Not Found_" displayed when you open that URL on your browser.)

## How to Install to Eclipse
1. In Eclipse open Help -> Install New Software...
2. In the dialog that opens up paste the updatesite URL into "Work with:" text field and hit ENTER
3. <img width="700" alt="Install plugin to Eclipse" src="https://user-images.githubusercontent.com/2838592/138778750-33af6e1e-bba6-46c2-92e4-ad909211cb2d.png">
4. Click button "Finish" and follow the installation instructions.
5. When Eclipse notifies you that the plugin is not signed, just click "Install anyway".
6. When Eclipse asks you to restart, restart Eclipse.

## Examples
See the [examples](https://github.com/ingomohr/docwriter/wiki/Examples) in the [docwriter](https://github.com/ingomohr/docwriter) repo.
