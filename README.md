# Getting Started

### Endpoints

* [Swagger UI](http://localhost:8080/swagger-ui/index.html)
* [concourse](http://localhost:8081/)
    * username/password - petka/petka
* [keycloak](http://localhost:8082/)
    * username/password - petka/petka
* [actuator](http://localhost:8080/actuator/)
* [chaos-monkey](http://localhost:8080/actuator/chaosmonkey)

### Configuration

* Configure keycloak server (create realm, client and user) and generate auth token.   
  from **devops/keycloak/** run   
  `./keycloak.py`
* Build test image  
  from **devops/test** run   
  `./build_image.sh`
* Upload concourse task   
  from **devops/concourse** run   
  `./upload_tasks.sh`

| Features         |                 Description                 | Status     |
|:-----------------|:-------------------------------------------:|          ---: |
| Api First        | Generate SDK and server stub from api yaml. |Done|
| Rest query       | Perform search over all entity properties.  |Done|
| Tenant isolation |        Supports org and operator API        |Done|
| includeDeleted   |  Include in the response deleted elements.  |Done|
| includeCount     |        Returns either page or slice.        |Done|
| patch            |            Partial update entity            |Done|
| chaos-monkey     |              Chaos engineering              |Done|

## Filtering Syntax

* [spring-filter](https://github.com/turkraft/spring-filter)

### Fields

Field names should be directly given without any extra literals. Dots indicate nested fields. For
example: `category.updatedAt`

### Inputs

Numbers should be directly given. Booleans should also directly be given, valid values are `true` and `false` (case
insensitive). Others such as strings, enums, dates, should be quoted. For example: `status : 'active'`

### Operators

<table>
  <tr> <th>Literal (case insensitive)</th> <th>Description</th> <th>Example</th> </tr>
  <tr> <td>and</td> <td>and's two expressions</td> <td>status : 'active' <b>and</b> createdAt > '1-1-2000'</td> </tr>
  <tr> <td>or</td> <td>or's two expressions</td> <td>value ~ '%hello%' <b>or</b> name ~ '%world%'</td> </tr>
  <tr> <td>not</td> <td>not's an expression</td> <td> <b>not</b> (id > 100 or category.order is null) </td> </tr>
</table>

> You may prioritize operators using parentheses, for example: `x and (y or z)`

### Comparators

<table>
  <tr> <th>Literal (case insensitive)</th> <th>Description</th> <th>Example</th> </tr>
  <tr> <td>~</td> <td>checks if the left (string) expression is similar to the right (string) expression</td> <td>catalog.name <b>~</b> 'electronic%'</td> </tr>
  <tr> <td>:</td> <td>checks if the left expression is equal to the right expression</td> <td>id <b>:</b> 5</td> </tr>
  <tr> <td>!</td> <td>checks if the left expression is not equal to the right expression</td> <td>username <b>!</b> 'torshid'</td> </tr>
  <tr> <td>></td> <td>checks if the left expression is greater than the right expression</td> <td>distance <b>></b> 100</td> </tr>
  <tr> <td>>:</td> <td>checks if the left expression is greater or equal to the right expression</td> <td>distance <b>>:</b> 100</td> </tr>
  <tr> <td><</td> <td>checks if the left expression is smaller than the right expression</td> <td>distance <b><</b> 100</td> </tr>
  <tr> <td><:</td> <td>checks if the left expression is smaller or equal to the right expression</td> <td>distance <b><:</b> 100</td> </tr>
  <tr> <td>is null</td> <td>checks if an expression is null</td> <td>status <b>is null</b></td> </tr>
  <tr> <td>is not null</td> <td>checks if an expression is not null</td> <td>status <b>is not null</b></td> </tr>
  <tr> <td>is empty</td> <td>checks if the (collection) expression is empty</td> <td>children <b>is empty</b></td> </tr>
  <tr> <td>is not empty</td> <td>checks if the (collection) expression is not empty</td> <td>children <b>is not empty</b></td> </tr>
  <tr> <td>in</td> <td>checks if an expression is present in the right expressions</td> <td>status <b>in (</b>'initialized'<b>,</b> 'active'<b>)</b></td> </tr>
</table>

> Note that the `*` character can also be used instead of `%` when using the `~` comparator

### Functions

A function is characterized by its name (case insensitive) followed by parentheses. For example: `currentTime()`. Some
functions might also take arguments, arguments are seperated with commas. For example: `min(ratings) > 3`
<table>
  <tr> <th>Name</th> <th>Description</th> <th>Example</th> </tr>
  <tr> <td> absolute </td> <td> returns the absolute </td> <td> <b>absolute(</b>x<b>)</b> </td> </tr>
  <tr> <td> average </td> <td> returns the average </td> <td> <b>average(</b>ratings<b>)</b> </td> </tr>
  <tr> <td> min </td> <td> returns the minimum </td> <td> <b>min(</b>ratings<b>)</b> </td> </tr>
  <tr> <td> max </td> <td> returns the maximum </td> <td> <b>max(</b>ratings<b>)</b> </td> </tr>
  <tr> <td> sum </td> <td> returns the sum </td> <td> <b>sum(</b>scores<b>)</b> </td> </tr>
  <tr> <td> currentDate </td> <td> returns the current date </td> <td> <b>currentDate()</b> </td> </tr>
  <tr> <td> currentTime </td> <td> returns the current time </td> <td> <b>currentTime()</b> </td> </tr>
  <tr> <td> currentTimestamp </td> <td> returns the current time stamp </td> <td> <b>currentTimestamp()</b> </td> </tr>
  <tr> <td> size </td> <td> returns the collection's size </td> <td> <b>size(</b>accidents<b>)</b> </td> </tr>
  <tr> <td> length </td> <td> returns the string's length </td> <td> <b>length(</b>name<b>)</b> </td> </tr>
  <tr> <td> trim </td> <td> returns the trimmed string </td> <td> <b>trim(</b>name<b>)</b> </td> </tr>
  <tr> <td> upper </td> <td> returns the uppercased string </td> <td> <b>upper(</b>name<b>)</b> </td> </tr>
  <tr> <td> lower </td> <td> returns the lowercased string </td> <td> <b>lower(</b>name<b>)</b> </td> </tr>
  <tr> <td> concat </td> <td> returns the concatenation of two given strings </td> <td> <b>concat(</b>firstName<b>, concat(</b>' '<b>,</b> lastName<b>))</b> </td> </tr>
</table>
