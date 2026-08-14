/**
 * Agrega un producto al carrito asumiendo cantidad = 1.
 * @param {HTMLFormElement} formulario - El objeto form que contiene el ID del producto.
 */
function addCart(formulario, boton) {

    var idAnuncio = $(formulario).find('input[name="idAnuncio"]').val();
    var ruta = $(formulario).attr('action') || '/carrito/agregar';

    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");

    // Prevent multiple clicks while processing
    $(boton)
        .prop('disabled', true)
        .html('<i class="fas fa-spinner fa-spin me-2"></i> Agregando...');

    $.ajax({
        url: ruta,
        type: 'POST',

        data: {
            idAnuncio: idAnuncio
        },

        beforeSend: function (xhr) {

            if (csrfHeader && csrfToken) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }

        },

        success: function (response) {

            // Update cart fragment
            $("#resultBlock").html(response);

            // Save this announcement in sessionStorage
            sessionStorage.setItem("cart_" + idAnuncio, "true");

            // Change button to "Agregado"
            $(boton)
                .removeClass('btn-primary')
                .addClass('btn-success')
                .html('<i class="fas fa-check me-2"></i> Agregado')
                .prop('disabled', true);

            console.log("Producto agregado al carrito.");

        },

        error: function (xhr, status, error) {

            // Allow user to try again
            $(boton)
                .prop('disabled', false)
                .removeClass('btn-success')
                .addClass('btn-primary')
                .html('<i class="fas fa-cart-plus me-2"></i> Agregar al carrito');

            var mensaje = xhr.responseText || 'Error en la conexión.';

            alert("Error al agregar producto: " + mensaje);
        }
    });
}

$(document).ready(function () {

    $("input[name='idAnuncio']").each(function () {

        var idAnuncio = $(this).val();

        // Check if this announcement was already added
        if (sessionStorage.getItem("cart_" + idAnuncio) === "true") {

            var boton = $(this)
                .closest("form")
                .find("button");

            $(boton)
                .removeClass("btn-primary")
                .addClass("btn-success")
                .html('<i class="fas fa-check me-2"></i> Agregado')
                .prop("disabled", true);
        }

    });

});



// funcion para hacer un preview de una imagen 
function mostrarImagen(input) {
    if (input.files && input.files[0]) {
        const imagen = input.files[0];
        const maximo = 512 * 1024; //Se limita el tamaño a 512 Kb las imágenes.
        if (imagen.size <= maximo) {
            var lector = new FileReader();
            lector.onload = function (e) {
                $('#blah').attr('src', e.target.result).height(200);
            };
            lector.readAsDataURL(input.files[0]);
        } else {
            alert("La imagen seleccionada es muy grande... no debe superar los 512 Kb!");
        }
    }
}

//Para insertar información en el modal según el registro...
document.addEventListener('DOMContentLoaded', function () {
    const confirmModal = document.getElementById('confirmModal');
    confirmModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        document.getElementById('modalId').value = button.getAttribute('data-bs-id');
        document.getElementById('modalDescripcion').textContent = button.getAttribute('data-bs-descripcion');
    });
});

//Para quitar toast
setTimeout(() => {
    document.querySelectorAll('.toast').forEach(t => t.classList.remove('show'));
}, 4000);
