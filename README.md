InfiniteObjects
===============
InfiniteObjects is a plugin for the [Spout voxel game platform](https://github.com/SpoutDev/Spout) that provides user definable world generator objects using a script like file format based on YAML syntax.

[Homepage] | [Forums] | [Twitter] | [Facebook]

## Using
It's easy to get started! Simply [download the latest][Download] compatible InfiniteObjects jar, then place the jar in your [Spout](http://get.spout.org) plugins folder. Configure as desired, and fire up your server! Simple as that!

## Contributing
Like the project? Feel free to [donate] to help continue development!

Are you a talented programmer looking to contribute some code? We'd love the help!
* Open a pull request with your changes, following our [guidelines and coding standards](http://spout.in/prguide).
* Please follow the above guidelines for your pull request(s) accepted.
* For help setting up the project, keep reading!

## The license
InfiniteObjects is licensed under the [GNU Lesser General Public License Version 3][License], but with a provision that files are released under the MIT license 180 days after they are published. This dual license is referred to as the [Spout License Version 1][License].

## Getting the source
The latest and greatest source can be found here on [GitHub][Source].

If you are using Git, use this command to clone the project:

    git clone git://github.com/SpoutDev/InfiniteObjects.git

Or download the [latest zip archive][Download Source].

## Compiling the source
SpoutPlugin uses Maven to handle its dependencies.

* Download and install [Maven 2 or 3](http://maven.apache.org/download.html)  
* Checkout this repo and run: `mvn clean install`

## Using with your project
If you're using [Maven](http://maven.apache.org/download.html) to manage project dependencies, simply include the following in your `pom.xml`:

    <dependency>
        <groupId>org.spout</groupId>
        <artifactId>infobjects</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

If you do not already have our repo in your repository list, you will need to add this as well:

    <repository>
        <id>spout-repo</id>
        <url>http://nexus.spout.org/content/groups/public</url>
    </repository>

If you'd prefer to manually import the latest .jar file, you can get it from our [download site][Download].

Want to know how to use the API? Check out the latest [docs][Docs].

[Homepage]: http://www.spout.org
[Forums]: http://forums.spout.org
[License]: http://spout.in/licensev1
[Source]: https://github.com/SpoutDev/InfiniteObjects
[Download]: http://get.spout.org/dev/infobjects.jar
[Download Source]: https://github.com/SpoutDev/InfiniteObjects/archive/master.zip
[Builds]: https://travis-ci.org/SpoutDev/InfiniteObjects
[Docs]: http://jd.spout.org/infiniteobjects
[Issues]: https://spoutdev.atlassian.net/browse/INFOBJECTS
[Twitter]: http://spout.in/twitter
[Facebook]: http://spout.in/facebook
[Donate]: http://spout.in/donate
