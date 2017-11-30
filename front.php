<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script>
function myFunction() {
  var input, filter, table, tr, td, i;
  input = document.getElementById("myInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("myTable");
  tr = table.getElementsByTagName("tr");
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[0];
    if (td) {
      if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }       
  }
}
</script>

<style>
td { white-space:pre; }
.center{text-align:center;}
#myInput{
padding: 3px;
    border: solid 1px #E4E4E4;
    border-radius: 6px;
    background-color: #fff;
    font-size:23px;
    margin-top:20px;
    
    }      
</style>
<?php

$con=mysqli_connect("localhost","root","","test");
// Check connection
if (mysqli_connect_errno())
{
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$result = mysqli_query($con,"SELECT * FROM ibmproducts");


echo "<body>";
echo "<div class='container-fluid'>";
echo "<input type='text' id='myInput' onkeyup='myFunction()' placeholder='Search for names..' title='Type in a name'>";



echo "<br><br>";
echo "<table class='table table-striped table-bordered sortable' id='myTable'>
<tr>
<th class='center'>Hauptprodukt</th>
<th class='center'>Bundles</th>
<th class='center'>Productnumber</th>
<th class='center'>Last Modified</th>
<th class='center'>Link</th>
</tr>";

while($row = mysqli_fetch_array($result))
{
	echo "<tr>";
	echo "<td>" . $row['hauptprodukt'] . "</td>";
	echo "<td>" . $row['bundles'] . "</td>";
	echo "<td class='center'>" . $row['product_number'] . "</td>";
	echo "<td class='center'>" . $row['last_modified'] . "</td>";
	echo "<td class='center'><a href=".$row['link_to_document'].">Link</a></td>";
	echo "</tr>";
}
echo "</table>";
echo "</div>";
echo "</body>";

mysqli_close($con);
?>