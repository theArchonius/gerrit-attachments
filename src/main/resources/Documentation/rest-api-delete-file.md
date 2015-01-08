fileattachment-plugin delete file
=================================

NAME
----
file - delete an attached file of the given file resource

SYNOPSIS
--------
    DELETE /a/changes/{changeId}/revisions/{revisionId}/file/{file}/fileattachment~file

DESCRIPTION
-----------
Deletes an attached file of the given file resource.

OPTIONS
-------

--file_path
> the file path of the file including the file name.

RESULT
------

`OK` if the file has been deleted or `FAILED` if the file could not be deleted.

ACCESS
------
Any authenticated user.

EXAMPLES
--------

Deletes the attached file in the directory `somedir` and with the file name `somefile` of the file resource `src/Main.java` of the current revision of the change `Ib0b5840e1e7a6f9047cde4982271d4b0dec09da5`

    curl -X DELETE -d '{ file_path:"somedir/somefile"}' -H "Content-Type: application/json" \
    --digest --user joe:secret  http://localhost:8080/a/changes/Ib0b5840e1e7a6f9047cde4982271d4b0dec09da5/revisions/current/files/src%2FMain.java/fileattachment~file
    > "OK"

SEE ALSO
--------

* [Attach file](rest-api-add-file.html)
* [List attached files](rest-api-list-files.html)