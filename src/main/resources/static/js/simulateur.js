	$(document).ready(function() {

		// initialiser tous les calculateurs
		var request = $.ajax({
			type : "GET",
			url : window.location.href + "/getCalculateurs"
		});

		request.done(function(htmlResponse, textStatus, jqXHR) {
			refreshCalculateurs(htmlResponse);
		});

		request.fail(function(jqXHR) {
			console.log("échec ajax.");
		});

		// fixation d'un objectif
		$(".range-objectif").on("change mouseup", function() {

			var idCalculateur = $(this).data("id");
			var noteObjectif = $(this).val();

			var request = $.ajax({
				type : "POST",
				url : window.location.href + "/setNoteObjectif",
				data : {
					"idCalculateur" : idCalculateur,
					"noteObjectif" : noteObjectif
				}
			});

			request.done(function(htmlResponse, textStatus, jqXHR) {
				refreshCalculateurs(htmlResponse);
			});

			request.fail(function(jqXHR) {
				console.log("échec ajax.");
			});
		});

		// suppression d'un objectif
		$(".bouton-annuler-objectif").click(function() {

			var idCalculateur = $(this).data("id");

			var request = $.ajax({
				type : "POST",
				url : window.location.href + "/annulerNoteObjectif",
				data : {
					"idCalculateur" : idCalculateur
				}
			});

			request.done(function(htmlResponse, textStatus, jqXHR) {
				refreshCalculateurs(htmlResponse);
			});

			request.fail(function(jqXHR) {
				console.log("échec ajax.");
			});
		});

		// afficher la valeur de range objectif lors de son utilisation
		$(".range-objectif").on("input", function() {

			var idCalc = $(this).data("id");
			var valeur = $(this).val();
			valeur = Math.round(valeur * 100) / 100;
			$("#caption-note" + idCalc).text(valeur);

		});

	});

	function refreshCalculateurs(calculateurs) {

		for ( var index in calculateurs) {

			var calc = calculateurs[index];

			var progressSousMin = $("#progress-sous-min" + calc.id);
			var progressNote = $("#progress-note" + calc.id);
			var range = $("#range" + calc.id);
			var caption = $("#caption-note" + calc.id);
			var flagObjectif = $("#flag-objectif" + calc.id);
			var boutonAnnulerObjectif = $("#bouton-annuler-objectif" + calc.id);

			// progress-note : class
			if (arrondirDouble(calc.noteMin) == arrondirDouble(calc.noteMax)) {
				progressNote.addClass("progress-note-unique");
				progressNote.removeClass("progress-note-variable");
				progressNote.removeClass("progress-note-objectif");
			} else if (calc.noteObjectif != null) {
				progressNote.removeClass("progress-note-unique");
				progressNote.removeClass("progress-note-variable");
				progressNote.addClass("progress-note-objectif");
			} else {
				progressNote.removeClass("progress-note-unique");
				progressNote.addClass("progress-note-variable");
				progressNote.removeClass("progress-note-objectif");
			}

			// range : min, max, value
			range.attr({
				"min" : calc.noteMin,
				"max" : calc.noteMax,
				"value" : calc.noteObjectif
			});

			// caption-note : html
			if (arrondirDouble(calc.noteMin) == arrondirDouble(calc.noteMax)) {
				flagObjectif.hide();
				boutonAnnulerObjectif.hide();
				caption.html(arrondirDouble(calc.noteMin));
			} else if (calc.noteObjectif != null) {
				flagObjectif.show();
				boutonAnnulerObjectif.show();
				caption.html(arrondirDouble(calc.noteObjectif));
			} else {
				flagObjectif.hide();
				boutonAnnulerObjectif.hide();
				caption.html(arrondirDouble(calc.noteMin) + " -> " + arrondirDouble(calc.noteMax));
			}

			// progress-sous-min : change width
			var pctSousMin = 100 * calc.noteMin / 20 + "%";
			$(progressSousMin).outerWidth(pctSousMin);

			// progress-note : change width
			var pctNote = 100 * (calc.noteMax - calc.noteMin) / 20 + "%";
			$(progressNote).outerWidth(pctNote);

		}
		
		function arrondirDouble(double) {
			return Math.round(double * 100) / 100;
		}
	}