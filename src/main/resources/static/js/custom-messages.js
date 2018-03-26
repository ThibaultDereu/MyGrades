/**
 *
 * En retour des requête ajax, un message peut être retourné dans le header "appMessage".
 * Pour pouvoir afficher le message même après refresh de la page, stockage du message dans un objet de 
 * sessionStorage (persistant à travers la navigation). 
 * Au rafraîchissement, extraction de cet objet appMessage et affichage s'il existe.
 *     
 */

$(document).ready(function() {		
	
	$(".ajax-form").submit(function(event) {
		event.preventDefault();
	})
		
	afficherMessage();
	
});


function afficherMessage() {
	
	message = sessionStorage.getItem("appMessage");
	sessionStorage.removeItem("appMessage");
	
	if (!message) {
		message = $(".app-message").html();
	}
	
	if (message) {
		$(".app-message").stop();
		$(".app-message").queue(function() {
			$(this).html(message).dequeue();
		});
		$(".app-message").slideDown(400, function() {
			$(".app-message").delay(3000).slideUp();
		});		
	}
}


