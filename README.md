# Simple DBMS
The goal of the term projects is to make a simple DBMS. There are documents about the projects in the [docs/](https://github.com/hyunjinjeong/snu-db-2020/blob/master/docs/) folder.

## Project 1-1: SQL Parser
The first project is to implement a simple DBMS parser by using [JavaCC](https://javacc.github.io/javacc/). The grammar for the parser is defined in [docs/2020_1-1_Grammar.pdf](https://github.com/hyunjinjeong/snu-db-2020/blob/master/docs/2020_1-1_Grammar.pdf).

## Environment and Versions
* `IDE`: Eclipse 2020-03
* `Java`: JAVA 14
* `JavaCC Eclipse Plug-in`: 1.5.33

## How to Use
To generate parser files, open the **.jj file** and select the `Compile with javacc` option offered by JavaCC Eclipse Plug-in.

To run the **.jar file** that has been exported by Eclipse, put the following line:

```shell
javaw -jar FILE_NAME.jar
```
