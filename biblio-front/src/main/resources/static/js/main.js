/**
 * 
 */






$(document).ready(function(){
	
	$('#encours').click(function(){
	    $(this).hide();
	    setTimeout(() => {  $('#myModal').modal('show'); }, 500);
	  });
	
	$(' .table .delBtn').on("click",function(event){
		event.preventDefault();
	
		var href = $(this).attr('href');
		
	$('#myModal #delRef').attr('href',href);
	setTimeout(() => {  $('#myModal').modal('show'); }, 500);
	
	
	
	
	});
	
	
	

	

	
	
});