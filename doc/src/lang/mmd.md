// áéíóú

section: Macros
title: <<name>>/<<section>>

------------

<< /_header.mmd

# <<title>> 

MySWG process file replacing macros.

  * replace a variable for its value
  * copy a line
  * include (inline) another file
  * you may comment lines out

Examples:

  * replace a &lt;&lt;variable&gt;&gt; for its value
  * &lt;&lt;&lt; copy this line
  * &lt;&lt; file to include
  * // this is a comment (that is, skipped)

## Variables
Variables are defined either at the configuration file or in any MD file.

As in

    file.md
      variable-1: value 1
      variable-2: value 2
      -----

When a MD file includes another MD file,
the macros of the include file extend (or even replace) variables for new values.

A special variable is defined for every MD file:

> &lt;&lt;ptr&gt;&gt;

This variable is replaced by the "path to root"
that is a relative path from this file to the web site root.

Example

> &lt;&lt; &lt;&lt;ptr&gt;&gt;/style.css

Includes 

> <<ptr>>/style.css

## Replace
The syntax

> &lt;&lt;name&gt;&gt;

is replaced by the value of 'name'.

The angle brackets cannot start a new line in order to avoid confussion with include command.

## Include line
The syntax

>  &lt;&lt;&lt; line

copies the line onto the html output.

The characters '<<<' must be at the begimming of the line (column 0).

## Include file
The syntax

>  &lt;&lt; filename

includes the mentioned filename.

The characters '<<' must be at the begimming of the line (column 0).

## Comments
Lines starting with '//' are skipped.

<< /_footer.mmd

