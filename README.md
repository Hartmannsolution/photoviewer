# Photo viewer
This project is a backend application to show photos with description, view no and tags

## Lessons learned
1. To use JPQL in intellij it is necessary to add the eclipselink persistense info. But this will create an impossible error to debug on the server.
2. When merging entities with relation to collections of other entities us a SET to avoid sql error duplicate key in the merge table.
3. When commit was bad do a: git checkout -b <last commit hash> to see if this is better
4. Use intellij to Git -> Show git log -> Mark to commits -> right click and "compare versions" -> choose file.
5. Bidirectional think of owning side and create add and remove method to automatically remove the other sides reference.
6. Handle Dates as created and updated in the way it is done in Photo entity.
7. 

## API Documentation 
All endpoints are to be found on this URL: https://edu.bugelhartmann.dk/photoviewer/
<table>
<tr>
<th>
Request
</th>
<th>
Request data (json)
</th>
<th>
Response
</th>
<th>
Description
</th>
</tr>
<!-- ########## LOGIN ####################-->
<tr>
<td>
<pre>
<br/>POST: /api/login<br/>
</pre>
</td>

<td>
<pre>

  {
    "username": "user", (mandatory)
    "password": "password" (mandatory)
}
</pre>
</td>
<td>
<pre>
json
  {
  "username": "admin",
  "token": "dfJhbGci..."
}
</pre>
</td>
<td>

</td>
</tr>
<!-- ########## GET TAGS ####################-->
<tr>

<td>
<pre>
<br/>GET: /api/tag<br/>
</pre>
</td>

<td>
<pre>NONE</pre>
</td>
<td>
<pre>
  [
  {
    "name": "1900",
    "photos": ['photo1']
  },...]
</pre>
</td>
<td>

</td>
</tr>
<!-- ########## GET TAG ####################-->
<tr>

<td>
<pre>
<br/>GET: /api/tag/{name}<br/>
</pre>
</td>

<td>
<pre>
  {
    "name": "1900",
    "photos": ['photo1']
  }
</pre>
</td>
<td>
<pre>
{
    "name": "1900",
    "photos": ['photo1']
}
</pre>
</td>
<td>

</td>
</tr>
<!-- ########## POST TAG ####################-->
<tr>

<td>
<pre>
<br/>POST: /api/tag/<br/>
</pre>
</td>

<td>
<pre>
{
"name":"tag name", (mandatory)
"description":"some text"
}
</pre>
</td>
<td>
<pre>
{"name":"tag name","description":"some text"}
</pre>
</td>
<td>
PROTECTED
</td>
</tr>
<!-- ########## PUT TAG ####################-->
<tr>

<td>
<pre>
<br/>PUT: /api/tag/{name}<br/>
</pre>
</td>

<td>
<pre>
{
"name":"tag name", (mandatory)
"description":"some text" (mandatory)
}
</pre>
</td>
<td>
<pre>
{"name":"tag name","description":"some text"}
</pre>
</td>
<td>
PROTECTED
</td>
</tr>
<!-- ########## DELETE TAG ####################-->
<tr>

<td>
<pre>
<br/>DELETE: /api/tag/{name}<br/>
</pre>
</td>

<td>
<pre>
NONE
</pre>
</td>
<td>
<pre>
{
"name":"tag name", (mandatory)
"description":"some text"
}
</pre>
</td>
<td>
PROTECTED
</td>
</tr>
<!-- ########## PHOTO ####################-->
<!-- ########## GET PHOTO ####################-->
<tr>

<td>
<pre>
<br/>GET: /api/photo<br/>
</pre>
</td>

<td>
<pre>NONE</pre>
</td>
<td>
<pre>
[
{
  "location": "Somewhere",
  "name": "somename",
  "viewno": 0,
  "description": "Some text",
  "tags": [{"name":"tagname"},..]
},...]
</pre>
</td>
<td>

</td>
</tr>
<!-- ########## GET PHOTO ####################-->
<tr>

<td>
<pre>
<br/>GET: /api/photo/{name}<br/>
</pre>
</td>

<td>
<pre>
  NONE
</pre>
</td>
<td>
<pre>
{
  "location": "Somewhere",
  "name": "somename",
  "viewno": 0,
  "description": "Some text",
  "tags": [{"name":"tagname"},..]
}
</pre>
</td>
<td>

</td>
</tr>
<!-- ########## POST PHOTO ####################-->
<tr>

<td>
<pre>
<br/>POST: /api/photo/<br/>
</pre>
</td>

<td>
<pre>
{
  "location": "Somewhere", (mandatory)
  "name": "some name", (mandatory)
  "description": "Some text"
}
</pre>
</td>
<td>
<pre>
{
  "location": "Somewhere",
  "name": "some name",
  "description": "Some text"
}
</pre>
</td>
<td>
PROTECTED
</td>
</tr>
<!-- ########## PUT PHOTO ####################-->
<tr>

<td>
<pre>
<br/>PUT: /api/photo/{name}<br/>
</pre>
</td>

<td>
<pre>
{
  "location": "Somewhere", 
  "name": "some name",       (mandatory)
  "description": "Some text",
"tags":["tagName"],
"viewno":2
}

</pre>
</td>
<td>
<pre>
{
  "location": "Somewhere",
  "name": "some name",
  "description": "Some text"
}
</pre>
</td>
<td>
PROTECTED
Updating the viewno will change viewno for all other images
</td>
</tr>
<!-- ########## DELETE PHOTO ####################-->
<tr>

<td>
<pre>
<br/>DELETE: /api/photo/{name}<br/>
</pre>
</td>

<td>
<pre>
NONE
</pre>
</td>
<td>
<pre>
{
  "location": "Somewhere",
  "name": "some name",
  "description": "Some text"
}
</pre>
</td>
<td>
PROTECTED
</td>
</tr>

</table>

## Project can be seen here: 
[//]: # (TODO what needs to be done) 
1. Add list of old image descriptions (maybe tag descriptions to) with username, to see who wrote what
2. 