/**
 * Formulaires ajax : le code html est généré sur le serveur et récupéré par ajax.
 * (Pour laisser le moteur de template générer le code html)
 * 
 * Un tel formulaire doit être fait ainsi :
 *
 *	- classe bouton-creer avec attributs:
 *		> data-url : l'url de création.
 *		> data-form : le form visé par l'action.
 *
 *	- classe bouton-modifier avec attributs:
 *		> data-url : l'url de création.
 *		> data-form : le form visé par l'action.
 *
 *	- classe bouton-supprimer avec attributs: data-url :
 *		> data-url : l'url de création.
 *		> data-form : le form visé par l'action.
 *
 *	- classe bouton-formulaire-nouveau avec attributs: 
 *	    > data-form : le formulaire à alimenter.
 *	    > data-url : l'url pour récupérer les informations.
 *
 *	- classe bouton-formulaire-modif avec attributs:
 *	    > data-form : le formulaire à alimenter.
 *	    > data-url : l'url pour récupérer les informations.
 *
 * En cas de retour de la requête en erreur, le formulaire, renvoyé dans la réponse, est réaffiché.
 * En cas de retour en succès, la page est rechargée.
 *
 * En retour de requête ajax, un message peut être retourné dans le header "appMessage".
 * Pour pouvoir afficher le message même après refresh de la page, stockage du message dans un objet de 
 * sessionStorage (persistant à travers la navigation). 
 * Au rafraîchissement, extraction de cet objet appMessage et affichage s'il existe.
 *    
 */


$(document).ready(function() {		
		
	$(".ajax-form").submit(function(event) {
		event.preventDefault();
	})
	
	// Si le formulaire utilise un selectpicker, il faut le refresh à son chargement.
	// A cette fin, on déclare aussi un événement "formFilled" à chaque chargement de formulaire.
	$(".ajax-form").on("formFilled", function() {
		$('.selectpicker').selectpicker('refresh');		
	});
	
	
	$(".bouton-formulaire-nouveau, .bouton-formulaire-modif").click(function() {
		var idFormulaire = $(this).data("form");
		var formulaire = $(idFormulaire);
		var modal = formulaire.closest(".modal");
		var url = $(this).data("url");
		
		if (!url) {
			return;
		}
		
		if ($(this).hasClass("bouton-formulaire-modif")) {
			$(modal).find(".bouton-creer").hide();
			$(modal).find(".bouton-modifier").show();
			$(modal).find(".bouton-supprimer").show();
		} else {
			$(modal).find(".bouton-creer").show();
			$(modal).find(".bouton-modifier").hide();
			$(modal).find(".bouton-supprimer").hide();
		}
		
		getForm(formulaire, url);
		if (modal) {
			$(modal).modal();
		}
	});
			

	$(".bouton-creer, .bouton-modifier, .bouton-supprimer").click(function(event) {
		event.preventDefault();
		idFormulaire = $(this).data("form");
		formulaire = $(idFormulaire);
		url = $(this).data("url");
		postForm(formulaire, url);
	})
	
	afficherMessage();
	
});


function getForm(formulaire, formUrl) {
	
	$(formulaire).html("");
	
	var request = $.ajax({
		type: "GET",
		url: formUrl
	})
	
	request.done(function(htmlResponse, textStatus, jqXHR) {		
		formulaire.html(htmlResponse);
		$(formulaire).trigger("formFilled");
	});
	
	request.fail(function(jqXHR) {
		console.log("échec ajax.");
	});
};


function postForm(formulaire, formUrl) {
		
	var request = $.ajax({
		type: "POST",
		url: formUrl,
		data: formulaire.serialize()
	});
	
	request.done(function(htmlResponse, textStatus, jqXHR) {
		$(formulaire).html(htmlResponse);
		$(formulaire).trigger("formFilled");
		var message = jqXHR.getResponseHeader("appMessage");
		if (message) {
			sessionStorage.setItem("appMessage", message);
		}
		location.reload();
	});
	
	request.fail(function(jqXHR) {
		$(formulaire).html(jqXHR.responseText);
		$(formulaire).trigger("formFilled");
		var message = jqXHR.getResponseHeader("appMessage");
		if (message) {
			sessionStorage.setItem("appMessage", message);
			afficherMessage();
		}
	});
	
};
