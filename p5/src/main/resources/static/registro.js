function compruebaPass() {
  let correcto = false;
  correcto = document.getElementById("password").value === 
             document.getElementById("password2").value;
  if (correcto) mostrarAviso();
  else mostrarAviso('✖︎ Contraseña inválida', 'error');
  return correcto;
}

function registrarUsuario(datosJsonFormulario) {
  if (!compruebaPass()) return;
  fetch('/api/users', {method: 'post', body: datosJsonFormulario, headers: {'content-type': 'application/json'}})
    .then(response => {
      if (response.ok) location.href = 'login.html?registrado';
      else if (response.status === 409) mostrarAviso('✖︎ Usuario ya registrado', 'error');
      else mostrarAviso('✖︎ Error en el registro', 'error');
    });
}

function mostrarAviso(texto, tipo) {
  const aviso = document.getElementById("aviso");
  aviso.textContent = texto;
  aviso.className = tipo;
}

function form2json(event) {
  event.preventDefault();
  const data = new FormData(event.target);
  return JSON.stringify(Object.fromEntries(data.entries()));
}