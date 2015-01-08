fileattachment-plugin list files
================================

NAME
----
files - lists all attached file resources for a given file

SYNOPSIS
--------
    GET /changes/{changeId}/revisions/{revisionId}/file/{file}/fileattachment~files

DESCRIPTION
-----------
Lists all attached files for a given file resource.

OPTIONS
-------

No Options available


RESULT
------

A list of objects with the properties `file_path` and `content`

--file_path
> the unique file path of the attached file within the given revision.

-- content
> the content of the text file

ACCESS
------
Any user.

EXAMPLES
--------

Lists all attached files to the file resource `src/Main.java` of the current revision of the change `Ib0b5840e1e7a6f9047cde4982271d4b0dec09da5`

    curl -X GET --digest --user joe:secret  http://localhost:8080/a/changes/Ib0b5840e1e7a6f9047cde4982271d4b0dec09da5/revisions/current/files/src%2FMain.java/fileattachment~files
    > )]}'
    >[
    >  {
    >    "file_path": "testfile",
    >    "content": "some additional data for main.java"
    >  },
    >  {
    >    "file_path": "file3",
    >    "content": "Lorem ipsum dolor sit amet\n"
    >  },
]



SEE ALSO
--------

* [Attach file](rest-api-add-file.html)
* [Delete attached file](rest-api-delete-file.html)