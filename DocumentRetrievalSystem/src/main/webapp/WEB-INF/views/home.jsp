<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<title>Home</title>
</head>
<body>

<div class="row">
<div class="col-md-4"></div>
<div class="col-md-4">
<h2>
	Document Retrieval System 
</h2>
<i>Type your query</i><br/>
<form action="/textretrieval/myquery?q="+queryname" method="get">
<input type="search" class="form-control" name="queryname" placeholder="enter your query"/><br/>
<input type="submit" class="btn btn-success form-control" value="Search"/>

</form>
</div>
<div class="col-md-4"></div>
</div><!-- end of row -->
</body>
</html>
