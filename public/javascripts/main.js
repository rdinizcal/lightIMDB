$(document).on("click", ".open-avaliarfilme", function () {
     var filmeId = $(this).data('id');
     var titulo = $(this).data('titulo');
     
     $(".modal-body #filme_titulo").html(titulo);
     $(".modal-body #FilmeId").val(filmeId);
});

$(document).on("click", "#avaliar-submit", function () {
	$("#avaliar-form").attr("action",'/filme/avaliar/'+$("#avaliar-form #FilmeId").val());
	$("#avaliar-form").submit();
});