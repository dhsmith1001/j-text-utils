# j-text-utils
This is a fork of [RuedigerMoeller/j-text-utils](https://github.com/RuedigerMoeller/j-text-utils) originally exported from https://code.google.com/archive/p/j-text-utils/.

------

Text Utilities for Java

Gives text representations of common data views such as tree and table.

**A table example:**

```
______________________________________________________________

| First Name| Last Name| Sport        | # of Years| Vegetarian|

|=============================================================|

| Jane      | White    | Speed reading| 20        | true      |

| Joe       | Brown    | Pool         | 10        | false     |

| John      | Doe      | Rowing       | 3         | true      |

| Kathy     | Smith    | Snowboarding | 5         | false     |

| Sue       | Black    | Knitting     | 2         | false     |
```



Read
[Using TextTable to see how it is done.](https://code.google.com/archive/p/j-text-utils/wikis/UsingTextTable.wiki)



**A tree example:**

```
-- target/

   |-- j-text-utils-0.3.jar

   |-- maven-archiver/

   |   `-- pom.properties

   |-- classes/

   |   `-- dnl/

   |      `-- utils/

   |         `-- text/

   |            |-- TextTreeTable$TreeTableSeparatorPolicy.class

   |            |-- SeparatorPolicy.class

   |            |-- TextTable.class

   |            |-- TextTreeTable.class

   |            |-- TextTableRenderer.class

   |            |-- tree/

   |            |   `-- TextTree.class

   |            |-- TextTableModel.class

   |            |-- TextTreeTable$1.class

   |            |-- LastRowSeparatorPolicy.class

   |            `-- TextTreeTableRenderer.class

   |-- test-classes/

   |   `-- dnl/

   |      `-- utils/

   |         `-- text/

   |            |-- TextTreeTableTest.class

   |            |-- TextTableTest.class

   |            `-- tree/

   |               |-- FsObject.class

   |               |-- TextTreeTest.class

   |               `-- FsModel.class

   `-- surefire-reports/

      |-- dnl.utils.text.TextTreeTableTest.txt

      |-- TEST-dnl.utils.text.tree.TextTreeTest.xml

      |-- TEST-dnl.utils.text.TextTreeTableTest.xml

      |-- TEST-dnl.utils.text.TextTableTest.xml

      |-- dnl.utils.text.TextTableTest.txt

      `-- dnl.utils.text.tree.TextTreeTest.txt
```

Example Code:
[Using TextTree](https://code.google.com/archive/p/j-text-utils/wikis/UsingTextTree.wiki)

------

### Adding j-text-utils to your Maven POM

The dependency:

```
<dependency>

    <groupId>dnl.utils</groupId>

    <artifactId>j-text-utils</artifactId>

    <version>0.3.3</version>

</dependency>
```

And add the following repository:

```
<repositories>

    <repository>

        <id>d-maven</id>

        <url>http://d-maven.googlecode.com/svn/trunk/repo</url>

    </repository>

</repositories>
```



------

**'Provided' dependencies:**

Note that if you use the `GuavaTableModel` you'll need to add a dependency for guava in your pom.

Same goes for CSV functionality which requires opencsv, for example:



```
<dependency>

    <groupId>net.sf.opencsv</groupId>

    <artifactId>opencsv</artifactId>

    <version>2.3</version>

</dependency>
```
