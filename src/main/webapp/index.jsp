<!DOCTYPE html>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Library</title>
</head>
<body>
<a href="/people">Users library</a>
<br/>
<a href="/book">All library books</a>
<br/>

<a href="/book?page=0&count=3">First page</a>

<br/>

<a href="/book?sort_by_year=true">Library books with sort by year</a>

<br/>

<a href="/book?sort_by_year=true&page=0&count=4">First sort page</a>
</body>
</html>
