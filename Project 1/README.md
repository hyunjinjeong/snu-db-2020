# Simple DBMS

The goal of the term project is to make a simple DBMS that can execute basic fuctions of SQL such as create table, insert, delete, select, etc.

The project consists of three subprojects from 1-1 to 1-3.

There are project specification and reports in [specs/](https://github.com/hyunjinjeong/snu-db-2020/tree/master/specs) and [reports/](https://github.com/hyunjinjeong/snu-db-2020/tree/master/reports).

## Project 1-1: SQL Parser

The first project is to implement a simple SQL parser by using [JavaCC](https://javacc.github.io/javacc/). The grammar for the parser is defined in [docs/2020_1-1_Grammar.pdf](https://github.com/hyunjinjeong/snu-db-2020/blob/master/docs/2020_1-1_Grammar.pdf).

## Project 1-2: Implementing DDL

Based on Project 1-1, implement functions to save and manage schemas with BerkeleyDB.

- CREATE TABLE
- DROP TABLE #name
- DESC #name
- SHOW TABLES

## Project 1-3: Implmenting DML

Based on Project 1-1 and 1-2, implement DMLs that directly handle data with BerkeleyDB.

- INSERT
- DELETE
- SELECT

## Environment and Versions

- `IDE`: Eclipse 2020-03
- `Java`: JAVA 14
- `JavaCC Eclipse Plug-in`: 1.5.33

## How to Use

To generate parser files, open the **.jj file** and select the `Compile with javacc` option offered by JavaCC Eclipse Plug-in.

To run the **.jar file** that has been exported by Eclipse, put the following line:

```shell
java -jar FILE_NAME.jar
```
