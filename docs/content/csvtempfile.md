title: Return a temp file without creating one on disk
date: 2022-12-27 10:00
category: Daily-Java
author: teenamgeorge

#### Context of the problem:
Last problem (flattening map of <String, CustomType>) was about preparing the data for creating the CSV file.
This problem is about creating the CSV file itself. The file is to be created in memory and returned to the caller.
The caller is a ReST endpoint that will stream the file to the client. The file is to be deleted as soon as the client has downloaded it.

The size of the file can be quite large, so it's not a good idea to store it on disk, even on the `temp` folder of the OS. The caller may not download the file and instead re attempt the whole workflow another day. The server will not be restarted often enough to clean up `temp` folders before eating up disk space.

#### Solution:
Today's problem is about delaying the file creating until the user requests for it, and then return the file without storing anything in disk.
The whole content of the CSV file (headers and rows) will be created and persisted into a DB table. Due to the dynamic nature of the headers, the content will be stored in a `CLOB` column (which has the capacity to store up to 4 GB of data).
So the DB table will have a primary key, a timestamp column and a `CLOB` column. When the caller requests for the file, the `CLOB` column will be read, and the file will be created in memory and returned to the caller.
This problem is solved in two similar ways: one using `org.springframework.core.io.ByteArrayResource` and the other using `org.springframework.core.io.FileSystemResource`.

##### Solution 1: Using `org.springframework.core.io.ByteArrayResource`
`usecase.tempfilecsv.me.teenageorge.daily.CsvTempFileUsecase.createByteArrayResource()`

``` java
public ByteArrayResource createByteArrayResource() {
        var csvString = fileRepository.getCsvString();
        return new ByteArrayResource(csvString.getBytes(StandardCharsets.UTF_8));
    }

```

...end-code-block
##### Solution 2: Using `org.springframework.core.io.FileSystemResource` with an in-memory file system

`usecase.tempfilecsv.me.teenageorge.daily.CsvTempFileUsecase.createFileSystemResource()`
``` java
@SneakyThrows(IOException.class)
    public FileSystemResource createFileSystemResource() {
        var csvString = fileRepository.getCsvString();
        Map<String, Object> env = new HashMap<>();
        env.put("create", "true");

        URI uri = URI.create("jar:file:/tmp/temp-fs");

        try (FileSystem inMemoryFs = FileSystems.newFileSystem(uri, env)) {
            String prefix = "tempfile";
            String suffix = ".csv";
            Path tempFile = Files.createTempFile(inMemoryFs.getPath("/"), prefix, suffix);
            Files.writeString(tempFile, csvString);
            return new FileSystemResource(tempFile);
        }
    }
```
#### Conclusion:
Solution 1 is simpler and more straightforward. It's also more efficient, as it doesn't require creating a file system in memory.

#### Source code: [daily-problems/tempfilecsv](https://github.com/teenageorge/daily-problems/tree/main/src/main/java/me/teenageorge/daily/tempfilecsv)