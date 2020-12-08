// áéíóú

section: tables
title: <<name>>/<<section>>

------------

<< /_header.mmd

# <<title>> 

The markdown syntax is extended with the specification of tables.
As described in [](https://www.markdownguide.org/extended-syntax/).ZZ

Like this

<<< <div class="boxed">
  | header 1 | header 2 |
  | -------- | -------- |
  | col 1, 1 | col 1, 2 |
  | col 2, 1 | col 2, 2 |
<<< </div>

specified as

<<< <div class="boxed">
    | header 1 | header 2 |
    | -------- | -------- |
    | col 1, 1 | col 1, 2 |
    | col 2, 1 | col 2, 2 |
<<< </div>

## Tables with headers

A header is identified because the header row is followed by a like with '---'.

Like this

<<< <div class="boxed">
  | header 1 | header 2 |
  | col 1, 1 | col 1, 2 |
  | col 2, 1 | col 2, 2 |
<<< </div>

specified as

<<< <div class="boxed">
    | header 1 | header 2 |
    | col 1, 1 | col 1, 2 |
    | col 2, 1 | col 2, 2 |
<<< </div>

## Column alignment

And you may establish text alignment in columns:

  * align left: `:---`
  * align right: `---:`
  * center: `:---:`

<<< <div class="boxed">
  | header 1 | header 2 | header 3 |
  | :------- | :------: | -------: |
  | col 1, 1 | col 1, 2 | col 1, 3 |
  | col 2, 1 | col 2, 2 | col 2, 3 |
<<< </div>

specified as

<<< <div class="boxed">
    | header 1 | header 2 | header 3 |
    | :------- | :------: | -------: |
    | col 1, 1 | col 1, 2 | col 1, 3 |
    | col 2, 1 | col 2, 2 | col 2, 3 |
<<< </div>

## Table style

If you with to specify a style for a table,
you can use CSS.
For example:

<<< <div class="boxed">
    .mytable table {
      border: 1px solid black;
      border-collapse: collapse;
    }

    .mytable th, .mytable td {
      border: 1px solid black;
      padding-left: 15px;
      padding-right: 15px;
    }
<<< </div>

Then use it inside a `div` block:

<<< <div class="boxed">
<<< <div class="mytable">
  | header 1 | header 2 | header 3 |
  | :------- | :------: | -------: |
  | col 1, 1 | col 1, 2 | col 1, 3 |
  | col 2, 1 | col 2, 2 | col 2, 3 |
<<< </div>
<<< </div>

specified as

<<< <div class="boxed">
    <<< <div class="mytable">
    | header 1 | header 2 | header 3 |
    | :------- | :------: | -------: |
    | col 1, 1 | col 1, 2 | col 1, 3 |
    | col 2, 1 | col 2, 2 | col 2, 3 |
    <<< </div>
<<< </div>


<< /_footer.mmd

