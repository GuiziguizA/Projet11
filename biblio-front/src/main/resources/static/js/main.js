/**
 * 
 */
$(document).ready(function(){
	

	
	$('.table  .delBtn').click(function(event){
		event.preventDefault();
		var href = $(this).attr('href');
	$('#myModal #delRef').attr('href',href);
	$('#myModal ').modal();
	});
	
});