// áéíóú

title: <<name>>

------------

<< /_header.mmd

# MySWG - Static Website Generation
or
> **MySWG** - Simple Website Generator

MySWG is a processor that transform a source tree into a website.

While replicating source directory structure,
files are copied from source tree into destination tree
with the following changes

  * file.md -> file.html (using markdown syntax)
  * file.mmd -> file.html (using macro syntax)
  * _file -> are ignored (not copied)
  * file -> file (any other file is copied as is)

Example: this site

      src                       dst
        src/_config.txt
        src/_footer.mmd
        src/_header.mmd
        src/imgs                  dst/imgs
          src/imgs/weight-32.png    dst/imgs/weight-32.png
        src/index.md              dst/index.html
        src/lang                  dst/lang
          src/lang/md.md            dst/lang/md.html
          src/lang/mmd.md           dst/lang/mmd.html
          src/lang/tables.md        dst/lang/tables.md

## Files
MD files are written using
[markdown syntax](lang/md.html) .

See

  * [markdown](lang/md.html)
  * [macros](lang/mmd.html)
  * [tables](lang/tables.html)

## File variables
Each MD file includes a heading part, 
that defines a number of variables.
the heading part expands from the beginning of the file to a line with 5 or more dashes.

the format of definitions is

> variable : value

Files may include a mark for MySWG to guess the encoding of the file

> charset : áéíóú

Generated html is always UTF-8.

## Site configuration
Site generation is controled by a configuration file in source directory.

This file defines variables for general use, or for MMD files. 
Definitions are project-wide, and may be refined in MD files.

Some definitions are used configure MySWG:

**root**
> Specifies the directory of the source tree. 
> If none is specified,
> MySWG uses the directory where configuration file sits.

**site**
> Specifies the directory of the html destination tree.
> If none is specified,
> MySWG uses `root/_site`

**force**
> It is a boolean value that controls what to do
> if the generated file already exists in the destination tree.

> If `false`, the destination directory is generated.

> If `true`, files are generated, or copied, **only** if changed or newer.

## Build your site
Let your source files be located at DIR,
with a configuration file at `DIR/_config.txt`.
The following command will build your web site: 

    > cd DIR
    > Build _config.txt

## Character encoding
MySWG will discover source file encoding using the following methods

  1. If the file contains the sequence 'áéíóú', its encoding is used.
  2. If the file has a BOM, use it.
  3. Otherwise, use system default character set.

A practical way to ensure the correct encoding is used
is to use the first method including a comment like this in every file

  // áéíóú

<< /_footer.mmd

