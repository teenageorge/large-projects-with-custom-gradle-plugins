title: Flatten nested map with custom type as values
date: 2022-12-26 10:00
category: Daily-Java
author: teenamgeorge

#### Context of the problem:

Last week, there was a feature request to re-create a downloadable CSV file and delete it as soon as the file is downloaded.
The file can be quite large, so it's not a good idea to store it on disk, even on the `temp` folder of the OS.

This feature was to be built in a long existing/evolving monolithic application deployed on client premises.
For some historic reason the frontend accepts the CSV file, parses the contents and send the content over to a ReST endpoint.
The backend module then passes this content through multiple complex business validations.
If any rule is broken, the CSV file must be recreated as-is along with two additional columns - status(OK, NOT_OK) and error message.
To complicate matters further, the headers are not fixed. They are dynamic and can change from one file to another depending on the data.

#### Solution:

Today's problem is about recreating a part of the headers from a `Map<String, CustomType>`.
`CustomType` is a POJO with a single field - a `Map<String, String>`.
To re-create this part of the header, the outer map (`Map<String, CustomType>`) needs to be flattened into a `Map<String, String>` by concatenating the key of the outer map with the key of the inner map as keys and inner map's values as values.
Then the keys of this map will be used as headers, while preserving the insertion order.
Outer keys are integer values represented as strings.
The inner keys are languages IDs (`en`, `fr`, `de` etc.). Some values may be missing, but each inner map entry will have all three keys.
The outer map size can be any size from 100 to 100,000.

Here is the method from `domain.flatmapjson.me.teenageorge.daily.Opinions` class that does this:
`LinkedHashMap` is used to preserve the insertion order of the keys. `java.parallelStream` is not used because the order is important.
``` java
public Map<String, String> getLocalisedOpinions() {
        return opinions.entrySet().stream()
                .flatMap(opinionEntry ->
                        opinionEntry.getValue().getLocalisedOpinions()
                                .entrySet().stream()
                                .map(localeOpinionEntry ->
                                        Map.entry(
                                                StringUtils.joinWith(" - ", opinionEntry.getKey(), localeOpinionEntry.getKey()),
                                                localeOpinionEntry.getValue())
                                )
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));
    }
```
#### Sample input:

```` json
{
"1":
    {
        "en": "good",
        "fr": "bien",
        "de": ""
    },
"2":
    {
        "en": "bad",
        "fr": "mal",
        "de": "Schlecht"
    },
"3":
    {
        "en": "bad",
        "fr": "mal",
        "de": "Schlecht"
    }
}
````

Expected output:

```` json
{
    "1 - en": "good",
    "1 - fr": "bien",
    "1 - de": "",
    "2 - en": "bad",
    "2 - fr": "mal",
    "2 - de": "Schlecht",
    "3 - en": "",
    "3 - fr": "mal",
    "3 - de": "Schlecht"
}
````

Source code: [daily-problems/flatmap](https://github.com/teenageorge/daily-problems/tree/main/src/main/java/me/teenageorge/daily/flatmapjson)