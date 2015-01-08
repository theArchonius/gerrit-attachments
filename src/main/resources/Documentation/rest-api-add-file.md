fileattachment-plugin attach file
=================================

NAME
----
file - attach or update a UTF-8 encoded file of the given file resource

SYNOPSIS
--------
    PUT /a/changes/{changeId}/revisions/{revisionId}/file/{file}/fileattachment~file

DESCRIPTION
-----------
Attaches a file to the given file resource or updates am existing file if a file with the same file path exists.

OPTIONS
-------

--file_path
> the file path of the file including the file name. Please note that this path is used to uniquely identify a attached file within the revision and does not have to match the original path of the file

--content
> the UTF-8 encoded content of the file 

RESULT
------

`OK` if the file has been attached or `FAILED` if the file could not be attached to the given file resource.

ACCESS
------
Any authenticated user.

EXAMPLES
--------

Attach a text file in the directory `somedir` and the file name `somefile` with a short blindtext to the file resource `src/Main.java` of the current revision of the change `Ib0b5840e1e7a6f9047cde4982271d4b0dec09da5`

    curl -X PUT -d '{ file_path:"somedir/somefile", content:"Lorem ipsum dolor sit amet\n" }' -H "Content-Type: application/json" \
    --digest --user joe:secret  http://localhost:8080/a/changes/Ib0b5840e1e7a6f9047cde4982271d4b0dec09da5/revisions/current/files/src%2FMain.java/fileattachment~file
    > "OK"

SEE ALSO
--------

* [List attached files](rest-api-list-files.html)
* [Delete attached file](rest-api-delete-file.html)