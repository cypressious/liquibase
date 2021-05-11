package liquibase.extension.testing.command

import liquibase.exception.CommandValidationException

CommandTests.define {
    command = ["updateCountSql"]
    signature = """
Short Description: Generate the SQL to deploy the specified number of changes
Long Description: NOT SET
Required Args:
  changelogFile (String) The root changelog
  count (Integer) The number of changes to generate SQL for
  url (String) The JDBC database connection URL
Optional Args:
  contexts (String) Changeset contexts to match
    Default: null
  defaultCatalogName (String) The default catalog name to use for the database connection
    Default: null
  defaultSchemaName (String) The default schema name to use for the database connection
    Default: null
  labels (String) Changeset labels to match
    Default: null
  password (String) Password to use to connect to the database
    Default: null
  username (String) Username to use to connect to the database
    Default: null
"""

    run "Happy path", {
        arguments = [
                count        : 1,
                changelogFile: "changelogs/hsqldb/complete/simple.changelog.xml",
        ]

        expectedResults = [
                statusCode   : 0
        ]
    }

    run "Happy path with an output file", {
        arguments = [
                count        : 1,
                changelogFile: "changelogs/hsqldb/complete/simple.changelog.xml",
        ]

        setup {
            cleanResources("target/test-classes/updateCount.sql")
        }

        outputFile = new File("target/test-classes/updateCount.sql")

        expectedFileContent = [
                //
                // Find the " -- Release Database Lock" line
                //
                "target/test-classes/updateCount.sql" : [CommandTests.assertContains("-- Release Database Lock")]
        ]

        expectedResults = [
                statusCode   : 0
        ]
    }

    run "Run without a URL throws an exception", {
        arguments = [
                url: "",
                count : 1
        ]
        expectedException = CommandValidationException.class
    }

    run "Run without a changeLogFile throws an exception", {
        arguments = [
                changelogFile: "",
                count: 1
        ]
        expectedException = CommandValidationException.class
    }

    run "Run without count throws an exception", {
        arguments = [
                changelogFile: "changelogs/hsqldb/complete/simple.changelog.xml"
        ]
        expectedException = CommandValidationException.class
    }

    run "Run without any argument throws an exception", {
        arguments = [
                url: "",
        ]
        expectedException = CommandValidationException.class
    }
}