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
	
	
	
	
	$('#ajouter').on('click', function(event){
		event.preventDefault();
		var href = $(this).attr('href');
		
		
	$.get(href,function(livre,status){
		$('.myForm #nom').val(livre.nom);
		$('.myForm #auteur').val(livre.auteur);
		$('.myForm #type').val(livre.type);
		$('.myForm #emplacement').val(livre.emplacement);
		$('.myForm #nombreExemplaire').val(livre.nombreExemplaire);
	
	
	});
	
		setTimeout(() => {  $('.myForm #exampleModal').modal('show'); }, 500);
	});
	
	});
	
	
	

	

	
	
});