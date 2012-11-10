InfObjects
==========
An experimental project for user definable world generator objects using a script like file format based on YAML syntax.

Visit our [website][Website] or get support on our [forums][Forums].  
View issues and bugs on our [issue tracker][Issues].

[![Follow us on Twitter][Twitter Logo]][Twitter][![Like us on Facebook][Facebook Logo]][Facebook][![Donate to the Spout project][Donate Logo]][Donate]

## The License
InfObjects is licensed under the [GNU Lesser General Public License Version 3][License], but with a provision that files are released under the MIT license 180 days after they are published. Please see the `LICENSE.txt` file for details.

Copyright (c) 2011-2012, SpoutDev <<http://www.spout.org/>>  
[![Spout][Author Logo]][Website]

## Getting the Source
The latest and greatest source can be found on [GitHub].  
Download the latest builds from [Jenkins]. [![Build Status](http://build.spout.org/job/InfObjects/badge/icon)][Jenkins]  
View the latest [Javadoc].

## Compiling the Source
InfObjects uses Maven to handle its dependencies.

* Install [Maven 2 or 3](http://maven.apache.org/download.html)  
* Checkout this repo and run: `mvn clean install`

## Using with Your Project
For those using [Maven](http://maven.apache.org/download.html) to manage project dependencies, simply include the following in your pom.xml:

    <dependency>
        <groupId>org.spout</groupId>
        <artifactId>infobjects</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

If you do not already have repo.spout.org in your repository list, you will need to add this also:

    <repository>
        <id>spout-repo</id>
        <url>http://repo.spout.org</url>
    </repository>

## Coding and Pull Request Conventions
* Generally follow the Oracle coding standards.
* No spaces, only tabs for indentation.
* No trailing whitespaces on new lines.
* 200 column limit for readability.
* Pull requests must compile, work, and be formatted properly.
* Sign-off on ALL your commits - this indicates you agree to the terms of our license.
* No merges should be included in pull requests unless the pull request's purpose is a merge.
* Number of commits in a pull request should be kept to *one commit* and all additional commits must be *squashed*.
* You may have more than one commit in a pull request if the commits are separate changes, otherwise squash them.
* For clarification, see the full pull request guidelines [here](http://spout.in/prguide).

**Please follow the above conventions if you want your pull request(s) accepted.**

[Author Logo]: http://cdn.spout.org/img/logo/spout_305x135.png
[License]: http://www.spout.org/SpoutDevLicenseV1.txt
[Website]: http://www.spout.org
[Forums]: http://forums.spout.org
[GitHub]: https://github.com/VanillaDev/InfObjects
[Jenkins]: http://build.spout.org/job/InfObjects
[Issues]: http://issues.spout.org/browse/InfObjects
[Twitter]: http://spout.in/twitter
[Twitter Logo]: http://cdn.spout.org/img/button/twitter_follow_us.png
[Facebook]: http://spout.in/facebook
[Facebook Logo]: http://cdn.spout.org/img/button/facebook_like_us.png
[Donate]: http://spout.in/donate
[Donate Logo]: http://cdn.spout.org/img/button/donate_paypal_96x96.png
